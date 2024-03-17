"use client";

import LoadingScreen from "@/app/components/loading-screen";
import { useUser } from "@/hooks/use-user";
import React from "react";

export default function Page({ }) {
  const { user } = useUser();

  if (!user) {
    return <LoadingScreen fullScreen={true} />
  }

  return (
    <main className={`
        flex font-semibold min-h-screen flex-col
        items-center justify-start py-24 px-10
    `}>
    </main>
  );
}

