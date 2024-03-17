"use client";

import React, { useState } from "react"
import Login from "./login";
import Register from "./register";
import { useUser } from "@/hooks/use-user";
import { redirect } from "next/navigation";
import { Slide, ToastContainer } from "react-toastify";


export default function Page() {
  const { status } = useUser();
  const [ isLogin, setIsLogin ] = useState(true);

  const handleClick = () => {
    setIsLogin(!isLogin);
  }

  if (status === "authenticated") {
    redirect("/home");
  }

  return <>
    <main
        className={`
          flex bg-center bg-cover
          min-h-screen flex-col justify-center
          items-center py-24`}
      >
      <div className="flex flex-col items-center justify-center bg-transparent rounded-xl backdrop-blur-xl py-20">
        <div className="flex flex-col">
          {
            isLogin ? (
              <Login />
            ) : (
              <Register />
            )
          }
        </div>
        <span className="border-b border-gray-500 w-[400px] my-4"></span>
        {
          isLogin ? (
            <span className="text-md"> Don&apost have an account?
              <strong onClick={handleClick} className="cursor-pointer"> Register</strong>
            </span>
          ) : (
            <span className="text-md"> Already have an account?
              <strong onClick={handleClick} className="cursor-pointer"> Login</strong>
            </span>
          )
        }
      </div>
    </main>
    <ToastContainer
      position="bottom-center"
      autoClose={1200}
      hideProgressBar={true}
      newestOnTop={false}
      closeOnClick
      rtl={false}
      pauseOnHover={false}
      pauseOnFocusLoss={false}
      draggable
      theme="dark"
      transition={Slide}
      limit={3}
    />
  </>
}
