package com.lab04.api.config;

public class ApiConfig {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final int DEFAULT_TIMEOUT_MS = 10_000;

    // Endpoints
    public static final String USERS_ENDPOINT     = "/users";
    public static final String POSTS_ENDPOINT     = "/posts";
    public static final String COMMENTS_ENDPOINT  = "/comments";
    public static final String TODOS_ENDPOINT     = "/todos";
    public static final String ALBUMS_ENDPOINT    = "/albums";

    private ApiConfig() {}
}
