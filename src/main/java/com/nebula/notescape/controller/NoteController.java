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

    @DeleteMapping("/delete/{id}")
    public ApiResponse delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long id
    ) {
        return noteService.deleteById(token, id);
    }

}
