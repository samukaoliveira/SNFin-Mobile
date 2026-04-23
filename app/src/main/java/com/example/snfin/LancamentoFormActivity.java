package com.example.snfin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;
import com.example.snfin.models.lancamento.LancamentoDTO;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LancamentoFormActivity extends AppCompatActivity {

    private EditText etDescricao, etValor, etData, etParcelas;
    private Spinner spNatureza, spFixo, spEscopo;
    private CheckBox cbPago;
    private Button btnSalvar, btnCancelar;
    private TextView tvTitulo;

    private Integer lancamentoId = -1; // 🔥 controla edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento_form);

        initViews();
        setupSpinners();
        setupListeners();
        verificarModoEdicao();
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        etDescricao = findViewById(R.id.etDescricao);
        etValor = findViewById(R.id.etValor);
        etData = findViewById(R.id.etData);
        etParcelas = findViewById(R.id.etParcelas);

        spNatureza = findViewById(R.id.spNatureza);
        spFixo = findViewById(R.id.spFixo);
        spEscopo = findViewById(R.id.spEscopo);

        cbPago = findViewById(R.id.cbPago);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    private void setupSpinners() {
        spNatureza.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"RECEITA", "DESPESA"}
        ));

        spFixo.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"NAO", "FIXO", "PARCELADO"}
        ));

        spEscopo.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"um", "todos"}
        ));
    }

    private void setupListeners() {

        etData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String data = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        etData.setText(data);
                    },
                    ano, mes, dia
            );

            dialog.show();
        });

        spFixo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String valor = parent.getItemAtPosition(pos).toString();

                if (valor.equals("PARCELADO")) {
                    etParcelas.setVisibility(View.VISIBLE);
                } else {
                    etParcelas.setVisibility(View.GONE);
                    etParcelas.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCancelar.setOnClickListener(v -> finish());
        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void verificarModoEdicao() {
        Intent intent = getIntent();

        if (intent.hasExtra("id")) {
            lancamentoId = intent.getIntExtra("id", -1);

            tvTitulo.setText("Editar Lançamento");
            spEscopo.setVisibility(View.VISIBLE);

            etDescricao.setText(intent.getStringExtra("descricao"));
            etValor.setText(String.valueOf(intent.getDoubleExtra("valor", 0)));
            etData.setText(intent.getStringExtra("data"));
            cbPago.setChecked(intent.getBooleanExtra("pago", false));

            // 🔥 NOVO: recuperar valores
            String natureza = intent.getStringExtra("natureza");
            String fixo = intent.getStringExtra("fixo");
            int parcelas = intent.getIntExtra("parcelas", 0);

            Log.d("EDIT", "Natureza: " + natureza);
            Log.d("EDIT", "Fixo: " + fixo);
            Log.d("EDIT", "Parcelas: " + parcelas);

            // 🔥 NOVO: setar spinner Natureza
            if (natureza != null) {
                ArrayAdapter adapterNatureza = (ArrayAdapter) spNatureza.getAdapter();
                int posNatureza = adapterNatureza.getPosition(natureza);
                if (posNatureza >= 0) {
                    spNatureza.setSelection(posNatureza);
                }
            }

            // 🔥 NOVO: setar spinner Fixo
            if (fixo != null) {
                ArrayAdapter adapterFixo = (ArrayAdapter) spFixo.getAdapter();
                int posFixo = adapterFixo.getPosition(fixo);
                if (posFixo >= 0) {
                    spFixo.setSelection(posFixo);
                }
            }

            // 🔥 NOVO: parcelas
            if (parcelas > 0) {
                etParcelas.setText(String.valueOf(parcelas));
                etParcelas.setVisibility(View.VISIBLE);
            }

        } else {
            spEscopo.setVisibility(View.GONE);
        }

        Log.d("EDIT", "ID recebido: " + lancamentoId);
    }

    private void salvar() {

        try {
            String descricao = etDescricao.getText().toString();
            double valor = Double.parseDouble(etValor.getText().toString());
            String data = etData.getText().toString();
            String natureza = spNatureza.getSelectedItem().toString();
            String fixo = spFixo.getSelectedItem().toString();

            String parcelasStr = etParcelas.getText().toString();
            int parcelas = parcelasStr.isEmpty() ? 0 : Integer.parseInt(parcelasStr);

            boolean pago = cbPago.isChecked();

            LancamentoDTO req = new LancamentoDTO();
            req.setDescricao(descricao);
            req.setValor(valor);
            req.setData(data);
            req.setNatureza(natureza);
            req.setFixo(fixo);
            req.setParcelas(parcelas);
            req.setPago(pago);

            Context context = this;
            SharedPreferences sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String token = sharedPref.getString("auth_token", null);

            ApiService api = RetrofitInstance.getRetrofitInstance(context).create(ApiService.class);

            Call<Void> call;

            if (lancamentoId == null || lancamentoId == -1) {
                call = api.criarLancamento(token, req);
            } else {
                call = api.atualizarLancamento(token, lancamentoId, req);
            }

            Log.d("API", "Modo: " + (lancamentoId == -1 ? "CREATE" : "UPDATE"));

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(LancamentoFormActivity.this,
                                "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        try {
                            String erro = response.errorBody().string();
                            Log.e("API", erro);
                            Toast.makeText(LancamentoFormActivity.this,
                                    "Erro: " + erro, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(LancamentoFormActivity.this,
                                    "Erro ao salvar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(LancamentoFormActivity.this,
                            "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show();
        }
    }
}