package com.example.snfin.models.cartao;

import com.example.snfin.models.lancamento.Lancamento;
import java.util.List;

public class CartaoDetalheResponse {

    private CartaoDetalheDTO cartao;
    private List<Lancamento> lancamentos;

    public CartaoDetalheDTO getCartao() {
        return cartao;
    }

    public void setCartao(CartaoDetalheDTO cartao) {
        this.cartao = cartao;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }
}