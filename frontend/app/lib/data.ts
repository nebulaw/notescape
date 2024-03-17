import axios from "axios";

export const API_BASE_URL = "http://localhost:8080/api";

export const instance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Accept": "application/json",
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
  }
});

export const setAuthToken = (token: string | null) => {
  if (token) {
    instance.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    instance.defaults.headers.common["Authorization"] = '';
  }
};

export async function wait(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export async function getNotes(username: string) {
return [
  {
    id: 1,
    movieId: 12,
    author: "nebulaw",
    context: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. A cras semper auctor neque vitae tempus. Integer vitae justo eget magna fermentum. Faucibus pulvinar elementum integer enim. At volutpat diam ut venenatis tellus in metus. Erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Dignissim suspendisse in est ante. Egestas sed tempus urna et pharetra pharetra massa massa. Tortor condimentum lacinia quis vel eros donec ac odio. Tempor orci dapibus ultrices in iaculis nunc sed. A arcu cursus vitae congue. In nisl nisi scelerisque eu ultrices vitae. Tristique nulla aliquet enim tortor at auctor urna nunc id. Sit amet cursus sit amet dictum. Amet porttitor eget dolor morbi. Id diam maecenas ultricies mi eget. Nisi lacus sed viverra tellus in hac habitasse platea dictumst.",
    access: "PRIVATE",
    likeCount: 1242,
  },
  {
    id: 2,
    movieId: 12,
    author: "nebulaw",
    context: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. A cras semper auctor neque vitae tempus. Integer vitae justo eget magna fermentum. Faucibus pulvinar elementum integer enim. At volutpat diam ut venenatis tellus in metus. Erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Dignissim suspendisse in est ante. Egestas sed tempus urna et pharetra pharetra massa massa. Tortor condimentum lacinia quis vel eros donec ac odio. Tempor orci dapibus ultrices in iaculis nunc sed. A arcu cursus vitae congue. In nisl nisi scelerisque eu ultrices vitae. Tristique nulla aliquet enim tortor at auctor urna nunc id. Sit amet cursus sit amet dictum. Amet porttitor eget dolor morbi. Id diam maecenas ultricies mi eget. Nisi lacus sed viverra tellus in hac habitasse platea dictumst.",
    access: "PRIVATE",
    likeCount: 1242,
  },
  {
    id: 3,
    movieId: 12,
    author: "nebulaw",
    context: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. A cras semper auctor neque vitae tempus. Integer vitae justo eget magna fermentum. Faucibus pulvinar elementum integer enim. At volutpat diam ut venenatis tellus in metus. Erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Dignissim suspendisse in est ante. Egestas sed tempus urna et pharetra pharetra massa massa. Tortor condimentum lacinia quis vel eros donec ac odio. Tempor orci dapibus ultrices in iaculis nunc sed. A arcu cursus vitae congue. In nisl nisi scelerisque eu ultrices vitae. Tristique nulla aliquet enim tortor at auctor urna nunc id. Sit amet cursus sit amet dictum. Amet porttitor eget dolor morbi. Id diam maecenas ultricies mi eget. Nisi lacus sed viverra tellus in hac habitasse platea dictumst.",
    access: "PUBLIC",
    likeCount: 1242,
  },
  {
    id: 4,
    movieId: 12,
    author: "nebulaw",
    context: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. A cras semper auctor neque vitae tempus. Integer vitae justo eget magna fermentum. Faucibus pulvinar elementum integer enim. At volutpat diam ut venenatis tellus in metus. Erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Dignissim suspendisse in est ante. Egestas sed tempus urna et pharetra pharetra massa massa. Tortor condimentum lacinia quis vel eros donec ac odio. Tempor orci dapibus ultrices in iaculis nunc sed. A arcu cursus vitae congue. In nisl nisi scelerisque eu ultrices vitae. Tristique nulla aliquet enim tortor at auctor urna nunc id. Sit amet cursus sit amet dictum. Amet porttitor eget dolor morbi. Id diam maecenas ultricies mi eget. Nisi lacus sed viverra tellus in hac habitasse platea dictumst.",
    access: "PUBLIC",
    likeCount: 1242,
  },
  {
    id: 5,
    movieId: 12,
    author: "nebulaw",
    context: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. A cras semper auctor neque vitae tempus. Integer vitae justo eget magna fermentum. Faucibus pulvinar elementum integer enim. At volutpat diam ut venenatis tellus in metus. Erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Dignissim suspendisse in est ante. Egestas sed tempus urna et pharetra pharetra massa massa. Tortor condimentum lacinia quis vel eros donec ac odio. Tempor orci dapibus ultrices in iaculis nunc sed. A arcu cursus vitae congue. In nisl nisi scelerisque eu ultrices vitae. Tristique nulla aliquet enim tortor at auctor urna nunc id. Sit amet cursus sit amet dictum. Amet porttitor eget dolor morbi. Id diam maecenas ultricies mi eget. Nisi lacus sed viverra tellus in hac habitasse platea dictumst.",
    access: "PUBLIC",
    likeCount: 1242,
  },
]
}


