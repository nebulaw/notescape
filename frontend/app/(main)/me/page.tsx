"use client"

import LoadingScreen from "@/app/components/loading-screen";
import { useUser } from "@/hooks/use-user";
import Link from "next/link";
import { redirect } from "next/navigation";

export default function Me() {
  const { user, status } = useUser();

  if (!user) {
    if (status === "authenticated") {
      return <LoadingScreen fullScreen={true} />
    } else {
      return (<main className="flex gap-3 font-semibold min-h-screen flex-col items-center justify-center py-24 px-10">
        You need to login
        <Link
          href="/authenticate"
        >
          <span
            className="text-blue-500"
          >
            Go to Login Page
          </span>
        </Link>
      </main>
      );
    }
  } else {
    redirect(`/${user.user.username}`)
  }
}


