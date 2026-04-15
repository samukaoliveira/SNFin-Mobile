package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardResponse {

    @SerializedName("saldo_em_caixa")
    private double saldoEmCaixa;

    @SerializedName("saldo_previsto")
    private double saldoPrevisto;

    @SerializedName("receitas_previstas")
    private double receitasPrevistas;

    @SerializedName("despesas_previstas")
    private double despesasPrevistas;

    @SerializedName("receitas_realizadas")
    private double receitasRealizadas;

    @SerializedName("despesas_realizadas")
    private double despesasRealizadas;

    private List<Lancamento> lancamentos;

    private List<CartaoResumo> cartoes;

    // GETTERS

    public double getSaldoEmCaixa() {
        return saldoEmCaixa;
    }

    public double getSaldoPrevisto() {
        return saldoPrevisto;
    }

    public double getReceitasPrevistas() {
        return receitasPrevistas;
    }

    public double getDespesasPrevistas() {
        return despesasPrevistas;
    }

    public double getReceitasRealizadas() {
        return receitasRealizadas;
    }

    public double getDespesasRealizadas() {
        return despesasRealizadas;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public List<CartaoResumo> getCartoes() {
        return cartoes;
    }
}
