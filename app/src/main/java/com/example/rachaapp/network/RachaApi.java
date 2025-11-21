package com.rachapp.app.network;

import com.example.rachaapp.model.AmigoResponse;
import com.example.rachaapp.model.Racha;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RachaApi {

    // GET ALL rachas
    @GET("rachas")
    Call<List<Racha>> getAllRachas();

    // GET friends of a user
    @GET("amigos/{idUsuario}")
    Call<List<AmigoResponse>> getAmigos(@Path("idUsuario") Long idUsuario);
}
