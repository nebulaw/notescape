import React from "react";
import "../styles/globals.css";
import { Inter } from "next/font/google";
import { twMerge } from "tailwind-merge";
import { Providers } from "./providers";
import 'react-toastify/dist/ReactToastify.css'

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Notescape",
  description: "Take notes on your favourite movies and share",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {

  return (
    <html lang="en">
      <body
        className={twMerge(
          inter.className,
          "flex justify-center overflow-y-scroll",
        )}
      >
        <Providers>
          {children}
        </Providers>
      </body>
    </html>
  );
}
