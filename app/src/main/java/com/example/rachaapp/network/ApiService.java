package com.example.rachaapp.network;

import com.example.rachaapp.model.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Register
    @POST("api/usuarios")
    Call<Usuario> criarUsuario(@Body Usuario usuario);

    // Login
    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    // NEW: Get Friends List
    @GET("api/amigos/{idUsuario}")
    Call<List<Usuario>> getAmigos(@Path("idUsuario") Long idUsuario);
}