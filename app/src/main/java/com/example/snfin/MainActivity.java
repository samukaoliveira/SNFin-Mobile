package com.example.snfin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appml.R;
import com.example.snfin.adapters.LancamentoAdapter;
import com.example.snfin.models.DashboardResponse;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private TextView tvSaldoCaixa, tvSaldoPrevisto, tvReceitasPrevistas,
            tvDespesasPrevistas, tvReceitasRealizadas, tvDespesasRealizadas;

    private RecyclerView recyclerView;
    private LancamentoAdapter adapter;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_financas);

        // Cards
        tvSaldoCaixa = findViewById(R.id.tvSaldoCaixa);
        tvSaldoPrevisto = findViewById(R.id.tvSaldoPrevisto);
        tvReceitasPrevistas = findViewById(R.id.tvReceitasPrevistas);
        tvDespesasPrevistas = findViewById(R.id.tvDespesasPrevistas);
        tvReceitasRealizadas = findViewById(R.id.tvReceitasRealizadas);
        tvDespesasRealizadas = findViewById(R.id.tvDespesasRealizadas);

        // Lista
        recyclerView = findViewById(R.id.recyclerLancamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LancamentoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        apiService = RetrofitInstance.getRetrofitInstance(this)
                .create(ApiService.class);

        carregarDashboard();
    }

    private void carregarDashboard() {
        apiService.getDashboard().enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    DashboardResponse data = response.body();

                    tvSaldoCaixa.setText("R$ " + data.getSaldoEmCaixa());
                    tvSaldoPrevisto.setText("R$ " + data.getSaldoPrevisto());
                    tvReceitasPrevistas.setText("R$ " + data.getReceitasPrevistas());
                    tvDespesasPrevistas.setText("R$ " + data.getDespesasPrevistas());
                    tvReceitasRealizadas.setText("R$ " + data.getReceitasRealizadas());
                    tvDespesasRealizadas.setText("R$ " + data.getDespesasRealizadas());

                    adapter.updateList(data.getLancamentos());
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {

            }
        });
    }
}