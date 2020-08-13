package com.machwusa.graphikal.util;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;

public class AplClient {

    public static final String BASE_URL = "https://graphql-android-project.herokuapp.com/graphql";
    public static final String TAG = AplClient.class.getSimpleName();

    private static ApolloClient mApolloClient;


    public static ApolloClient getApolloClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        mApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return mApolloClient;
    }
}
