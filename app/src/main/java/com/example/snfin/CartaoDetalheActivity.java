package com.example.snfin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.models.cartao.CartaoDetalheDTO;
import com.example.snfin.models.cartao.CartaoDetalheResponse;
import com.example.snfin.models.lancamento.LancamentoAdapter;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartaoDetalheActivity extends AppCompatActivity {

    private TextView tvFaturaAtual, tvFaltaPagar, tvLimite, tvParceladas;
    private RecyclerView rvLancamentos;

    private LancamentoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_detalhe);

        initViews();
        setupRecycler();
        carregarDados();
    }

    private void initViews() {
        tvFaturaAtual = findViewById(R.id.tvFaturaAtual);
        tvFaltaPagar = findViewById(R.id.tvFaltaPagar);
        tvLimite = findViewById(R.id.tvLimite);
        tvParceladas = findViewById(R.id.tvParceladas);

        rvLancamentos = findViewById(R.id.rvLancamentos);
    }

    private void setupRecycler() {
        adapter = new LancamentoAdapter(this, new ArrayList<>());
        rvLancamentos.setLayoutManager(new LinearLayoutManager(this));
        rvLancamentos.setAdapter(adapter);
    }

    private void carregarDados() {

        int cartaoId = getIntent().getIntExtra("cartaoId", -1);

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("auth_token", null);

        ApiService api = RetrofitInstance
                .getRetrofitInstance(this)
                .create(ApiService.class);

        Call<CartaoDetalheResponse> call = api.getCartao("Token " + token, cartaoId);

        call.enqueue(new Callback<CartaoDetalheResponse>() {
            @Override
            public void onResponse(Call<CartaoDetalheResponse> call, Response<CartaoDetalheResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    CartaoDetalheDTO dto = response.body().getCartao();

                    tvFaturaAtual.setText("R$ " + dto.getTotalFatura());
                    tvFaltaPagar.setText("R$ " + dto.getFaltaPagar());
                    tvLimite.setText("R$ " + dto.getLimite());
                    tvParceladas.setText("R$ " + dto.getComprasParceladas());

                    adapter.updateList(response.body().getLancamentos());
                }
            }

            @Override
            public void onFailure(Call<CartaoDetalheResponse> call, Throwable t) {
                Toast.makeText(CartaoDetalheActivity.this,
                        "Erro ao carregar: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}