package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.persistence.dao.FollowDao;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.Follow;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.key.FollowKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class InteractionServiceImplTest {

  @Mock
  private UserDao userDao;

  @Mock
  private FollowDao followDao;

  @InjectMocks
  private InteractionServiceImpl interactionService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Follows a user successfully")
  public void followsUserSuccessfully() {
    FollowKey followKey = new FollowKey(1L, 2L);
    when(followDao.getById(followKey)).thenReturn(Optional.empty());
    when(userDao.getById(1L)).thenReturn(Optional.of(new User()));
    when(userDao.getById(2L)).thenReturn(Optional.of(new User()));

    interactionService.follow(1L, 2L);

    verify(followDao, times(1)).save(any(Follow.class));
  }

  @Test
  @DisplayName("Updates follow when already following")
  public void updatesFollowWhenAlreadyFollowing() {
    FollowKey followKey = new FollowKey(1L, 2L);
    when(followDao.getById(followKey)).thenReturn(Optional.of(new Follow()));

    interactionService.follow(1L, 2L);

    verify(followDao, times(1)).save(any(Follow.class));
  }

  @Test
  @DisplayName("Throws exception when follower id is invalid")
  public void throwsExceptionWhenFollowerIdIsInvalid() {
    assertThrows(ApiException.class, () -> interactionService.follow(-1L, 2L));
  }

  @Test
  @DisplayName("Throws exception when following id is invalid")
  public void throwsExceptionWhenFollowingIdIsInvalid() {
    assertThrows(ApiException.class, () -> interactionService.follow(1L, -2L));
  }

  @Test
  @DisplayName("Throws exception when user not found")
  public void throwsExceptionWhenUserNotFound() {
    FollowKey followKey = new FollowKey(1L, 2L);
    when(followDao.getById(followKey)).thenReturn(Optional.empty());
    when(userDao.getById(1L)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> interactionService.follow(1L, 2L));
  }
}