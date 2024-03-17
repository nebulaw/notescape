import React, { useState } from "react";
import dayjs from "dayjs";
import Image from "next/image";
import SpinnerSvg from "@/assets/spinner.svg";
import {MovieType} from "@/types/tmdb";

const imageBaseUrl: string = "https://image.tmdb.org/t/p/original";

export default function SearchItem({ item }: { item: MovieType }) {
  const imagePath =
    item.poster_path || item.backdrop_path
      ? `${imageBaseUrl}${item.poster_path || item.backdrop_path}`
      : "/abstract.png";
  const [imageLoading, setImageLoading] = useState(true);

  return (
    <a
      href={`/movies/${item.id}`}
      className="flex gap-4 border border-[#4c4c4c] px-3 py-2 w-full rounded-xl"
    >
      <span className={`w-1/6 rounded-2xl bg-cover`}>
        <Image
          draggable={false}
          className=""
          src={!imageLoading ? imagePath : SpinnerSvg}
          alt={item.title}
          width={64}
          height={0}
          style={{
            height: "auto",
            width: "100%",
          }}
          onLoad={() => setImageLoading(false)}
        />
      </span>
      <span className="flex w-5/6 flex-col justify-start pr-4">
        <p className="text-xl font-semibold">{item.title}</p>
        <p className="text-sm text-gray-400">
          {item.release_date
            ? dayjs(item.release_date).format("YYYY")
            : "No Date"}
        </p>
        <p className="line-clamp-2">{item.overview}</p>
      </span>
    </a>
  );
}
