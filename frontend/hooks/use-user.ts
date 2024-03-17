import { useContext, useEffect } from "react";
import { AuthContext } from "@/contexts/auth-context";
import { AuthUserType, UserType } from "@/types/auth";
import useCookie from "./use-cookie";
import { setAuthToken } from "@/app/lib/data";

export const useUser = () => {
  const { user, setUser, status, setStatus } = useContext(AuthContext);
  const { setCookie, removeCookie } = useCookie();

  const addUser = (user: AuthUserType) => {
    setUser(user);
    setCookie("user", JSON.stringify(user));
    setStatus("authenticated");
    setAuthToken(user.token);
  };

  const removeUser = () => {
    setUser(null);
    setStatus("unauthenticated");
    removeCookie("user");
    setAuthToken(null);
  };

  useEffect(() => {
    if (user) {
      setStatus("authenticated");
    } else {
      setStatus("unauthenticated");
    }
  }, [user, setUser])

  return {
    status,
    setStatus,
    user,
    setUser,
    addUser,
    removeUser,
  };
};


