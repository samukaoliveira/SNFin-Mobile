package com.example.snfin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.models.lancamento.DashboardResponse;
import com.example.snfin.models.lancamento.LancamentoAdapter;
import com.example.snfin.models.lancamento.Saldos;
import com.example.snfin.models.lancamento.Totais;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    private TextView tvSaldoCaixa;
    private TextView tvSaldoPrevisto;
    private TextView tvReceitasPrevistas;
    private TextView tvDespesasPrevistas;
    private TextView tvReceitasRealizadas;
    private TextView tvDespesasRealizadas;

    private Button btnNovo;

    private RecyclerView recyclerView;
    private LancamentoAdapter adapter;

    private ProgressBar progressBar;

    private ApiService apiService;
    public ActivityResultLauncher<Intent> launcher;

    private int mesAtual;
    private int anoAtual;
    private TextView tvCompetencia;
    private ImageButton btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        mesAtual = calendar.get(Calendar.MONTH) + 1; // 🔥 mês começa em 0
        anoAtual = calendar.get(Calendar.YEAR);

        tvCompetencia = findViewById(R.id.tvCompetencia);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        atualizarCompetencia();

        Log.d(TAG, "onCreate iniciado");

        // ===== TEXTVIEWS =====
        tvSaldoCaixa = findViewById(R.id.tvSaldoCaixa);
        tvSaldoPrevisto = findViewById(R.id.tvSaldoPrevisto);
        tvReceitasPrevistas = findViewById(R.id.tvReceitasPrevistas);
        tvDespesasPrevistas = findViewById(R.id.tvDespesasPrevistas);
        tvReceitasRealizadas = findViewById(R.id.tvReceitasRealizadas);
        tvDespesasRealizadas = findViewById(R.id.tvDespesasRealizadas);

        // ===== PROGRESS =====
        progressBar = findViewById(R.id.progressBar);

        // ===== RECYCLER =====
        recyclerView = findViewById(R.id.recyclerLancamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LancamentoAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        btnNovo = findViewById(R.id.btnNovo);

        // ===== API =====
        apiService = RetrofitInstance
                .getRetrofitInstance(this)
                .create(ApiService.class);

        btnNovo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LancamentoFormActivity.class);
            launcher.launch(intent);
        });

        btnPrev.setOnClickListener(v -> {
            mesAtual--;

            if (mesAtual < 1) {
                mesAtual = 12;
                anoAtual--;
            }

            atualizarCompetencia();
            carregarDashboard();
        });

        btnNext.setOnClickListener(v -> {
            mesAtual++;

            if (mesAtual > 12) {
                mesAtual = 1;
                anoAtual++;
            }

            atualizarCompetencia();
            carregarDashboard();
        });

        carregarDashboard();

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        carregarDashboard(); // 🔥 sua função que chama API da home
                    }
                }
        );
    }

    private void atualizarCompetencia() {

        String[] meses = {
                "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"
        };

        String texto = meses[mesAtual - 1] + "/" + anoAtual;
        tvCompetencia.setText(texto);
    }

    private void carregarDashboard() {

        Log.d(TAG, "Iniciando chamada da API /home/");

        mostrarLoading(true);

        apiService.getHome(mesAtual, anoAtual).enqueue(new Callback<DashboardResponse>() {

            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {

                mostrarLoading(false);

                Log.d(TAG, "Código HTTP: " + response.code());
                Log.d(TAG, "Response raw: " + response.raw());

                if (response.body() != null) {
                    Log.d(TAG, "JSON convertido:");
                    Log.d(TAG, new Gson().toJson(response.body()));
                } else {
                    Log.d(TAG, "response.body() veio null");
                }

                if (response.isSuccessful() && response.body() != null) {

                    DashboardResponse dashboard = response.body();

                    Log.d(TAG, "Saldos: " + dashboard.getSaldos());
                    Log.d(TAG, "Totais: " + dashboard.getTotais());

                    if (dashboard.getLancamentos() != null) {
                        Log.d(TAG, "Qtd lançamentos: " + dashboard.getLancamentos().size());
                    } else {
                        Log.d(TAG, "Lista de lançamentos veio null");
                    }

                    atualizarCards(dashboard);

                    if (dashboard.getLancamentos() != null) {
                        adapter.updateList(dashboard.getLancamentos());
                    }

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Erro ao carregar dados. Código: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();

                    Log.e(TAG, "Resposta inválida ou body null");
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {

                mostrarLoading(false);

                Log.e(TAG, "Falha na chamada", t);

                Toast.makeText(
                        MainActivity.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void atualizarCards(DashboardResponse data) {

        if (data.getSaldos() == null) {
            Log.e(TAG, "data.getSaldos() veio null");
            return;
        }

        if (data.getTotais() == null) {
            Log.e(TAG, "data.getTotais() veio null");
            return;
        }

        Saldos saldos = data.getSaldos();
        Totais totais = data.getTotais();

        Log.d(TAG, "saldoEmCaixa = " + saldos.getSaldoEmCaixa());
        Log.d(TAG, "saldoPrevisto = " + saldos.getSaldoPrevisto());

        Log.d(TAG, "receitas = " + totais.getReceitas());
        Log.d(TAG, "despesas = " + totais.getDespesas());
        Log.d(TAG, "receitasRealizadas = " + totais.getReceitasRealizadas());
        Log.d(TAG, "despesasRealizadas = " + totais.getDespesasRealizadas());

        // ===== SALDOS =====
        setValorColorido(tvSaldoCaixa, saldos.getSaldoEmCaixa());
        setValorColorido(tvSaldoPrevisto, saldos.getSaldoPrevisto());

        // ===== RECEITAS / DESPESAS =====
        tvReceitasPrevistas.setText(formatarMoeda(totais.getReceitas()));
        tvReceitasPrevistas.setTextColor(getColor(R.color.verde_claro));

        tvDespesasPrevistas.setText(formatarMoeda(totais.getDespesas()));
        tvDespesasPrevistas.setTextColor(getColor(R.color.despesa));

        tvReceitasRealizadas.setText(formatarMoeda(totais.getReceitasRealizadas()));
        tvReceitasRealizadas.setTextColor(getColor(R.color.verde_claro));

        tvDespesasRealizadas.setText(formatarMoeda(totais.getDespesasRealizadas()));
        tvDespesasRealizadas.setTextColor(getColor(R.color.despesa));
    }

    private String formatarMoeda(double valor) {
        NumberFormat format =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return format.format(valor);
    }

    private void setValorColorido(TextView textView, double valor) {

        textView.setText(formatarMoeda(valor));

        if (valor < 0) {
            textView.setTextColor(getColor(R.color.despesa));
        } else {
            textView.setTextColor(getColor(R.color.verde_claro));
        }
    }

    private void mostrarLoading(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }
}