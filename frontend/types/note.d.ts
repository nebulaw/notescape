import { UserType } from "./auth";

export type NoteCreateType = {
  noteType: "NOTE" | "COMMENT";
  movieId: number;
  movieName: string;
  email: string;
  context: string;
  access: "PRIVATE" | "PUBLIC";
};

export type NoteType = {
  id: number;
  movieId: number;
  movieName: string;
  author: UserType;
  context: string;
  access: "PRIVATE" | "PUBLIC";
  likeCount: number;
  isLiked?: boolean;
};

