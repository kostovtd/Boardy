package com.kostovtd.boardy.web

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    private const val FIREBASE_CLOUD_FUNCTIONS_DEV_URL = "https://us-central1-boardy-dev.cloudfunctions.net/"

    //TODO Add environment
    private fun getFirebaseClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(FIREBASE_CLOUD_FUNCTIONS_DEV_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val firebaseAPI: FirebaseAPI = getFirebaseClient().create(FirebaseAPI::class.java)
}