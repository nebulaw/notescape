import { useCookies } from "next-client-cookies";

const useCookie = () => {
  const cookies = useCookies();
  const getCookie = (key: string | null) => {
    if (key) {
      try {
        return cookies.get(key);
      } catch (err) {
        return null;
      }
    } else {
      return null;
    }
  };
  const setCookie = (key: string, value: string) =>
    cookies.set(key, value, {
      expires: 5,
      sameSite: "None",
      secure: true,
    });
  const removeCookie = (key: string) => cookies.remove(key);
  return { setCookie, getCookie, removeCookie };
}

export default useCookie;

