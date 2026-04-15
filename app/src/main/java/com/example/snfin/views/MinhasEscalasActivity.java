package com.example.snfin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.BaseActivity;
import com.example.snfin.R;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.HomeService;
import com.example.snfin.services.RetrofitInstance;

public class MinhasEscalasActivity extends BaseActivity
        implements EscalasAdapter.OnEscalaClickListener {

    private RecyclerView recyclerView;

    private Button todasAsEscalas;

    ImageButton btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_escalas);

        btnHome = findViewById(R.id.btnHome);
        HomeService.VoltaPraHome(btnHome, this);

        recyclerView = findViewById(R.id.recycler_escalas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todasAsEscalas = findViewById(R.id.todas_as_escalas);

        todasAsEscalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MinhasEscalasActivity.this, EscalasActivity.class);
                startActivity(intent);
            }
        });

        ApiService api = RetrofitInstance.getRetrofitInstance(this)
                .create(ApiService.class);

        new CarregaEscalas().carregar(
                this,
                recyclerView,
                api.getMinhasEscalas(),
                this
        );
    }

    @Override
    public void onEscalaClick(int escalaId) {
        startActivity(new Intent(this, EscalaDetalheActivity.class)
                .putExtra("escala_id", escalaId));
    }
}
