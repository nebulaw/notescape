
export type UserType = {
  id: number;
  email: string;
  username: string;
  fullName: string;
  about: string;
  imageUrl: string;
  noteCount: number;
  followingCount: number;
  followerCount: number;
};

export type LoginType = {
  email: string;
  password: string;
};

export type RegisterType = {
  email: string;
  fullName: string;
  username: string;
  password: string;
}

export type AuthResponseType = {
  user: UserType;
  token: string;
}

export type AuthUserType = {
  user: UserType;
  token: string | null;
}

