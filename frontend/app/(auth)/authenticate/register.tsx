import React, { useState } from "react";
import Image from "next/image";
import type { RegisterType } from "@/types/auth";
import { useAuth } from "@/hooks/use-auth";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

function validateRegisterRequest({
  email,
  fullName,
  username,
  password,
} : {
  email: string,
  fullName: string,
  username: string,
  password: string
}) {
  return email?.length >=6 &&
    fullName?.length > 0 &&
    username?.length >=6 &&
    password?.length >= 8
}

export default function Register() {
  const [registerRequest, setRegisterRequest] = useState<RegisterType>({
    email: "",
    username: "",
    fullName: "",
    password: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const { register } = useAuth();
  const router = useRouter();

  console.log(registerRequest);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRegisterRequest(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  const handleRegister = () => {
    console.log("register clicked");
    setLoading(true);
    if (validateRegisterRequest(registerRequest)) {
      register(registerRequest)
        .then((data) => {
          if (data?.user) {
            setTimeout(() => {
              router.push("/home")
            }, 1000);
          } else {
            toast(data?.error);
          }
        })
        .catch((err) => {
          toast.error(err);
          toast.error(err.response?.data.error);
        });
      setError(false);
    } else {
      setError(true);
    }
    setLoading(false);
  }

  return (
    <div className="flex flex-col bg-transparent items-center justify-center w-[640px]">
      <span className="w-full text-center text-3xl font-bold mb-4"> Register </span>
      {
        !error ? (<></>) : (<span className="text-red-300">Invalid field/fields</span>)
      }
      <div
        className="py-4 flex flex-col w-full gap-3 items-center justify-center"
      >
        <input
          className="w-1/2"
          name="email"
          type="text"
          placeholder="Email"
          onChange={handleChange}
        />
        <input
          className="w-1/2"
          name="username"
          type="text"
          placeholder="Username"
          onChange={handleChange}
        />
        <input
          className="w-1/2"
          name="fullName"
          type="text"
          placeholder="Full Name"
          onChange={handleChange}
        />
        <input
          className="w-1/2 mb-4"
          name="password"
          type="password"
          placeholder="Password"
          onChange={handleChange}
        />
        {
          loading ? (
            <button className="w-1/2 flex justify-center">
              <Image
                src="/spinner.svg"
                alt="spinner"
                width={24}
                height={24}
              />
            </button>
          ) : (
            <button className="w-1/2" onClick={handleRegister}> Register </button>
          )
        }
      </div>
    </div>
  );
}

