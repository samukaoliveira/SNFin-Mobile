package com.example.snfin.models.lancamento;

import com.google.gson.annotations.SerializedName;

public class Lancamento {

    private Integer id;
    private String descricao;
    private String data;
    private double valor;
    private boolean pago;

    private String fixo;
    private int parcelas;

    private Integer fatura; // pode ser null

    @SerializedName("dt_criacao")
    private String dtCriacao;

    @SerializedName("dt_update")
    private String dtUpdate;

    @SerializedName("eh_rotativo")
    private boolean ehRotativo;

    private String natureza;

    @SerializedName("is_pagamento_fatura")
    private boolean isPagamentoFatura;

    @SerializedName("grupo_id")
    private String grupoId;

    // 🔥 GETTERS

    public Integer getId() {
        return id;
    }
    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return formatarData(data);
    }

    public String getDataCurta() {
        return formatarDataCurta(data);
    }

    public double getValor() {
        return valor;
    }

    public boolean isPago() {
        return pago;
    }

    public String getNatureza() {
        return natureza;
    }

    public String getFixo() {
        return fixo;
    }

    public int getParcelas() {
        return parcelas;
    }

    public boolean isPagamentoFatura() {
        return isPagamentoFatura;
    }

    public String getGrupoId() {
        return grupoId;
    }

    // 🎯 FORMATADOR (igual seu template Django)

    private String formatarData(String data) {
        try {
            String[] partes = data.split("-");
            return partes[2] + "/" + partes[1] + "/" + partes[0];
        } catch (Exception e) {
            return data;
        }
    }

    private String formatarDataCurta(String data) {
        try {
            String[] partes = data.split("-");
            return partes[2] + "/" + partes[1];
        } catch (Exception e) {
            return data;
        }
    }
}