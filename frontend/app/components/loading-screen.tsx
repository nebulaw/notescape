import React from "react";
import Image from "next/image";
import { twMerge } from "tailwind-merge";

interface Props {
  fullScreen?: boolean;
}

export default function LoadingScreen({ fullScreen }: Props) {
  return(
    <div className={twMerge(
      "flex flex-col justify-center items-center absolute text-3xl overflow-y-scroll",
      fullScreen && "justify-between absolute py-8 z-30 bg-[#070707] min-h-screen w-full"
    )}>
      {
        fullScreen && <span></span>
      }
      <Image
        src={"/spinner.svg"}
        alt="spinner"
        height={44}
        width={44}
        className="invert"
        placeholder="blur"
        blurDataURL={"/spinner.svg"}
      />
      {
        fullScreen &&
        <Image
          src={"/logo.svg"}
          alt="logo"
          width={32}
          height={32}
          className="invert hover:cursor-pointer"
          draggable={false}
        />
      }
    </div>
  );
}

