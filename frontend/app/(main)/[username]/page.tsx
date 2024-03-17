"use client";

import React, { useEffect, useState } from "react";
import Card from "./components/user-card";
import { twMerge } from "tailwind-merge";
import NotesScroll from "./components/notes-scroll";
import MoviesScroll from "./components/movies-scroll";
import { UserType } from "@/types/auth";
import { instance as axios } from "@/app/lib/data";
import { toast } from "react-toastify";
import LoadingScreen from "@/app/components/loading-screen";

export default function Page({ params }: { params: { username: string } } ) {
  const [ page, setPage ] = useState("notes");
  const [user,setUser] = useState<UserType | null>(null);
  const [status,setStatus] = useState<'loading' | 'found' | 'not-found'>('loading');

  useEffect(() => {
    axios.get(`/users/find/${params.username}`)
      .then(res => res.data)
      .then(data => {
        if (data.status && data.status == 200) {
          setStatus('found');
        } else {
          setStatus('not-found')
        }
      })
  }, []);

  return (
    <main className={`flex min-h-screen gap-2 flex-col items-center justify-start ${status !== 'loading' && "pt-20"} px-10 w-[640px]`}>
      {
        status === "found" ? (
        <>
          <Card username={params.username} />
          <ul className="w-full flex justify-between items-center mb-2">
            <li
              className={twMerge(
                "w-1/2 cursor-pointer text-center py-2",
                page === "notes" ? "border-b" : "border-b border-b-gray-800",
              )}
              onClick={() => setPage("notes")}
            >
              Notes
            </li>
            <li
              className={twMerge(
                "w-1/2 cursor-pointer text-center py-2",
                page === "movies" ? "border-b" : "border-b border-b-gray-800",
              )}
              onClick={() => setPage("movies")}
            >
              Movies
            </li>
          </ul>
          {page === "notes" ? (
            <NotesScroll username={params.username} />
          ) : page === "movies" ? (
            <MoviesScroll />
          ) : (
            <></>
          )}
        </>
        ) : status === 'not-found' ? (<>
          Not found
        </>) : (<LoadingScreen fullScreen/>)
      }
    </main>
  );
}
