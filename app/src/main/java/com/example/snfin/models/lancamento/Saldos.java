package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;

public class Saldos {

    @SerializedName("saldo_previsto")
    private double saldoPrevisto;

    @SerializedName("saldo_em_caixa")
    private double saldoEmCaixa;

    // ===== GETTERS =====

    public double getSaldoPrevisto() {
        return saldoPrevisto;
    }

    public double getSaldoEmCaixa() {
        return saldoEmCaixa;
    }

    // (opcional, mas útil pra debug)
    @Override
    public String toString() {
        return "Saldos{" +
                "saldoPrevisto=" + saldoPrevisto +
                ", saldoEmCaixa=" + saldoEmCaixa +
                '}';
    }
}