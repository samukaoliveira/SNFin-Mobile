package com.example.snfin.services;

import com.example.snfin.models.LoginResponse;
import com.example.snfin.models.UsuarioLoginRequest;
import com.example.snfin.models.cartao.CartaoDetalheDTO;
import com.example.snfin.models.cartao.CartaoDetalheResponse;
import com.example.snfin.models.escala.EscalaDetalhada;
import com.example.snfin.models.escala.EscalaNotificacao;
import com.example.snfin.models.escala.EscalaSimples;
import com.example.snfin.models.lancamento.DashboardResponse;
import com.example.snfin.models.lancamento.LancamentoDTO;
import com.example.snfin.models.mensalidade.Mensalidade;
import com.example.snfin.models.musica.Musica;
import com.example.snfin.models.musica.MusicaDetalhada;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login/")
    Call<LoginResponse> login(@Body UsuarioLoginRequest request);

    @GET("home/")
    Call<DashboardResponse> getHome(
            @Query("mes") int mes,
            @Query("ano") int ano
    );

    @POST("lancamentos/")
    Call<Void> criarLancamento(
            @Header("Authorization") String token,
            @Body LancamentoDTO request
    );

    @PUT("lancamentos/{id}/")
    Call<Void> atualizarLancamento(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body LancamentoDTO request
    );

    @DELETE("lancamentos/{id}/delete/")
    Call<Void> deletarLancamento(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("cartoes/{id}/")
    Call<CartaoDetalheResponse> getCartao(
            @Header("Authorization") String token,
            @Path("id") int id
    );


}