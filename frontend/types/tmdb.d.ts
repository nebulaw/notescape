
export type MovieType = {
    adult: boolean,
    backdrop_path: string,
    genre_ids: number[],
    id: number,
    original_title: string,
    overview: string,
    popularity: number,
    poster_path: string,
    release_date: string,
    title: string,
    vote_average: number,
    vote_count: number,
};

export type MoviePageType = {
    page: number,
    results: MovieType[],
    total_pages: number,
    total_results: number,
};
