package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;

public class Fatura {

    private int id;

    @SerializedName("valor_pago")
    private double valorPago;

    @SerializedName("data_pagamento")
    private String dataPagamento;

    public int getId() {
        return id;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }
}