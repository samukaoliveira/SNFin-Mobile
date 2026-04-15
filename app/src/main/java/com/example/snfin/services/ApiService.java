package com.example.snfin.services;

import com.example.snfin.models.LoginResponse;
import com.example.snfin.models.UsuarioLoginRequest;
import com.example.snfin.models.escala.EscalaDetalhada;
import com.example.snfin.models.escala.EscalaNotificacao;
import com.example.snfin.models.escala.EscalaSimples;
import com.example.snfin.models.lancamento.DashboardResponse;
import com.example.snfin.models.mensalidade.Mensalidade;
import com.example.snfin.models.musica.Musica;
import com.example.snfin.models.musica.MusicaDetalhada;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login/")
    Call<LoginResponse> login(@Body UsuarioLoginRequest request);

    @GET("home/")
    Call<DashboardResponse> getHome();

    @GET("escalas/")
    Call<List<EscalaSimples>> getListaEscalas();

    @GET("minhas_escalas/")
    Call<List<EscalaSimples>> getMinhasEscalas();

    @GET("musicas/")
    Call<List<Musica>> getMusicas();

    @GET("musicas/{id}")
    Call<MusicaDetalhada> getDetalheMusica(@Path("id") int id);

    @GET("escalas/{id}")
    Call<EscalaDetalhada> getDetalheEscala(@Path("id") int id);

    @GET("atualizadas")
    Call<List<EscalaNotificacao>> getAtualizadas();

    @GET("recentes")
    Call<List<EscalaNotificacao>> getRecentes();

    @GET("mensalidades")
    Call<List<Mensalidade>> getMensalidades();
}