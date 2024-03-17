import { instance as axios, setAuthToken } from "@/app/lib/data";
import { useUser } from "@/hooks/use-user";
import { NoteType } from "@/types/note";
import React, { useEffect, useState } from "react";
import NoteCard from "./note-card";

interface Props {
  username: string
}

export default function NotesScroll({ username }: Props) {
  const [ notes, setNotes ] = useState<NoteType[] | []>([]);
  const { user, status } = useUser();

  useEffect(() => {
    if (user && user.user) {
      // find user
      setAuthToken(user.token);
      if (user.user.username === username) {
        axios.get(`/notes/user?userId=${user.user.id}`)
          .then((res) => {
            if (res.data) {
              console.log(res.data)
              setNotes(res.data.data);
            }
          })
          .catch((err) => {
            console.log(err);
          })
      } else {
        axios.get(`/notes/discover/user?username=${username}`)
          .then((res) => {
            if (res.data) {
              setNotes(res.data.data);
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    }
  }, [user, username])
  return (
    <div className="w-full h-full flex flex-col gap-2 pb-4">
      {notes && notes.length > 0 ?
        notes.map((note: any) => (
          <NoteCard key={note.id} note={note} />
        )) : (<div className="h-full flex items-center justify-center">
          No notes
        </div>)}
    </div>
  );
}

