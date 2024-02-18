package com.nebula.notescape.exception;


public class Exceptions {
  public static final String USER_NOT_FOUND = "User was not found";
  public static final String NOTE_NOTE_FOUND = "Note was not found";
  public static final String USER_ALREADY_EXISTS = "User already exists";
  public static final String INVALID_ID = "Invalid id was provided";
  public static final String INVALID_FOLLOWER_ID = "Invalid follower id was provided";
  public static final String INVALID_FOLLOWING_ID = "Invalid following id was provided";
  public static final String INVALID_EMAIL = "Invalid email was provided";
  public static final String INVALID_USERNAME = "Invalid user was provided";
  public static final String INVALID_USERNAME_LENGTH = "Username length must be between 4 and 30 characters";
  public static final String INVALID_ABOUT_LENGTH = "About length must be less than 240 characters";
  public static final String INVALID_REQUEST_HEADER = "Request has an invalid header";
  public static final String INVALID_REQUEST_BODY = "Request body was invalid or not provided";
}
