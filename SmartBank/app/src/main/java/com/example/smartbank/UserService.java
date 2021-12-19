package com.example.smartbank;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    @POST("/api/v1/login/")
    Call<LoginResponse> loginuser(@Body LoginRequest loginRequest);

    @POST("/api/v1/signup/")
    Call<RegisterResponse> registerUsers(@Body RegisterRequest registerRequest);

    @GET("/api/v1/bank/mumbai")
    Call<List<PlaceModel>> getAll();

    @POST("/api/v1/bank/mumbai/nearest")
    Call<List<PlaceModel>> getNearest(@Body NearestRequest nearestRequest);

}
