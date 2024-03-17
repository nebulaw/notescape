"use client";

import LoadingScreen from "@/app/components/loading-screen";
import { UserType } from "@/types/auth";
import React, { useEffect, useState } from "react";
import Image from "next/image";
import { instance as axios } from "@/app/lib/data";
import { useAuth } from "@/hooks/use-auth";
import { useRouter } from "next/navigation";
import { Bounce,  Slide, ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

export default function Page({}) {
  const router = useRouter();
  const { user, status, updateUser } = useAuth();
  const [ userRequest, setUserRequest ] = useState<UserType>({
    id: user?.user.id || -1,
    fullName: "",
    username: "",
    about: "",
    email: user?.user.email || "",
    imageUrl: "",
  });

  useEffect(() => {
    setUserRequest((prev) => ({
      ...prev,
      id: user ? user.user.id : -1,
      email: user ? user.user.email : "",
    }));
  }, [])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setUserRequest((prev) => ({
      ...prev,
      [e.target.name]: e.target.value
    }))
  };

  const handleUpdate = () => {
    // TODO: validate user request
    if (userRequest) {
      axios.put(`/users/update?userId=${user?.user.id}`, userRequest)
        .then((res) => {
          if (res.status == 200 && res.data.data) {
            updateUser({
              token: user ? user.token : "",
              user: res.data.data
            });
            toast.info('Successfully updated');
            setTimeout(() => {
              router.push(`/${res.data.data.username}`);
            }, 1200);
          } else if (res.status == 409) {
          }
          console.log(res.data);
        }, (error) => {
            toast.error(error.response.data.error)
        });
    }
  }

  if (!user && status === "authenticated") {
    return <LoadingScreen />
  }

  return <>
    <div className="flex w-[640px] h-auto items-center">
    <div className="flex gap-3 px-8 py-6 flex-col w-full h-auto bg-white/5 rounded-xl">
      <div className="flex w-full justify-between">
        <span className="flex flex-col gap-3">
          <span className="flex flex-col justify-start items-start gap-2 w-full">
            <span className="font-semibold pl-1"> Username </span>
            <input
              name={"username"}
              placeholder={`${user?.user.username || "Username"}`}
              onChange={handleChange}
            />
          </span>
          <span className="flex flex-col justify-start items-start gap-2 w-full">
            <span className="font-semibold pl-1"> Full Name </span>
            <input
              name={"fullName"}
              placeholder={`${user?.user.fullName || "Full Name"}`}
              onChange={handleChange}
            />
          </span>
        </span>
        <span className="flex flex-col justify-center items-center gap-2 w-full">
          {
            user && user.user && (
              <>
                <Image
                  src={user.user.imageUrl || "https://i.imgur.com/7wQ61EW.jpeg"}
                  alt="spinner"
                  width={120}
                  height={0}
                  placeholder={"blur"}
                  blurDataURL={user.user.imageUrl || "https://i.imgur.com/7wQ61EW.jpeg"}
                  style={{ height: "auto", width: "auto" }}
                />
                <span className="flex items-center gap-2">
                  <a> Upload </a>
                  <a> Delete </a>
                </span>
              </>
            )
          }
        </span>
      </div>
      <span className="flex flex-col justify-start items-start gap-2 w-full">
        <span className="font-semibold pl-1"> About </span>
        <textarea
          name={"about"}
          placeholder={`${user?.user.about || "About"}`}
          onChange={handleChange}
          className={"h-24 w-full"}
        />
      </span>
      <button className="w-full my-2" onClick={handleUpdate}>
        Save update
      </button>
    </div>
  </div>
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
  </>;
}


