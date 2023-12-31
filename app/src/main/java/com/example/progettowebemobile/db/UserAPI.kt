package com.example.db_connection
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.Date
import retrofit2.Response


interface UserAPI {
    @POST("postSelect/")
    @FormUrlEncoded
    fun login(@Field("query") query : String): Call <JsonObject>


    @POST("postUpdate/")
    @FormUrlEncoded
    fun modifica(@Field("query") query: String): Call <JsonObject>

    @GET
    fun getAvatar(@Url url: String) : Call <ResponseBody>

    @POST("postInsert/")
    @FormUrlEncoded
    fun insert(@Field("query") query: String): Call <JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun update(@Field("query") query: String): Call <JsonObject>

    @POST("postRemove/")
    @FormUrlEncoded
    fun remove(@Field("query") query: String): Call <JsonObject>
}