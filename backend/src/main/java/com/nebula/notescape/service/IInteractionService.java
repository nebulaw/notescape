package com.nebula.notescape.service;

import com.nebula.notescape.payload.response.ApiResponse;

public interface IInteractionService {

  ApiResponse follow(Long followerId, Long followingId);

  ApiResponse unfollow(Long followerId, Long followingId);

  ApiResponse getRelationType(Long user1Id, Long user2Id);

  ApiResponse getFollowingsByUserId(Long id, Integer page, Integer size, String[] sort);

  ApiResponse getFollowersByUserId(Long id, Integer page, Integer size, String[] sort);

  ApiResponse getWatchlistByUserId(Long userId, Integer page, Integer size, String[] sort);

  ApiResponse getWishlistByUserId(Long userId, Integer page, Integer size, String[] sort);

  ApiResponse like(Long userId, Long noteId);

  ApiResponse unlike(Long userId, Long noteId);

  ApiResponse watch(Long userId, Long movieId);

  ApiResponse unwatch(Long userId, Long movieId);

  ApiResponse wish(Long userId, Long movieId);

  ApiResponse unwish(Long userId, Long movieId);

}
