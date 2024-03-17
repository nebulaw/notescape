"use client"

import React from "react";
import Image from "next/image";
import  { useMovie, useMoviePoster } from "@/hooks/use-movie";
import { twMerge } from "tailwind-merge";
import LoadingScreen from "@/app/components/loading-screen";

export default function MovieCard({ id }: { id: number }) {
  const { item: movie, isLoading, error } = useMovie(id);
  const { image, imageLoading } = useMoviePoster(movie);

  if (isLoading && !error) {
    return <LoadingScreen />;
  }

  if (error !== "none") {
    return <div>Error</div>
  }

  return (
    <div className="flex h-52 text-white flex-col w-full justify-start items-center">
      <div className={`flex gap-8 w-full justify-start items-center overflow-hidden`}>
        <div className={twMerge(
          "w-1/6 rounded-xl h-36 border-none border-gray-200 justify-start flex items-center",
          isLoading && "skeleton w-1/6 h-36 border-0"
        )}>
          {
            !isLoading && imageLoading === "loaded" &&
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
                className="my-auto mx-auto rounded-xl"
              />
          }
        </div>
        <div className="flex w-5/6 flex-col">
          <span className={twMerge("w-full text-4xl leading-10 font-semibold", isLoading && "h-11 rounded-lg skeleton w-64")}>
            {movie?.title}
          </span>
        </div>
      </div>
    </div>
  );
}

