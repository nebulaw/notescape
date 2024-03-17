"use client";

import { useEffect, useState } from "react";


const useLocalStorage = (key: string, defaultValue: any) => {
  const store = typeof window !== "undefined" ? localStorage : null;
  const [ value, setValue ] = useState(() => {
    let currentValue;

    try {
      currentValue = JSON.parse(
        store?.getItem(key) || String(defaultValue)
      );
    } catch (error) {
      currentValue = defaultValue;
    }

    return currentValue;
  });

  useEffect(() => {
    store?.setItem(key, JSON.stringify(value));
  }, [ value, key ]);

  return [ value, setValue ];
}

export default useLocalStorage;


