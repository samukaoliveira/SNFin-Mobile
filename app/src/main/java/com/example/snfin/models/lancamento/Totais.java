package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;

public class Totais {

    private double receitas;
    private double despesas;

    @SerializedName("receitas_realizadas")
    private double receitasRealizadas;

    @SerializedName("despesas_realizadas")
    private double despesasRealizadas;

    // ===== GETTERS =====

    public double getReceitas() {
        return receitas;
    }

    public double getDespesas() {
        return despesas;
    }

    public double getReceitasRealizadas() {
        return receitasRealizadas;
    }

    public double getDespesasRealizadas() {
        return despesasRealizadas;
    }

    // (opcional - debug)
    @Override
    public String toString() {
        return "Totais{" +
                "receitas=" + receitas +
                ", despesas=" + despesas +
                ", receitasRealizadas=" + receitasRealizadas +
                ", despesasRealizadas=" + despesasRealizadas +
                '}';
    }
}