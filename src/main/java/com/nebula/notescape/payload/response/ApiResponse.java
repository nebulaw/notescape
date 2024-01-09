package com.nebula.notescape.payload.response;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse extends HashMap<String, Object> {

    protected ApiResponse(Map<String, Object> map) {
        super(map);
    }

    public static ApiResponseBuilder builder() {
        return new ApiResponseBuilder();
    }

    public static class ApiResponseBuilder {

        private Map<String, Object> map;

        protected ApiResponseBuilder() {
            map = new HashMap<>();
        }

        public ApiResponseBuilder put(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public ApiResponseBuilder data(Object value) {
            map.put("data", value);
            return this;
        }

        public ApiResponseBuilder error(Object error) {
            map.put("error", error);
            return this;
        }

        public ApiResponseBuilder status(Integer status) {
            map.put("status", status);
            return this;
        }

        public ApiResponseBuilder status(HttpStatus status) {
            return status(status.value());
        }

        public ApiResponse build() {
            return new ApiResponse(map);
        }
    }

}
