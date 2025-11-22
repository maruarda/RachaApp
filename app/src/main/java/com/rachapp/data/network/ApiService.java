package com.rachapp.data.network;

import com.rachapp.data.model.Usuario;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/usuarios")
    Call<Usuario> criarUsuario(@Body Usuario usuario);

    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    @GET("api/amigos/{idUsuario}")
    Call<List<Usuario>> getAmigos(@Path("idUsuario") Long idUsuario);

    @GET("api/usuarios/search")
    Call<List<Usuario>> searchUsuarios(@Query("query") String query);

    @POST("api/amigos/{idUsuario}/{idAmigo}")
    Call<ResponseBody> addFriend(@Path("idUsuario") Long idUsuario, @Path("idAmigo") Long idAmigo);
}