import React, { useState } from "react";
import Image from "next/image";
import { LoginType } from "@/types/auth";
import { useAuth } from "@/hooks/use-auth";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

function validateLoginRequest(creds: LoginType | null) {
  if (creds) {
    if (creds?.email && creds?.password) {
      if (creds.email.length >= 6 && creds.password.length >= 8) {
        return true;
      }
    }
  }
  return false;
}

export default function Login() {
  const [ loginRequest, setLoginRequest ] = useState<LoginType>({
    email: "",
    password: ""
  });
  const [ loading, setLoading ] = useState(false);
  const [ error, setError ] = useState(false);
  const { status, login } = useAuth();
  const router = useRouter();

  console.log(loginRequest);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginRequest(prev => (prev && {
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  const handleLogin = () => {
    console.log("login clicked");
    setLoading(true);
    if (validateLoginRequest(loginRequest)) {
      login(loginRequest)
        .then((data) => {
          if (data?.user) {
            setTimeout(() => {
              router.push("/home");
            }, 1000);
          } else {
            toast(data?.error);
          }
        })
        .catch((err) => {
          toast.error(err);
          toast.error(err.response?.data.error);
        })
      setError(false);
    } else {
      setError(true);
    }
    setLoading(false);
  }

  if (status === "authenticated") {
    router.push("/home");
  }

  return <>
    <div className="flex flex-col bg-transparent items-center justify-center w-[640px]">
      <span className="w-full text-center text-3xl font-bold mb-4"> Login </span>
      {
        !error ? (<></>) : (<span className="text-red-300">Invalid credentials</span>)
      }
      <div
        className="py-4 flex flex-col w-full gap-3 items-center justify-center"
      >
        <form
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
            name="password"
            type="password"
            placeholder="password"
            onChange={handleChange}
            autoComplete="on"
          />
        </form>
        <a className="text-sm text-gray-500 cursor-pointer mb-2">Forgot password?</a>
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
            <button className="w-1/2" onClick={handleLogin}> Login </button>
          )
        }
      </div>
    </div>
  </>

}

