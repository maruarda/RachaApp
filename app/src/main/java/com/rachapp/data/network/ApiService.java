package com.rachapp.data.network;

import com.rachapp.data.model.BalanceDTO;
import com.rachapp.data.model.Devedor;
import com.rachapp.data.model.ItemCreationDTO;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.Racha;
import com.rachapp.data.model.ResumoDTO;
import com.rachapp.data.model.Usuario;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT; // New Import
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/usuarios")
    Call<Usuario> criarUsuario(@Body Usuario usuario);

    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    @GET("api/usuarios/{id}")
    Call<Usuario> getUsuarioById(@Path("id") Long id);

    @GET("api/usuarios/search")
    Call<List<Usuario>> searchUsuarios(@Query("query") String query);

    @GET("api/amigos/{idUsuario}")
    Call<List<Usuario>> getAmigos(@Path("idUsuario") Long idUsuario);

    @POST("api/amigos/{idUsuario}/{idAmigo}")
    Call<ResponseBody> addFriend(@Path("idUsuario") Long idUsuario, @Path("idAmigo") Long idAmigo);

    @GET("api/resumo/{userId}")
    Call<ResumoDTO> getResumoFinanceiro(@Path("userId") Long userId);

    @POST("api/rachas")
    Call<Racha> criarRacha(@Body Racha racha);

    @GET("api/rachas/{id}")
    Call<Racha> getRachaDetalhes(@Path("id") Long idRacha);

    @GET("api/rachas/user/{userId}")
    Call<List<Racha>> getMeusRachas(@Path("userId") Long userId);

    @DELETE("api/rachas/{id}")
    Call<Void> deleteRacha(@Path("id") Long idRacha);

    @PATCH("api/rachas/{id}/fechar")
    Call<Racha> fecharRacha(@Path("id") Long idRacha);

    @POST("api/itens")
    Call<ItemRacha> adicionarItem(@Body ItemCreationDTO dto);

    @PUT("api/itens/{id}")
    Call<ItemRacha> updateItem(@Path("id") Long itemId, @Body ItemCreationDTO dto);

     @DELETE("api/itens/{id}")
    Call<Void> deleteItem(@Path("id") Long itemId);

    @GET("api/devedores/item/{id}")
    Call<List<Devedor>> getDevedoresByItem(@Path("id") Long idItem);

    @GET("api/rachas/{id}/balances")
    Call<List<BalanceDTO>> getRachaBalances(@Path("id") Long rachaId);
}