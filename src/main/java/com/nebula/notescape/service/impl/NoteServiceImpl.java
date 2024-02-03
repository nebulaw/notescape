package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.CustomMessageException;
import com.nebula.notescape.exception.IncorrectParameterException;
import com.nebula.notescape.exception.NoteNotFoundException;
import com.nebula.notescape.exception.UserNotFoundException;
import com.nebula.notescape.payload.request.NoteRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.NoteResponse;
import com.nebula.notescape.persistence.dao.NoteDao;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.Note;
import com.nebula.notescape.persistence.entity.User;
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
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse create(String token, NoteRequest noteRequest) {
        token = jwtUtil.parseToken(token);

        // extract
        String email = jwtUtil.extractClaim(token, Claims::getSubject);

        if (!email.equals(noteRequest.getEmail())) {
            throw new CustomMessageException("Invalid email provided", HttpStatus.BAD_REQUEST);
        }

        // TODO: validate noteRequest
        Optional<User> userOptional = userDao.getByEmail(email);
        Optional<Note> parentNote = noteDao.getById(noteRequest.getParentId());

        Note note = Note.builder()
                .movieId(noteRequest.getMovieId())
                .movieName(noteRequest.getMovieName())
                .noteType(noteRequest.getNoteType())
                .author(userOptional.orElseThrow(() ->
                        new UserNotFoundException(email)))
                .context(noteRequest.getContext())
                .access(noteRequest.getAccess())
                .likeCount(0L)
                .parentNote(parentNote.orElse(null))
                .build();

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
            throw new IncorrectParameterException()
                    .parameter("id", id);
        } else {
            Optional<Note> noteOptional = noteDao.getById(id);

            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(NoteResponse.of(noteOptional.orElseThrow(() ->
                            new NoteNotFoundException())))
                    .build();
        }
    }

    @Override
    public ApiResponse getAllNotesByUser(Long userId, String email, String username, Integer page, Integer size, String[] sort) {
        User user = extractUserFromIdentifiers(userId, email, username);

        Pageable pageable = createPageable(page - 1, size, sort);
        Page<Note> notePage = noteDao
                .getAllNotesByUser(user, pageable);

        return ApiResponse.builder()
                .data(notePage.stream().map(NoteResponse::of).toList())
                .status(HttpStatus.OK)
                .put("page", page)
                .put("totalPages", notePage.getTotalPages())
                .put("size", notePage.getContent().size())
                .build();
    }

    @Override
    public ApiResponse getPublicNotesByUserId(Long id, String email, String username, Integer page, Integer size, String[] sort) {
        User user = extractUserFromIdentifiers(id, email, username);

        Pageable pageable = createPageable(page - 1, size, sort);
        Page<Note> notePage = noteDao
                .getPublicNotesByUser(user, pageable);

        return ApiResponse.builder()
                .data(notePage.stream().map(NoteResponse::of).toList())
                .status(HttpStatus.OK)
                .put("page", page)
                .put("totalPages", notePage.getTotalPages())
                .put("size", notePage.getContent().size())
                .build();
    }

    @Override
    public ApiResponse getPublicNotesByMovieId(Long movieId, Integer page, Integer size, String[] sort) {
        Pageable pageable = createPageable(page - 1, size, sort);
        Page<Note> notePage = noteDao.getPublicNotesByMovieId(movieId, pageable);

        return ApiResponse.builder()
                .data(notePage.stream().map(NoteResponse::of).toList())
                .status(HttpStatus.OK)
                .put("page", page)
                .put("totalPages", notePage.getTotalPages())
                .put("size", notePage.getContent().size())
                .build();
    }

    @Override
    public ApiResponse getPrivateNotesByUserId(String token, Long userId, Integer page, Integer size, String[] sort) {
        if (!StringUtils.hasText(token)) {
            throw new IncorrectParameterException()
                    .parameter("token", token);
        } else {
            Optional<User> userOptional = userDao.getById(userId);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException();
            }
            token = jwtUtil.parseToken(token);
            // validate user authority
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(userOptional.get().getEmail());
            if (!jwtUtil.isValid(token, userDetails)) {
                throw new CustomMessageException("Invalid or expired token", HttpStatus.BAD_REQUEST);
            }

            // create page
            Pageable pageable = createPageable(page - 1, size, sort);
            Page<Note> notePage = noteDao
                    .getPrivateNotesByUser(userOptional.get(), pageable);

            return ApiResponse.builder()
                    .data(notePage.stream()
                            .map(NoteResponse::of)
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
            throw new IncorrectParameterException()
                    .parameter("id", id);
        } else {
            token = jwtUtil.parseToken(token);
            String email = jwtUtil.parseToken(token);

            Optional<Note> noteOptional = noteDao.getById(id);
            if (noteOptional.isEmpty()) {
                log.info("Note was not found by noteId={}", id);
                throw new NoteNotFoundException();
            }

            String authorEmail = noteOptional.get().getAuthor().getEmail();
            if (!email.equals(authorEmail)) {
                throw new CustomMessageException("Invalid email provided", HttpStatus.BAD_REQUEST);
            } else {
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
                throw new UserNotFoundException();
            } else if (StringUtils.hasText(email) ||
                    StringUtils.hasText(username)) {
                throw new UserNotFoundException(
                        StringUtils.hasText(email) ? email : username);
            } else {
                throw new CustomMessageException(
                        "You must provide userId, email, or username",
                        HttpStatus.BAD_REQUEST);
            }
        }

        return userOptional.get();
    }

}
