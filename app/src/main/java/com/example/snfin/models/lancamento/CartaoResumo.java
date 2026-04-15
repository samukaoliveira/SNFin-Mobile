package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;

public class CartaoResumo {

    private int id;
    private String descricao;

    @SerializedName("valor_fatura")
    private double valorFatura;

    private Fatura fatura;

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValorFatura() {
        return valorFatura;
    }

    public Fatura getFatura() {
        return fatura;
    }
}
