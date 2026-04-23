package com.example.snfin.models.cartao;

import java.io.Serializable;

public class CartaoDetalheDTO implements Serializable {

    private Integer id;
    private String nome;
    private Double limite;

    // 🔥 Datas importantes do cartão
    private Integer diaFechamento;
    private Integer diaVencimento;

    // 🔥 Valores da fatura
    private Double totalFatura;
    private Double faltaPagar;
    private Double limiteDisponivel;
    private Double comprasParceladas;

    public CartaoDetalheDTO() {}

    // ===== GETTERS E SETTERS =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Integer getDiaFechamento() {
        return diaFechamento;
    }

    public void setDiaFechamento(Integer diaFechamento) {
        this.diaFechamento = diaFechamento;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public Double getTotalFatura() {
        return totalFatura;
    }

    public void setTotalFatura(Double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public Double getFaltaPagar() {
        return faltaPagar;
    }

    public void setFaltaPagar(Double faltaPagar) {
        this.faltaPagar = faltaPagar;
    }

    public Double getLimiteDisponivel() {
        return limiteDisponivel;
    }

    public void setLimiteDisponivel(Double limiteDisponivel) {
        this.limiteDisponivel = limiteDisponivel;
    }

    public Double getComprasParceladas() {
        return comprasParceladas;
    }

    public void setComprasParceladas(Double comprasParceladas) {
        this.comprasParceladas = comprasParceladas;
    }
}