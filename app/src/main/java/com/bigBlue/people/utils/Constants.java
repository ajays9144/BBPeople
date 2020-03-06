package com.bigBlue.people.utils;

/**
 * The interface Constants.
 */
public interface Constants {
    /**
     * The constant MAX_IMAGE_UPLOAD_COUNT.
     */
    int MAX_IMAGE_UPLOAD_COUNT = 4;

    /**
     * The constant HEAD_CONTAINER.
     */
    int HEAD_CONTAINER = 1;
    /**
     * The constant IMAGE_CONTAINER.
     */
    int IMAGE_CONTAINER = 4;
    /**
     * The constant ABOUT.
     */
    String ABOUT = "about";

    /**
     * The constant BASE_URL.
     */
    String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * The interface Firebase.
     */
    interface FIREBASE {
        /**
         * The constant USERS.
         */
        String USERS = "users";
        /**
         * The constant PROFILE.
         */
        String PROFILE = "profile";
        /**
         * The constant IMAGES.
         */
        String IMAGES = "image";
        /**
         * The constant SERVICES.
         */
        String SERVICES = "services";
    }

    /**
     * The interface Adapter.
     */
    interface ADAPTER {
        /**
         * The constant COMMENT.
         */
        int COMMENT = 1;
        /**
         * The constant COMMENT_AND_BODY.
         */
        int COMMENT_AND_BODY = 2;
        /**
         * The constant POSTS.
         */
        int POSTS = 3;
    }
}
