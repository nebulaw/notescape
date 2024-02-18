package com.nebula.notescape.controller;

import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.service.IInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/interactions")
@RestController
public class InteractionController {

  private final IInteractionService interactionService;

  @PostMapping("/follow")
  public ApiResponse follow(
      @RequestParam(defaultValue = "-1") Long followerId,
      @RequestParam(defaultValue = "-1") Long followingId
  ) {
    return interactionService.follow(followerId, followingId);
  }

  @DeleteMapping("/unfollow")
  public ApiResponse unfollow(
      @RequestParam(defaultValue = "-1") Long followerId,
      @RequestParam(defaultValue = "-1") Long followingId
  ) {
    return interactionService.unfollow(followerId, followingId);
  }

  @GetMapping("/relation-type")
  public ApiResponse getRelationType(
      @RequestParam(defaultValue = "-1") Long user1Id,
      @RequestParam(defaultValue = "-1") Long user2Id
  ) {
    return interactionService.getRelationType(user1Id, user2Id);
  }

}
