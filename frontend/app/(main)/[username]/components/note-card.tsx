import { NoteType } from "@/types/note";
import Image from "next/image";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { instance as axios } from "@/app/lib/data";
import { useUser } from "@/hooks/use-user";
import { toast } from "react-toastify";

const collapseContext = (context: string) => {
  if (context.length <= 150) {
    return context;
  } else {
    return context.substring(0, 150);
  }
};

const clampNumber = (number: number) => {
  if (number > 0) {
    if (number < 1_000) {
      return `${number}`;
    } else if (number < 1_000_000) {
      return `${(number / 1_000).toFixed(1)}K`;
    } else if (number < 1_000_000_000) {
      return `${(number / 1_000_000).toFixed(2)}M`;
    } else if (number < 1_000_000_000_000) {
      return `${(number / 1_000_000_000).toFixed(3)}B`;
    }
  }
  return `${number}`;
};

const NoteCard = ({ note }: { note: NoteType }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const collapsedContext = collapseContext(note.context);
  const [likeCount, setLikeCount] = useState(note.likeCount);
  const [isLiked, setIsLiked] = useState(note.isLiked);
  const [commentCount, _] = useState(120_252);
  const router = useRouter();
  const { user } = useUser();

  const handleLike = () => {
    if (!isLiked) {
      setIsLiked(true);
      setLikeCount((prev) => prev + 1);
      axios.post(`/interactions/like?userId=${user?.user.id}&noteId=${note.id}`)
        .then(res => res.data)
        .then(data => {
          toast.info(data.data + "!");
        }, (error) => {
          toast.error(error.data + "!");
        });
    } else {
      setIsLiked(false);
      setLikeCount((prev) => (prev > 0 ? prev - 1 : prev));
      axios.delete(`/interactions/unlike?userId=${user?.user.id}&noteId=${note.id}`)
        .then(res => res.data)
        .then(data => {
          toast.info(data.data + "!");
        }, (error) => {
          toast.error(error.data + "!");
        })
    }
  };

  console.log(note.isLiked);

  return (
    <a className={`flex bg-[#101010] hover:bg-[#181818] gap-3 transition-all cursor-pointer flex-col px-4 rounded-xl py-4`}>
      <div className="flex flex-row gap-2">
        <Image
          src="/abstract.png"
          className="h-14 w-14 rounded-full"
          alt="profile"
          width={56}
          height={56}
          placeholder="blur"
          blurDataURL={"/abstract.png"}
        />
        <div className="flex justify-center flex-col">
          <p className="font-semibold">@{note.author.username}</p>
          <p className={`transition-colors`}>
            on <span
              className={`font-semibold`}
              onClick={() => { router.push(`/movies/${note.movieId}`) }}
            >
              {note.movieName}
            </span>
          </p>
        </div>
      </div>
      <div className="">
        <p>
          {note.context === collapsedContext ? (
            `${note.context}`
          ) : isExpanded ? (
            <>
              {note.context}{" "}
              <span
                onClick={() => setIsExpanded(!isExpanded)}
                className={`cursor-pointer font-bold`}
              >
                less
              </span>
            </>
          ) : (
            <>
              {collapsedContext}...
              <span
                className={`cursor-pointer font-bold`}
                onClick={() => setIsExpanded(!isExpanded)}
              >
                {" "}more
              </span>
            </>
          )}
        </p>
      </div>
      <div className="flex items-center select-none gap-4">
        <span className="flex text-sm items-center justify-end select-none gap-1">
          {!isLiked ? (
            <svg
              clipRule="evenodd"
              width={28}
              height={28}
              onClick={handleLike}
              className="invert"
              fillRule="evenodd"
              strokeLinejoin="round"
              strokeMiterlimit="2"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="m7.234 3.004c-2.652 0-5.234 1.829-5.234 5.177 0 3.725 4.345 7.727 9.303 12.54.194.189.446.283.697.283s.503-.094.697-.283c4.977-4.831 9.303-8.814 9.303-12.54 0-3.353-2.58-5.168-5.229-5.168-1.836 0-3.646.866-4.771 2.554-1.13-1.696-2.935-2.563-4.766-2.563zm0 1.5c1.99.001 3.202 1.353 4.155 2.7.14.198.368.316.611.317.243 0 .471-.117.612-.314.955-1.339 2.19-2.694 4.159-2.694 1.796 0 3.729 1.148 3.729 3.668 0 2.671-2.881 5.673-8.5 11.127-5.454-5.285-8.5-8.389-8.5-11.127 0-1.125.389-2.069 1.124-2.727.673-.604 1.625-.95 2.61-.95z"
                fillRule="nonzero"
              />
            </svg>
          ) : (
            <svg
              clipRule="evenodd"
              width={28}
              height={28}
              onClick={handleLike}
              className="invert"
              fillRule="evenodd"
              strokeLinejoin="round"
              strokeMiterlimit="2"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="m12 5.72c-2.624-4.517-10-3.198-10 2.461 0 3.725 4.345 7.727 9.303 12.54.194.189.446.283.697.283s.503-.094.697-.283c4.977-4.831 9.303-8.814 9.303-12.54 0-5.678-7.396-6.944-10-2.461z"
                fillRule="nonzero"
              />
            </svg>
          )}
          {clampNumber(likeCount)}
        </span>
      </div>
    </a>
  );
};

export default NoteCard;
