package com.example.snfin.models.lancamento;

import com.example.snfin.models.lancamento.CartaoResumo;
import com.example.snfin.models.lancamento.Lancamento;
import com.example.snfin.models.lancamento.Saldos;
import com.example.snfin.models.lancamento.Totais;

import java.util.List;

public class DashboardResponse {

    private Totais totais;
    private Saldos saldos;

    private List<Lancamento> lancamentos;
    private List<CartaoResumo> cartoes;

    public Totais getTotais() {
        return totais;
    }

    public Saldos getSaldos() {
        return saldos;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public List<CartaoResumo> getCartoes() {
        return cartoes;
    }
}