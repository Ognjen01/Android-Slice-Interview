package com.slicelife.tastyrecipes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NetworkService {
    suspend fun getRecipes(): ItemList {
        val client = OkHttpClient();

        val cacheControl = CacheControl.Builder()
            .minFresh(10, TimeUnit.SECONDS)
            .build()

        val headers = Headers.Builder()
            .add("X-RapidAPI-Key", "a356eca938mshecd748d7e4377d7p18b321jsn5f7a486ec28a")
            .add("X-RapidAPI-Host", "tasty.p.rapidapi.com")
            .build()

        val request = Request.Builder()
            .url("https://tasty.p.rapidapi.com/recipes/list?from=0&size=20&tags=under_30_minutes")
            .cacheControl(cacheControl)
            .headers(headers)
            .build()

        return suspendCoroutine{ it ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

                override fun onResponse(call: Call, response: Response) {
                    val itemList = Json { ignoreUnknownKeys = true }
                        .decodeFromString<ItemList>(response.body!!.string())
                    it.resume(itemList)
                }
            })
        }
    }
}