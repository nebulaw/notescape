"use client";
import React, { ChangeEvent, useEffect, useState } from "react";
import { twMerge } from "tailwind-merge";
import LoadingScreen from "@/app/components/loading-screen";
import { useMovie, useMoviePoster } from "@/hooks/use-movie";
import Image from "next/image";
import Button from "@/app/components/button";
import { useUser } from "@/hooks/use-user";
import { instance as axios, setAuthToken } from "@/app/lib/data";
import { Slide, ToastContainer, toast } from "react-toastify";


export default function Page({
  searchParams
}: {
  searchParams: { movie_id: number; };
}) {
  const {user} = useUser();
  const { item: movie, isLoading, error } = useMovie(searchParams.movie_id);
  const { image, imageLoading } = useMoviePoster(movie);
  const [context, setContext] = useState("");

  const handleContextChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    setContext(e.target.value);
  };

  const handlePublish = () => {
    if (user && context !== "" && movie.title) {
      setAuthToken(user.token);
      axios.post("/notes/create", {
        noteType: "NOTE",
        movieId: searchParams.movie_id,
        movieName: movie.title,
        email: user.user.email,
        context: context,
        access: "PUBLIC",
      }).then(() => {
          toast.info("Note successfully created");
      })
    } else {
      toast.error("Note is empty");
    }
  };

  if (isLoading && !error) {
      return <LoadingScreen />;
  }

  if (error !== "none") {
      return <div>Error</div>;
  }

  return (<>
    <div className="flex flex-col items-center justify-between pt-24 pb-4 w-[640px]">
      <div className="flex h-52 text-white flex-col w-full justify-start items-center">
        <div className={`flex gap-8 w-full justify-start items-center overflow-hidden`}>
          <div className={twMerge(
            "w-1/6 rounded-xl h-36 border-none border-gray-200 justify-start flex items-center",
            isLoading && "skeleton w-1/6 h-36 border-0"
          )}>
            {!isLoading && imageLoading === "loaded" &&
              <Image
                src={image}
                alt={"hey"}
                width={80}
                height={0}
                priority
                style={{
                    width: "100%",
                    height: "auto",
                }}
              className="my-auto mx-auto rounded-xl" />}
          </div>
          <div className="flex w-5/6 flex-col h-full justify-between">
            <span className={twMerge("w-full text-4xl leading-10 font-semibold", isLoading && "h-11 rounded-lg skeleton w-64")}>
              {movie?.title}
            </span>
          </div>
        </div>
      </div>
      <div className="h-full pb-4 w-full">
        <textarea
          className={twMerge(
              "py-4 px-8 h-full w-full border-none focus:border-none hover:border-none",
              (isLoading || imageLoading === "loading") && "skeleton"
          )}
          placeholder={!isLoading && imageLoading === "loaded" ? "Type..." : ""}
          onChange={handleContextChange}
        />
      </div>
      <Button
        className="w-full"
        active={true}
        values="publish"
        loading={isLoading && imageLoading === "loaded"}
        onClick={handlePublish}
      />
    </div>
    </>
  );
}

