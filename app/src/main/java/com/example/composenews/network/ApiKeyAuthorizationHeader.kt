package com.example.composenews.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyAuthorizationHeader @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request =  request.newBuilder().addHeader("X-Api-Key", "9f7a97fb43654b9eb1b1ded245c693f1").build()
        return chain.proceed(request)
    }
}