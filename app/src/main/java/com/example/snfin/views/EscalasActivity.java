package com.example.snfin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.BaseActivity;
import com.example.snfin.R;
import com.example.snfin.services.HomeService;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;

public class EscalasActivity extends BaseActivity implements EscalasAdapter.OnEscalaClickListener {

    private RecyclerView recyclerView;
    private Button minhasEscalas;

    ImageButton btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalas);

        btnHome = findViewById(R.id.btnHome);
        HomeService.VoltaPraHome(btnHome, this);

        recyclerView = findViewById(R.id.recycler_escalas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        minhasEscalas = findViewById(R.id.minhas_escalas);

        minhasEscalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EscalasActivity.this, MinhasEscalasActivity.class);
                startActivity(intent);
            }
        });

        ApiService api = RetrofitInstance.getRetrofitInstance(this)
                .create(ApiService.class);

        new CarregaEscalas().carregar(
                this,
                recyclerView,
                api.getListaEscalas(),
                this
        );
    }

    @Override
    public void onEscalaClick(int escalaId) {
        // Abrir detalhe passando o id da escala clicada
        Intent intent = new Intent(this, EscalaDetalheActivity.class);
        intent.putExtra("escala_id", escalaId);
        startActivity(intent);
    }
}
