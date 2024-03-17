import React, { ButtonHTMLAttributes } from "react";
import { twMerge } from "tailwind-merge";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  active: boolean;
  values: string | string[];
  loading: boolean;
}

const Button: React.FC<ButtonProps> = ({
  active,
  values,
  loading,
  className,
  ...props
}: ButtonProps) => {

  return (
    <button
      className={twMerge(
        !active && "border-gray-800 hover:bg-white bg-white text-black",
        className
      )}
      {...props}
    >
      {loading ? (
        <img alt="spinner" src="/spinner.svg" className="w-6 mx-auto" />
      ) : (
        <>
          {typeof values === "string" ? (
            `${values}`
          ) : (
            active ? `${values[0]}` : `${values[1]}`
          )}
        </>
      )}
    </button>
  );
};

export default Button;

