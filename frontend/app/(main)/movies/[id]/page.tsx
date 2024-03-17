"use client";

import React from "react";
import MovieCard from "../components/movie-card";

export default function Page({ params }: { params: { id: number } }) {

  return (
    <div className="flex flex-col items-center justify-start pt-24 w-[640px]">
      <div className="flex w-full flex-col justify-center items-center">
        <MovieCard id={params.id} />
      </div>
      <span className="border-b border-gray-500 w-full my-4"></span>
      <span className="w-full">
      </span>
    </div>
  );
}
