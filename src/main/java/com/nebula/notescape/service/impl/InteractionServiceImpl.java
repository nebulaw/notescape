package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.exception.Exceptions;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.UserResponse;
import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.UserRelationType;
import com.nebula.notescape.persistence.dao.*;
import com.nebula.notescape.persistence.entity.*;
import com.nebula.notescape.persistence.key.FollowKey;
import com.nebula.notescape.persistence.key.LikeKey;
import com.nebula.notescape.persistence.key.WatchKey;
import com.nebula.notescape.persistence.key.WishKey;
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
  private final LikeDao likeDao;
  private final WatchDao watchDao;
  private final WishDao wishDao;
  private final FollowDao followDao;

  private void validateId(Long id) {
    if (id == null || id < 1L) {
      throw new ApiException(Exceptions.INVALID_ID);
    }
  }

  @Override
  public ApiResponse follow(Long followerId, Long followingId) {
    validateId(followerId);
    validateId(followingId);

    // check if exists
    if (followDao.existsById(new FollowKey(followerId, followingId))) {
      Optional<Follow> followOpt = followDao
          .getById(new FollowKey(followerId, followingId));
      followOpt.ifPresent(follow -> {
        follow.setUpdateDate(LocalDateTime.now());
        follow.setRecordState(RecordState.ACTIVE);
        User from = follow.getFollower();
        from.setFollowingCount(relu(from.getFollowingCount() + 1));
        User to = follow.getFollowing();
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

  // This is a joke
  private Integer relu(Integer value) {
    return value <= 0 ? 0 : value;
  }

  @Override
  public ApiResponse unfollow(Long followerId, Long followingId) {
    validateId(followerId);
    validateId(followingId);

    followDao
        .getById(new FollowKey(followerId, followingId))
        .ifPresent(follow -> {
          follow.setUpdateDate(LocalDateTime.now());
          follow.setRecordState(RecordState.DELETED);
          User from = follow.getFollower();
          from.setFollowingCount(relu(from.getFollowingCount() - 1));
          User to = follow.getFollowing();
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
    validateId(user1Id);
    validateId(user2Id);

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
    validateId(id);
    Page<Follow> followPage = followDao
        .getFollowingsByUserId(id, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(followPage.stream()
            .map(follow -> UserResponse.of(follow.getFollowing())))
        .put("page", page)
        .put("totalPages", followPage.getTotalPages())
        .put("size", followPage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getFollowersByUserId(Long id, Integer page, Integer size, String[] sort) {
    validateId(id);
    Page<Follow> followPage = followDao
        .getFollowersByUserId(id, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(followPage.stream()
            .map(follow -> UserResponse.of(follow.getFollower()))
            .toList())
        .put("page", page)
        .put("totalPages", followPage.getTotalPages())
        .put("size", followPage.getContent().size())
        .build();
  }

  @Override
  public ApiResponse getWatchlistByUserId(Long userId, Integer page, Integer size, String[] sort) {
    validateId(userId);
    Page<Watch> watchPage = watchDao
        .getWatchPageByUserId(userId, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(watchPage.stream()
            .peek(watch -> watch.getUser().setPassword(""))
            .toList())
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse getWishlistByUserId(Long userId, Integer page, Integer size, String[] sort) {
    validateId(userId);

    Page<Wish> wishlistPage = wishDao
        .getWishPageByUserId(userId, createPageable(page, size, sort));
    return ApiResponse.builder()
        .data(wishlistPage.stream()
            .peek(wish -> wish.getUser().setPassword(""))
            .toList())
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse like(Long userId, Long noteId) {
    validateId(userId);
    validateId(noteId);
    LikeKey likeKey = new LikeKey(userId, noteId);

    Optional<User> userOpt = userDao.getById(userId);
    if (userOpt.isEmpty()) {
      throw new ApiException(Exceptions.USER_NOT_FOUND);
    }

    Optional<Note> noteOpt = noteDao.getById(noteId);
    if (noteOpt.isEmpty()) {
      throw new ApiException(Exceptions.NOTE_NOTE_FOUND);
    }
    Note note = noteOpt.get();
    note.setLikeCount(note.getLikeCount() + 1);
    noteDao.update(note);

    likeDao
        .getById(likeKey)
        .ifPresentOrElse((like) -> {
          like.setUpdateDate(LocalDateTime.now());
          like.setRecordState(RecordState.ACTIVE);
          likeDao.save(like);
        }, () -> likeDao.save(new Like(likeKey, userOpt.get(), noteOpt.get())));

    return ApiResponse.builder()
        .data("Successfully liked")
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse unlike(Long userId, Long noteId) {
    validateId(userId);
    validateId(noteId);

    likeDao
        .getById(new LikeKey(userId, noteId))
        .ifPresent(like -> {
          Optional<Note> noteOpt = noteDao.getById(noteId);
          if (noteOpt.isEmpty()) {
            return;
          }
          Note note = noteOpt.get();
          note.setLikeCount(note.getLikeCount() == 0 ?
              0 : note.getLikeCount() - 1);
          System.out.println("Like count after unlike: " + note.getLikeCount());
          like.setUpdateDate(LocalDateTime.now());
          like.setRecordState(RecordState.DELETED);
          likeDao.save(like);
          noteDao.update(note);
        });

    return ApiResponse.builder()
        .data("Successfully unliked")
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse watch(Long userId, Long movieId) {
    validateId(userId);
    validateId(movieId);

    WatchKey watchKey = new WatchKey(userId, movieId);

    watchDao
        .getById(watchKey)
        .ifPresentOrElse(watch -> {
          watch.setRecordState(RecordState.ACTIVE);
          watch.setUpdateDate(LocalDateTime.now());
          watchDao.save(watch);
        }, () -> {
          Optional<User> userOpt = userDao.getById(userId);
          if (userOpt.isEmpty()) {
            throw new ApiException(Exceptions.USER_NOT_FOUND);
          }
          watchDao.save(new Watch(watchKey, userOpt.get()));
        });

    return ApiResponse.builder()
        .data("Saved to watchlist")
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse unwatch(Long userId, Long movieId) {
    validateId(userId);
    validateId(movieId);

    watchDao
        .getById(new WatchKey(userId, movieId))
        .ifPresent(watch -> {
          watch.setRecordState(RecordState.DELETED);
          watch.setUpdateDate(LocalDateTime.now());
          watchDao.save(watch);
        });

    return ApiResponse.builder()
        .data("Removed from watchlist")
        .status(HttpStatus.OK)
        .build();
  }

  @Override
  public ApiResponse wish(Long userId, Long movieId) {
    validateId(userId);
    validateId(movieId);

    wishDao
        .getById(new WishKey(userId, movieId))
        .ifPresentOrElse(wish -> {
          wish.setRecordState(RecordState.ACTIVE);
          wish.setUpdateDate(LocalDateTime.now());
          wishDao.save(wish);
        }, () -> {
          Optional<User> userOpt = userDao.getById(userId);
          if (userOpt.isEmpty()) {
            throw new ApiException(Exceptions.USER_NOT_FOUND);
          }
          wishDao.save(new Wish(new WishKey(userId, movieId), userOpt.get()));
        });

    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data("Saved to wishlist")
        .build();
  }

  @Override
  public ApiResponse unwish(Long userId, Long movieId) {
    validateId(userId);
    validateId(movieId);

    wishDao
        .getById(new WishKey(userId, movieId))
        .ifPresent(wish -> {
          wish.setRecordState(RecordState.DELETED);
          wish.setUpdateDate(LocalDateTime.now());
          wishDao.save(wish);
        });

    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data("Removed from wishlist")
        .build();
  }

}
