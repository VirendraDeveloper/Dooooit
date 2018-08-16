package cl.activaresearch.android_app.Dooit.servercommunication;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import cl.activaresearch.android_app.Dooit.models.AccountBean;
import cl.activaresearch.android_app.Dooit.models.BankBean;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.models.PaymentDetails;
import cl.activaresearch.android_app.Dooit.models.RegionBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.models.UserBean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public interface ApiInterface {
    @Headers({
            "Content-Type:application/json"
    })

    @POST("/login")
    Call<ResponseBody> login(@Body HashMap<String, String> body);

    @POST("/shoppers")
    Call<ResponseBody> register(@Body HashMap<String, String> body);

    @POST("/forgot")
    Call<ResponseBody> forgot(@Body HashMap<String, String> body);

    @POST("/reset")
    Call<ResponseBody> reset(@Body HashMap<String, String> body);

    @POST("/fblogin")
    Call<ResponseBody> fbLogin(@Body HashMap<String, String> body);

    @GET("/shopper")
    Call<UserBean> details(@Header("Authorization") String token);

    @POST("/shopper")
    Call<ResponseBody> update(@Header("Authorization") String token, @Body HashMap<String, String> body);

    @GET("/cities")
    Call<List<RegionBean>> city(@Header("Authorization") String token);

    @GET("/tasks/categories")
    Call<List<CategoryBean>> category(@Header("Authorization") String token);

    @GET("/search")
    Call<List<TaskBean>> task(@Header("Authorization") String token);

    @GET("/geo")
    Call<List<TaskBean>> geo(@Header("Authorization") String token, @Query("lat") String lat, @Query("lon") String lon, @Query("distance") String distance, @Query("categories") String categories);

    @GET("/geo")
    Call<ResponseBody> geo1(@Header("Authorization") String token, @Query("lat") String lat, @Query("lon") String lon, @Query("distance") String distance, @Query("categories") String categories);

    @POST("tasks/claim/{id}")
    Call<ResponseBody> taskClaim(@Header("Authorization") String token, @Path("id") String id);

    @GET("/tasks/current")
    Call<List<TaskBean>> currentTask(@Header("Authorization") String token);

    @GET("/tasks/{id}")
    Call<TaskBean> taskDetails(@Header("Authorization") String token, @Path("id") String id);

    @GET("/tasks/history")
    Call<List<TaskBean>> previousTask(@Header("Authorization") String token);


    @GET("/tasks/{id}/questions")
    Call<ResponseBody> questions(@Header("Authorization") String token, @Path("id") String id);

    @GET("/shopperQuestions")
    Call<ResponseBody> shopperQuestions(@Header("Authorization") String token);

    @POST("/shopperQuestion/{qid}")
    Call<ResponseBody> shopperAnswer(@Header("Authorization") String token, @Path("qid") String qid, @Body JsonObject body);

    @POST("/tasks/{id}/questions/{qid}")
    Call<ResponseBody> answer(@Header("Authorization") String token, @Path("id") String id, @Path("qid") String qid, @Body JsonObject body);

    @POST("/tasks/complete/{id}")
    Call<ResponseBody> complete(@Header("Authorization") String token, @Path("id") String id);

    @POST("/balance/cashout")
    Call<ResponseBody> cashout(@Header("Authorization") String token);

    @GET("/balance")
    Call<ResponseBody> currentBalance(@Header("Authorization") String token);

    @GET("/balance/history")
    Call<ResponseBody> payHistory(@Header("Authorization") String token);

    @GET("/balance/details/{id}")
    Call<PaymentDetails> payDetails(@Header("Authorization") String token, @Path("id") String id);

    @POST("/device")
    Call<ResponseBody> register(@Header("Authorization") String token, @Body JsonObject body);

    @POST("/device/toggle")
    Call<ResponseBody> toggle(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @GET("/bank/index")
    Call<List<BankBean>> banks(@Header("Authorization") String token);

    @GET("/bank/account")
    Call<AccountBean> account(@Header("Authorization") String token);

    @POST("/bank/account")
    Call<ResponseBody> addAccount(@Header("Authorization") String token, @Body JsonObject jsonObject);
}
