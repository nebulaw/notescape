package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.exception.Exceptions;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.UserRelationType;
import com.nebula.notescape.persistence.dao.FollowDao;
import com.nebula.notescape.persistence.dao.NoteDao;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.Follow;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.key.FollowKey;
import com.nebula.notescape.service.BaseService;
import com.nebula.notescape.service.IInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InteractionServiceImpl extends BaseService implements IInteractionService {

  private final NoteDao noteDao;
  private final UserDao userDao;
  private final FollowDao followDao;

  @Override
  public ApiResponse follow(Long followerId, Long followingId) {
    if (followerId == null || followerId < 1L) {
      throw new ApiException(Exceptions.INVALID_FOLLOWER_ID);
    }
    if (followingId == null || followingId < 1L) {
      throw new ApiException(Exceptions.INVALID_FOLLOWING_ID);
    }

    // check if exists
    if (followDao.existsById(new FollowKey(followerId, followingId))) {
      Optional<Follow> followOpt = followDao
          .getById(new FollowKey(followerId, followingId));
      followOpt.ifPresent(follow -> {
        follow.setUpdateDate(LocalDateTime.now());
        follow.setRecordState(RecordState.ACTIVE);
        User from = follow.getFrom();
        from.setFollowingCount(relu(from.getFollowingCount() + 1));
        User to = follow.getTo();
        to.setFollowerCount(relu(to.getFollowerCount() + 1));
        userDao.save(from);
        userDao.save(to);
        followDao.save(follow);
      });
    } else {
      Optional<User> fromOpt = userDao.getById(followerId);
      Optional<User> toOpt = userDao.getById(followingId);
      if (fromOpt.isEmpty() || toOpt.isEmpty()) {
        throw new ApiException(Exceptions.USER_NOT_FOUND);
      }
      Follow follow = new Follow(
          new FollowKey(followerId, followingId), fromOpt.get(), toOpt.get());
      User from = fromOpt.get();
      from.setFollowingCount(relu(from.getFollowingCount() + 1));
      User to = toOpt.get();
      to.setFollowerCount(relu(to.getFollowerCount() + 1));
      userDao.save(from);
      userDao.save(to);
      followDao.save(follow);
    }

    return ApiResponse.builder()
        .data("Successfully followed")
        .status(HttpStatus.OK)
        .build();
  }

  private Integer relu(Integer value) {
    return value <= 0 ? 0 : value;
  }

  @Override
  public ApiResponse unfollow(Long followerId, Long followingId) {
    if (followerId == null || followerId < 1L) {
      throw new ApiException(Exceptions.INVALID_FOLLOWER_ID);
    }
    if (followingId == null || followingId < 1L) {
      throw new ApiException(Exceptions.INVALID_FOLLOWING_ID);
    }

    followDao
        .getById(new FollowKey(followerId, followingId))
        .ifPresent(follow -> {
          follow.setUpdateDate(LocalDateTime.now());
          follow.setRecordState(RecordState.DELETED);
          User from = follow.getFrom();
          from.setFollowingCount(relu(from.getFollowingCount() - 1));
          User to = follow.getTo();
          to.setFollowerCount(relu(to.getFollowerCount() - 1));
          userDao.save(from);
          userDao.save(to);
          followDao.save(follow);
        });

    return ApiResponse.builder()
        .data("Successfully unfollowed")
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse getRelationType(Long user1Id, Long user2Id) {
    if (user1Id == null || user1Id < 1L ||
        user2Id == null || user2Id < 1L) {
      throw new ApiException(Exceptions.INVALID_ID);
    }

    UserRelationType type = UserRelationType.NONE;

    // check follower
    Optional<Follow> var1 = followDao.getById(new FollowKey(user1Id, user2Id));
    Optional<Follow> var2 = followDao.getById(new FollowKey(user2Id, user1Id));

    if (var1.isPresent() && var1.get().getRecordState() == RecordState.ACTIVE &&
        var2.isPresent() && var2.get().getRecordState() == RecordState.ACTIVE) {
      type = UserRelationType.FRIENDS;
    } else if (var1.isPresent() && var1.get().getRecordState() == RecordState.ACTIVE) {
      type = UserRelationType.FOLLOWING;
    } else if (var2.isPresent() && var2.get().getRecordState() == RecordState.ACTIVE) {
      type = UserRelationType.FOLLOWER;
    }

    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data(type)
        .build();
  }

  @Override
  public ApiResponse getFollowingsByUserId(Long id, Integer page, Integer size, String[] sort) {
    if (id == null || id < 0) {
      throw new ApiException(Exceptions.INVALID_ID);
    }
    Page<Follow> followPage = followDao
        .getFollowingsByUserId(id, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(followPage.getContent())
        .put("page", page)
        .put("totalPages", followPage.getTotalPages())
        .put("size", followPage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getFollowersByUserId(Long id, Integer page, Integer size, String[] sort) {
    if (id == null || id < 0) {
      throw new ApiException(Exceptions.INVALID_ID);
    }
    Page<Follow> followPage = followDao
        .getFollowersByUserId(id, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(followPage.getContent())
        .put("page", page)
        .put("totalPages", followPage.getTotalPages())
        .put("size", followPage.getContent().size())
        .build();
  }

}
