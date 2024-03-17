import { MovieType } from "@/types/tmdb";
import { useEffect, useState } from "react";

const imageBaseUrl = "https://image.tmdb.org/t/p/w500";

const useMoviePoster = (item: MovieType) => {
  const [imagePath, setImagePath] = useState("");
  const [status, setStatus] = useState<"loading" | "loaded" | "error">("loading");

  useEffect(() => {
    if (item) {
      setImagePath(`${imageBaseUrl}${item.poster_path || item.backdrop_path}`);
      setStatus("loaded");
    } else {
      setStatus("error");
    }
  }, [item]);

  return { image: imagePath, imageLoading: status };
};

const useMovie = (id: number) => {
  const [data, setData] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("none");

  useEffect(() => {
    fetch(`https://api.themoviedb.org/3/movie/${id}?language=en-US`, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMzAyYzY2MzIwNDc0YzBhMTg0NWI1MjEwMDMxZjQzMSIsInN1YiI6IjY1OTMxNmM4NGY5YTk5NzY1ZDc3MWExOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.quX_QmPQPjiA2RTlbKjduL70hlBGWSU97o3toMdfw2Q`,
        accept: "application/json",
      },
    })
      .then((res) => res.json())
      .then(
        (data) => {
          setData(data);
          setIsLoading(false);
          setError("none");
        },
        (error) => {
          setIsLoading(false);
          setError(error);
        },
      );
  }, []);

  return {
    item: data,
    isLoading,
    error: error,
  };
};

export { useMovie, useMoviePoster, imageBaseUrl };
