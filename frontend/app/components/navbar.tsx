"use client";

import { useAuth } from "@/hooks/use-auth";
import { useRouter } from "next/navigation";
import React from "react";
import { twMerge } from "tailwind-merge";
import Image from "next/image";
import Link from "next/link";

export default function Navbar() {
  const { user, logout } = useAuth()
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push("/authenticate");
  }

  return (
    <nav
      className={twMerge(`
        flex fixed backdrop-blur-xl items-center w-full
        bg-transparent justify-around
        transition-all ease-in h-16
      `,
        ""
      )}
    >
      <Image
        src={"/logo.svg"}
        alt="logo"
        width={32}
        height={32}
        priority
        className="invert hover:cursor-pointer"
        draggable={false}
        onClick={() => router.push("/home")}
      />
      <ul
        className={"font-semibold transition-all ease-in flex flex-row gap-20"}
      >
        <li>
          <a href="/home">Home</a>
        </li>
        <li>
          <a href="/search">Search</a>
        </li>
        <li>
          <a href="/notes">Notes</a>
        </li>
        <li>
          <a href="/movies">Movies</a>
        </li>
        <li>
          <Link href={`/${user ? user.user.username : "me"}`}>
            Profile
          </Link>
        </li>
      </ul>
      <span>
        <button onClick={handleLogout}>
          Logout
        </button>
      </span>
    </nav>
  );
}
