package com.nebula.notescape.service;

import com.nebula.notescape.payload.request.NoteRequest;
import com.nebula.notescape.payload.response.ApiResponse;

public interface INoteService {

    ApiResponse create(String token, NoteRequest noteRequest);

    ApiResponse getById(Long id);

    ApiResponse getAllNotesByUser(Long userId, String email, String username, Integer page, Integer size, String[] sort);

    ApiResponse getPublicNotesByUserId(Long id, String email, String username, Integer page, Integer size, String[] sort);

    ApiResponse getPublicNotesByMovieId(Long id, Integer page, Integer size, String[] sort);

    ApiResponse getPrivateNotesByUserId(String token, Long userId, Integer page, Integer size, String[] sort);

    ApiResponse deleteById(String token, Long id);

}
