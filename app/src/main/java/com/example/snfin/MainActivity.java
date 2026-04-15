package com.example.snfin;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.R;
import com.example.snfin.models.lancamento.LancamentoAdapter;
import com.example.snfin.models.lancamento.DashboardResponse;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private TextView tvSaldoCaixa, tvSaldoPrevisto, tvReceitasPrevistas,
            tvDespesasPrevistas, tvReceitasRealizadas, tvDespesasRealizadas;

    private RecyclerView recyclerView;
    private LancamentoAdapter adapter;

    private ApiService apiService;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== CARDS =====
        tvSaldoCaixa = findViewById(R.id.tvSaldoCaixa);
        tvSaldoPrevisto = findViewById(R.id.tvSaldoPrevisto);
        tvReceitasPrevistas = findViewById(R.id.tvReceitasPrevistas);
        tvDespesasPrevistas = findViewById(R.id.tvDespesasPrevistas);
        tvReceitasRealizadas = findViewById(R.id.tvReceitasRealizadas);
        tvDespesasRealizadas = findViewById(R.id.tvDespesasRealizadas);

        // ===== LOADING =====
        progressBar = findViewById(R.id.progressBar);

        // ===== LISTA =====
        recyclerView = findViewById(R.id.recyclerLancamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LancamentoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ===== API =====
        apiService = RetrofitInstance.getRetrofitInstance(this)
                .create(ApiService.class);

        carregarDashboard();
    }

    private void carregarDashboard() {

        mostrarLoading(true);

        apiService.getDashboard().enqueue(new Callback<DashboardResponse>() {

            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {

                mostrarLoading(false);

                if (response.isSuccessful() && response.body() != null) {

                    DashboardResponse data = response.body();

                    atualizarCards(data);

                    adapter.updateList(data.getLancamentos());

                } else {
                    Toast.makeText(MainActivity.this,
                            "Erro ao carregar dados",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {

                mostrarLoading(false);

                Toast.makeText(MainActivity.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ===== ATUALIZA CARDS =====
    private void atualizarCards(DashboardResponse data) {

        setValorColorido(tvSaldoCaixa, data.getSaldoEmCaixa());
        setValorColorido(tvSaldoPrevisto, data.getSaldoPrevisto());

        tvReceitasPrevistas.setText(formatarMoeda(data.getReceitasPrevistas()));
        tvReceitasPrevistas.setTextColor(getColor(R.color.verde_claro));

        tvDespesasPrevistas.setText(formatarMoeda(data.getDespesasPrevistas()));
        tvDespesasPrevistas.setTextColor(getColor(R.color.despesa));

        tvReceitasRealizadas.setText(formatarMoeda(data.getReceitasRealizadas()));
        tvReceitasRealizadas.setTextColor(getColor(R.color.verde_claro));

        tvDespesasRealizadas.setText(formatarMoeda(data.getDespesasRealizadas()));
        tvDespesasRealizadas.setTextColor(getColor(R.color.despesa));
    }

    // ===== FORMATA MOEDA =====
    private String formatarMoeda(double valor) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return format.format(valor);
    }

    // ===== DEFINE COR AUTOMÁTICA =====
    private void setValorColorido(TextView tv, double valor) {
        tv.setText(formatarMoeda(valor));

        if (valor < 0) {
            tv.setTextColor(getColor(R.color.despesa));
        } else {
            tv.setTextColor(getColor(R.color.verde_claro));
        }
    }

    // ===== LOADING =====
    private void mostrarLoading(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }
}