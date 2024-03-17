package com.nebula.notescape.controller;

import com.nebula.notescape.payload.request.NoteRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.service.INoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/notes/")
@RestController
public class NoteController {

    private final INoteService noteService;

    @PostMapping("/create")
    public ApiResponse create(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody NoteRequest noteRequest
    ) {
        return noteService.create(token, noteRequest);
    }

    @GetMapping("/find/{id}")
    public ApiResponse get(@PathVariable Long id) {
        return noteService.getById(id);
    }

    @GetMapping("/discover/user")
    public ApiResponse getPublicNotesByUser(
            @RequestParam(defaultValue = "-1") Long userId,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size,
            @RequestParam(defaultValue = "createDate,desc") String[] sort
    ) {
        return noteService.getPublicNotesByUserId(userId, email, username, page, size, sort);
    }

    @GetMapping("/discover/movie")
    public ApiResponse getPublicNotesByMovieId(
            @RequestParam Long movieId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size,
            @RequestParam(defaultValue = "createDate,desc") String[] sort
    ) {
        return noteService.getPublicNotesByMovieId(movieId, page, size, sort);
    }

    @GetMapping("/private")
    public ApiResponse getPrivateNotesByUserId(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(defaultValue = "") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size,
            @RequestParam(defaultValue = "createDate,desc") String[] sort
    ) {
        return noteService.getPrivateNotesByUserId(token, userId, page, size, sort);
    }

    @GetMapping("/user")
    public ApiResponse getAllNotesByUser(
            @RequestParam(defaultValue = "-1") Long userId,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size,
            @RequestParam(defaultValue = "createDate,desc") String[] sort
    ) {
        return noteService.getAllNotesByUser(userId, email, username, page, size, sort);
    }

    @DeleteMapping("/delete")
    public ApiResponse delete(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "userId") Long userId
    ) {
        return noteService.deleteById(token, userId);
    }

}
