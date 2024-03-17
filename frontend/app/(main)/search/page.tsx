"use client";

import React, { useEffect, useState } from "react";
import SearchItem from "@/app/(main)/search/search-item";
import {MovieType} from "@/types/tmdb";

const useSearch = () => {
  const [keyword, setKeyword] = useState("");
  const [searchResult, setSearchResult] = useState<MovieType[]>([]);

  useEffect(() => {
    if (keyword.length >= 1) {
      fetch(
        `https://api.themoviedb.org/3/search/movie?query=${keyword}&include_adult=false&language=en-US&page=1`,
        {
          method: "GET",
          headers: {
            "Content-type": "application/json",
            Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMzAyYzY2MzIwNDc0YzBhMTg0NWI1MjEwMDMxZjQzMSIsInN1YiI6IjY1OTMxNmM4NGY5YTk5NzY1ZDc3MWExOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.quX_QmPQPjiA2RTlbKjduL70hlBGWSU97o3toMdfw2Q`,
            accept: "application/json",
          },
        },
      )
        .then((res) => res.json())
        .then(
          (result) => {
            setSearchResult(result.results);
          },
          (error) => {
            // console.log(error);
          },
        );
    }
  }, [keyword]);

  return { setKeyword, searchResult };
};

export default function Page({}) {
  const { setKeyword, searchResult } = useSearch();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(e.target.value);
  };

  return (
    <main className="flex min-h-screen gap-2 flex-col items-center justify-start pt-20 px-10 w-[640px]">
      <div className="flex flex-col w-full items-center justify-start">
        <div className="flex w-full mb-8">
          <input
            placeholder="Search"
            type="text"
            className="py-5 text-md rounded-2xl px-6 w-full"
            onChange={handleChange}
            spellCheck={false}
            onLoad={() => {}}
          />
        </div>
        <div className="flex flex-col gap-2 items-center w-full pb-4">
          {searchResult.map((item: any) => (
            <SearchItem key={item.id} item={item} />
          ))}
        </div>
      </div>
    </main>
  );
}
