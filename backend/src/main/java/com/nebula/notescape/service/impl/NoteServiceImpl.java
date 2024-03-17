package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.exception.Exceptions;
import com.nebula.notescape.payload.request.NoteRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.NoteResponse;
import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.dao.LikeDao;
import com.nebula.notescape.persistence.dao.NoteDao;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.Note;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.key.LikeKey;
import com.nebula.notescape.security.JwtUtil;
import com.nebula.notescape.security.UserDetailsServiceImpl;
import com.nebula.notescape.service.BaseService;
import com.nebula.notescape.service.INoteService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoteServiceImpl extends BaseService implements INoteService {

  private final UserDetailsServiceImpl userDetailsService;
  private final UserDao userDao;
  private final NoteDao noteDao;
  private final LikeDao likeDao;
  private final JwtUtil jwtUtil;

  @Override
  public ApiResponse create(String token, NoteRequest noteRequest) {
    token = jwtUtil.parseToken(token);

    // extract
    String email = jwtUtil.extractClaim(token, Claims::getSubject);

    if (!email.equals(noteRequest.getEmail())) {
      throw new ApiException(Exceptions.INVALID_EMAIL);
    }

    // TODO: validate noteRequest
    Optional<User> userOptional = userDao.getByEmail(email);
    Optional<Note> parentNote = noteDao.getById(noteRequest.getParentId());

    Note note = Note.builder()
        .movieId(noteRequest.getMovieId())
        .movieName(noteRequest.getMovieName())
        .noteType(noteRequest.getNoteType())
        .author(userOptional.orElseThrow(() ->
            new ApiException(Exceptions.INVALID_EMAIL)))
        .context(noteRequest.getContext())
        .access(noteRequest.getAccess())
        .likeCount(0L)
        .parentNote(parentNote.orElse(null))
        .build();

    if (note.getAccess() == Access.PUBLIC) {
      User user = userOptional.get();
      user.setNoteCount(user.getNoteCount() + 1);
      userDao.save(user);
    }

    note = noteDao.save(note);
    log.info("User='{}' created note={}", note.getAuthor().getEmail(), note);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data(NoteResponse.of(note))
        .build();
  }

  @Override
  public ApiResponse getById(Long id) {
    if (id == null || id < 1) {
      log.info("Invalid noteId='{}'", id);
      throw new ApiException(Exceptions.INVALID_ID);
    } else {
      Optional<Note> noteOptional = noteDao.getById(id);

      return ApiResponse.builder()
          .status(HttpStatus.OK)
          .data(NoteResponse.of(noteOptional.orElseThrow(() ->
              new ApiException(Exceptions.NOTE_NOTE_FOUND))))
          .build();
    }
  }

  @Override
  public ApiResponse getAllNotesByUser(Long userId, String email, String username,
                                       Integer page, Integer size, String[] sort) {
    User user = extractUserFromIdentifiers(userId, email, username);

    Pageable pageable = createPageable(page - 1, size, sort);
    Page<Note> notePage = noteDao
        .getAllNotesByUser(user, pageable);

    return ApiResponse.builder()
        .data(notePage.stream()
            .map(NoteResponse::of)
            .peek(note -> note.setIsLiked(likeDao
                .existsById(new LikeKey(user.getId(), note.getId()))))
            .toList())
        .status(HttpStatus.OK)
        .put("page", page)
        .put("totalPages", notePage.getTotalPages())
        .put("size", notePage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getPublicNotesByUserId(Long id, String email, String username,
                                            Integer page, Integer size, String[] sort) {
    User user = extractUserFromIdentifiers(id, email, username);

    Pageable pageable = createPageable(page - 1, size, sort);
    Page<Note> notePage = noteDao
        .getPublicNotesByUser(user, pageable);

    return ApiResponse.builder()
        .data(notePage.stream()
            .map(NoteResponse::of)
            .peek(note -> note.setIsLiked(
                likeDao.existsById(new LikeKey(user.getId(), note.getId()))))
            .toList())
        .status(HttpStatus.OK)
        .put("page", page)
        .put("totalPages", notePage.getTotalPages())
        .put("size", notePage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getPublicNotesByMovieId(Long movieId, Integer page,
                                             Integer size, String[] sort) {
    Pageable pageable = createPageable(page - 1, size, sort);
    Page<Note> notePage = noteDao.getPublicNotesByMovieId(movieId, pageable);

    return ApiResponse.builder()
        .data(notePage.stream()
            .map(NoteResponse::of)
            .peek(note -> note.setIsLiked(likeDao
                .existsById(new LikeKey(note.getAuthor().getId(), note.getId()))))
            .toList())
        .status(HttpStatus.OK)
        .put("page", page)
        .put("totalPages", notePage.getTotalPages())
        .put("size", notePage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getPrivateNotesByUserId(String token, Long userId, Integer page,
                                             Integer size, String[] sort) {
    if (!StringUtils.hasText(token)) {
      throw new ApiException(Exceptions.INVALID_TOKEN);
    } else {
      Optional<User> userOptional = userDao.getById(userId);
      if (userOptional.isEmpty()) {
        throw new ApiException(Exceptions.USER_NOT_FOUND);
      }
      token = jwtUtil.parseToken(token);
      // validate user authority
      UserDetails userDetails = userDetailsService
          .loadUserByUsername(userOptional.get().getEmail());
      if (!jwtUtil.isValid(token, userDetails)) {
        throw new ApiException(Exceptions.INVALID_TOKEN);
      }

      // create page
      Pageable pageable = createPageable(page - 1, size, sort);
      Page<Note> notePage = noteDao
          .getPrivateNotesByUser(userOptional.get(), pageable);

      return ApiResponse.builder()
          .data(notePage.stream()
              .map(NoteResponse::of)
              .peek(note -> note.setIsLiked(likeDao
                  .existsById(new LikeKey(userOptional.get().getId(), note.getId()))))
              .toList())
          .status(HttpStatus.OK)
          .put("page", page)
          .put("totalPages", notePage.getTotalPages())
          .put("size", notePage.getContent().size())
          .build();

    }
  }

  @Override
  public ApiResponse deleteById(String token, Long id) {
    if (id == null || id < 1) {
      throw new ApiException(Exceptions.INVALID_ID);
    } else {
      token = jwtUtil.parseToken(token);
      String email = jwtUtil.parseToken(token);

      Optional<Note> noteOptional = noteDao.getById(id);
      if (noteOptional.isEmpty()) {
        log.info("Note was not found by noteId={}", id);
        throw new ApiException(Exceptions.NOTE_NOTE_FOUND);
      }

      String authorEmail = noteOptional.get().getAuthor().getEmail();
      if (!email.equals(authorEmail)) {
        throw new ApiException(Exceptions.INVALID_EMAIL);
      } else {
        userDao.getByEmail(email).ifPresent(user -> {
          if (noteOptional.get().getAccess() == Access.PUBLIC) {
            user.setNoteCount(user.getNoteCount() - 1);
            userDao.save(user);
          }
        });

        noteDao.deleteById(id);
        log.info("User='{}' deleted note={}",
            noteOptional.get().getAuthor().getEmail(), noteOptional.get());
        return ApiResponse.builder()
            .status(HttpStatus.OK)
            .data("Note deleted successfully")
            .build();

      }
    }
  }

  private User extractUserFromIdentifiers(Long userId, String email, String username) {
    Optional<User> userOptional = Optional.empty();

    if (userId >= 1L && !StringUtils.hasText(email) &&
        !StringUtils.hasText(username)) {
      userOptional = userDao.getById(userId);
    } else if (StringUtils.hasText(email)) {
      userOptional = userDao.getByUsername(email);
    } else if (StringUtils.hasText(username)) {
      userOptional = userDao.getByUsername(username);
    }

    if (userOptional.isEmpty()) {
      if (userId >= 1L) {
        throw new ApiException(Exceptions.INVALID_ID);
      } else if (StringUtils.hasText(email) ||
          StringUtils.hasText(username)) {
        throw new ApiException(username + " was not found");
      } else {
        throw new ApiException("Username, email, or id was not provided");
      }
    }

    return userOptional.get();
  }

}
