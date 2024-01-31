package com.nebula.notescape.service;

import com.nebula.notescape.payload.request.NoteRequest;
import com.nebula.notescape.payload.response.ApiResponse;

public interface INoteService {

    ApiResponse create(String token, NoteRequest noteRequest);

    ApiResponse getById(Long id);

    ApiResponse deleteById(String token, Long id);

}
