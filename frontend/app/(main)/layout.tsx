import Navbar from "@/app/components/navbar";
import React from "react";
import { Slide, ToastContainer } from "react-toastify";

export default function Layout({ children }: { children: React.ReactNode }) {
  return <>
    <Navbar />
    {children}
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

