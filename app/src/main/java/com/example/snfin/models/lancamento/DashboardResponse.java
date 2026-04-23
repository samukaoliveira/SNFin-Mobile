package com.example.snfin.models.lancamento;

import com.example.snfin.models.cartao.CartaoResumoDTO;

import java.util.List;

public class DashboardResponse {

    private Totais totais;
    private Saldos saldos;

    private List<Lancamento> lancamentos;
    private List<CartaoResumoDTO> cartoes;

    public Totais getTotais() {
        return totais;
    }

    public Saldos getSaldos() {
        return saldos;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public List<CartaoResumoDTO> getCartoes() {
        return cartoes;
    }
}