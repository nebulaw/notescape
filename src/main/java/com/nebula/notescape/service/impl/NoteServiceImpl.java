package com.nebula.notescape.service.impl;

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
import com.nebula.notescape.service.INoteService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoteServiceImpl implements INoteService {

    private final UserDao userDao;
    private final NoteDao noteDao;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse create(String token, NoteRequest noteRequest) {
        token = jwtUtil.parseToken(token);
        log.trace("Parsed token: {}", token);

        // extract
        String email = jwtUtil.extractClaim(token, Claims::getSubject);
        log.trace("Extracted email: {}", email);


        if (!email.equals(noteRequest.getEmail())) {
            throw new IncorrectParameterException()
                    .parameter("email", noteRequest.getEmail());
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
                .likeCount(noteRequest.getLikeCount())
                .parentNote(parentNote.orElse(null))
                .build();

        note = noteDao.save(note);
        log.trace("Note: {}", note.toString());
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(NoteResponse.of(note))
                .build();
    }

    @Override
    public ApiResponse getById(Long id) {
        if (id == null || id < 1) {
            throw new IncorrectParameterException()
                    .parameter("id", id);
        } else {
            Optional<Note> noteOptional = noteDao.getById(id);

            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(NoteResponse.of(noteOptional.orElseThrow(() -> {
                        // TODO: note not found exception
                        throw new ResourceNotFoundException();
                    })))
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
                throw new NoteNotFoundException(id);
            }

            String authorEmail = noteOptional.get().getAuthor().getEmail();
            if (!email.equals(authorEmail)) {
                throw new BadCredentialsException("Token email does not match request email");
            } else {
                noteDao.deleteById(id);
                return ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .data("Note deleted successfully")
                        .build();

            }
        }
    }

}
