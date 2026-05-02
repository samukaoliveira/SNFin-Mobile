package com.example.snfin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button; // 🔥 NOVO
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.models.cartao.CartaoDetalheDTO;
import com.example.snfin.models.cartao.CartaoDetalheResponse;
import com.example.snfin.models.cartao.FaturaDTO;
import com.example.snfin.models.lancamento.LancamentoAdapter;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartaoDetalheActivity extends AppCompatActivity {

    private static final String TAG = "CARTAO_DETALHE";

    private TextView tvNomeCartao, tvFaturaAtual, tvFaltaPagar, tvLimite, tvParceladas;
    private RecyclerView rvLancamentos;

    private ProgressBar progressBar;
    private Button btnNovo; // 🔥 NOVO

    private LancamentoAdapter adapter;

    private Integer fatura = null; // 🔥 NOVO

    private ActivityResultLauncher<Intent> formLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_detalhe);

        initViews();
        setupRecycler();

        formLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "Retornou do form → recarregando");
                        recarregar();
                    }
                }
        );

        configurarBotaoNovo(); // 🔥 NOVO

        carregarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume → recarregando dados");
        recarregar();
    }

    private void recarregar() {
        carregarDados();
    }

    private void initViews() {
        tvNomeCartao = findViewById(R.id.tvNomeCartao);
        tvFaturaAtual = findViewById(R.id.tvFaturaAtual);
        tvFaltaPagar = findViewById(R.id.tvFaltaPagar);
        tvLimite = findViewById(R.id.tvLimite);
        tvParceladas = findViewById(R.id.tvParceladas);

        rvLancamentos = findViewById(R.id.rvLancamentos);

        progressBar = findViewById(R.id.progressBar);

        btnNovo = findViewById(R.id.btnNovo); // 🔥 NOVO
    }

    private void setupRecycler() {
        adapter = new LancamentoAdapter(this, new ArrayList<>());
        rvLancamentos.setLayoutManager(new LinearLayoutManager(this));
        rvLancamentos.setAdapter(adapter);
    }

    private void mostrarLoading(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    // =========================
    // 🔥 BOTÃO NOVO LANÇAMENTO
    // =========================
    private void configurarBotaoNovo() {
        btnNovo.setOnClickListener(v -> {

            if (fatura == null) {
                Toast.makeText(this, "Fatura ainda não carregada", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, LancamentoFormActivity.class);

            // 🔥 ENVIA FATURA
            intent.putExtra("faturaId", fatura);

            Log.d(TAG, "Abrindo form com faturaId: " + fatura);

            formLauncher.launch(intent);
        });
    }

    private void carregarDados() {

        mostrarLoading(true);

        int cartaoId = getIntent().getIntExtra("cartaoId", -1);

        Log.d(TAG, "cartaoId recebido: " + cartaoId);

        if (cartaoId == -1) {
            mostrarLoading(false);
            Toast.makeText(this, "Cartão inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("auth_token", null);

        if (token == null) {
            mostrarLoading(false);
            Toast.makeText(this, "Token não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiService api = RetrofitInstance
                .getRetrofitInstance(this)
                .create(ApiService.class);

        Call<CartaoDetalheResponse> call = api.getCartao("Token " + token, cartaoId);

        call.enqueue(new Callback<CartaoDetalheResponse>() {
            @Override
            public void onResponse(Call<CartaoDetalheResponse> call, Response<CartaoDetalheResponse> response) {

                mostrarLoading(false);

                Log.d(TAG, "HTTP: " + response.code());

                if (response.body() != null) {
                    Log.d(TAG, "JSON: " + new Gson().toJson(response.body()));
                } else {
                    Log.e(TAG, "Body veio null");
                }

                if (response.isSuccessful() && response.body() != null) {

                    CartaoDetalheResponse body = response.body();
                    CartaoDetalheDTO dto = body.getCartao();

                    if (dto == null) {
                        Log.e(TAG, "DTO veio null");
                        Toast.makeText(CartaoDetalheActivity.this,
                                "Erro ao carregar dados do cartão",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🔥 CAPTURA FATURA ID
                    if (dto.getFatura() != null) {
                        fatura = dto.getFaturaId();
                        Log.d(TAG, "Fatura ID: " + fatura);
                    }

                    tvNomeCartao.setText(dto.getNome());
                    setTitle("Cartão " + dto.getNome());

                    tvFaturaAtual.setText(formatarMoeda(dto.getTotalFatura()));
                    tvFaltaPagar.setText(formatarMoeda(dto.getFaltaPagar()));
                    tvLimite.setText(formatarMoeda(dto.getLimite()));
                    tvParceladas.setText(formatarMoeda(dto.getComprasParceladas()));

                    if (body.getLancamentos() != null) {
                        Log.d(TAG, "Qtd lançamentos: " + body.getLancamentos().size());
                        adapter.updateList(body.getLancamentos());
                    } else {
                        Log.d(TAG, "Lista de lançamentos veio null");
                        adapter.updateList(new ArrayList<>());
                    }

                } else {

                    Toast.makeText(CartaoDetalheActivity.this,
                            "Erro ao carregar cartão (" + response.code() + ")",
                            Toast.LENGTH_LONG).show();

                    Log.e(TAG, "Erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<CartaoDetalheResponse> call, Throwable t) {

                mostrarLoading(false);

                Log.e(TAG, "Erro na API", t);

                Toast.makeText(CartaoDetalheActivity.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String formatarMoeda(Double valor) {

        if (valor == null) {
            valor = 0.0;
        }

        NumberFormat format =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        return format.format(valor);
    }
}