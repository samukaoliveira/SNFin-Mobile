package com.example.snfin.models.cartao;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CartaoDetalheDTO implements Serializable {

    private Integer id;

    @SerializedName("descricao")
    private String nome;

    private Double limite;

    @SerializedName("dia_fechamento")
    private Integer diaFechamento;

    @SerializedName("dia_vencimento")
    private Integer diaVencimento;

    @SerializedName("total_fatura")
    private Double totalFatura;

    @SerializedName("falta_pagar")
    private Double faltaPagar;

    @SerializedName("fatura")
    private FaturaDTO fatura;

    @SerializedName("limite_disponivel")
    private Double limiteDisponivel;

    @SerializedName("compras_parceladas")
    private Double comprasParceladas;

    public CartaoDetalheDTO() {}

    // ===== GETTERS =====

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getLimite() {
        return limite;
    }

    public Integer getDiaFechamento() {
        return diaFechamento;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public Double getTotalFatura() {
        return totalFatura;
    }

    public Double getFaltaPagar() {
        return faltaPagar;
    }

    public Double getLimiteDisponivel() {
        return limiteDisponivel;
    }

    public Double getComprasParceladas() {
        return comprasParceladas;
    }

    // ===== SETTERS =====

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public void setDiaFechamento(Integer diaFechamento) {
        this.diaFechamento = diaFechamento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public void setTotalFatura(Double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public void setFaltaPagar(Double faltaPagar) {
        this.faltaPagar = faltaPagar;
    }

    public void setLimiteDisponivel(Double limiteDisponivel) {
        this.limiteDisponivel = limiteDisponivel;
    }

    public void setComprasParceladas(Double comprasParceladas) {
        this.comprasParceladas = comprasParceladas;
    }

    public FaturaDTO getFatura() {
        return fatura;
    }

    public Integer getFaturaId() {
        return fatura.getId();
    }
    public void setFatura(FaturaDTO fatura) {
        this.fatura = fatura;
    }
}