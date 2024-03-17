import { useUser } from "./use-user";
import { API_BASE_URL } from "@/app/lib/data";
import axios from "axios";
import type { AuthUserType, LoginType, RegisterType } from "@/types/auth";
import useCookie from "./use-cookie";

const API_URL = API_BASE_URL;

export const useAuth = () => {
  const { status, user, addUser, removeUser } = useUser();

  const { getCookie } = useCookie();

  const refresh = () => {
    let existingUser = null;
    const getFromCookie = async () => (existingUser = getCookie("user"));
    getFromCookie();

    if (existingUser) {
      try {
        addUser(JSON.parse(existingUser));
      } catch (e) {
        console.log(e);
      }
    }
  };

  const register = async (creds: RegisterType) => {
    return await axios
      .post(`${API_URL}/auth/register`, creds)
      .then((res) => res.data)
      .then((res) => {
        if (res?.data && res.data?.user && res.data?.token) {
          addUser(res.data);
        }
        return res
      });
  };

  const login = async (creds: LoginType) => {
    return await axios
      .post(`${API_URL}/auth/login`, creds)
      .then((res) => res.data)
      .then((res) => {
        if (res?.data && res.data?.user && res.data?.token) {
          addUser(res.data);
        }
        return res;
      });
  };

  const updateUser = (user: AuthUserType) => {
    removeUser();
    addUser(user);
//    refresh();
  }

  const logout = () => {
    removeUser();
  }

  return {
    status,
    user,
    login,
    register,
    updateUser,
    logout,
    refresh
  };
}


