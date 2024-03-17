"use client"

import React, { useState } from "react";
import Image from "next/image";
import  { useMovie, useMoviePoster } from "@/hooks/use-movie";
import Button from "@/app/components/button";
import { useRouter } from "next/navigation";
import { twMerge } from "tailwind-merge";
import LoadingScreen from "@/app/components/loading-screen";


interface Props {
  id: number;
}

export default function MovieCard({ id }: Props) {
  const { item: movie, isLoading, error } = useMovie(id);
  const { image, imageLoading } = useMoviePoster(movie);
  const [ watched, setWatched ] = useState(false);
  const [ watchLater, setWatchLater ] = useState(false);
  const router = useRouter();

  if (isLoading && !error) {
    return <LoadingScreen />;
  }

  if (error !== "none") {
    return <div>Error</div>
  }

  return (
    <div className="flex h-full flex-col py-3 gap-4 w-full justify-between items-center">
      <div className={`flex flex-col w-full h-full gap-4 justify-start items-center overflow-hidden`}>
        <div className={twMerge(
          "w-[230px] h-[345px] rounded-xl border border-gray-200 flex items-center",
          isLoading && "skeleton border-0"
        )}>
          {
            !isLoading && imageLoading === "loaded" &&
              <>
                <Image
                  src={image}
                  alt={"hey"}
                  width={230}
                  height={0}
                  priority
                  style={{
                    width: "auto",
                    height: "100%",
                  }}
                  className="my-auto mx-auto rounded-xl"
                />
              </>
          }
        </div>
        <div className="flex w-full flex-col gap-2 items-center">
          <span className={twMerge("text-4xl text-center leading-10 font-semibold", isLoading && "h-11 rounded-lg skeleton w-64")}>
            {movie?.title}
          </span>
          <span className={twMerge("text-md px-4", isLoading && "h-11 skeleton rounded-lg px-4 w-full")}>
            {movie?.overview}
          </span>
        </div>
      </div>
      <div className="flex gap-3 w-full">
        <Button
          className={twMerge("w-1/3", isLoading && "skeleton border-0" )}
          active={true}
          values={"Create note"}
          loading={isLoading}
          onClick={() => {router.push(`/create?movie_id=${movie?.id}`)}}
        />
        <Button
          className={twMerge("w-1/3", isLoading && "skeleton border-0" )}
          active={watchLater}
          values={["Unwatchlist", "Watchlist"]}
          loading={isLoading}
          onClick={() => setWatchLater(!watchLater)}
        />
        <Button
          className={twMerge("w-1/3", isLoading && "skeleton border-0" )}
          active={watched}
          values={["Unwatch", "Watch"]}
          loading={isLoading}
          onClick={() => setWatched(!watched)}
        />
      </div>
    </div>
  );
}
