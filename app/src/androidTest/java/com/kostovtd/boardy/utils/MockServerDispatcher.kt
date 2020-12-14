package com.kostovtd.boardy.utils

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Created by tosheto on 22.11.20.
 */
class MockServerDispatcher {

//    internal inner class UserDispatcher() : Dispatcher() {
//        override fun dispatch(request: RecordedRequest): MockResponse {
//            return when (request.method) {
//                "GET" -> {
//                    val requestPath = request.path ?: ""
//                    when (requestPath.substring(requestPath.lastIndexOf('/'), requestPath.length)) {
//                        "/Referrals" -> {
//                            if (emptyResponse == true) {
//                                MockResponse().setResponseCode(SUCCESS).setBody(readJSONFromAsset("emptyItemsListResponse.json"))
//                            } else {
//                                MockResponse().setResponseCode(SUCCESS).setBody(readJSONFromAsset("testSalesProcess", "referralsResponse.json"))
//                            }
//                        }
//                        else -> MockResponse().setResponseCode(NOT_FOUND)
//                    }
//                }
//                else -> {
//                    val requestPath = request.path ?: ""
//                    when (requestPath.substring(requestPath.lastIndexOf('/'), requestPath.length)) {
//                        "/PickAPlan" -> MockResponse().setResponseCode(SUCCESS).setBody(readJSONFromAsset("success"))
//                        else -> MockResponse().setResponseCode(NOT_FOUND)
//                    }
//                }
//            }
//        }
//    }

}