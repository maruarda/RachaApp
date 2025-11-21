package com.example.rachaapp.model;

public class ResumoItem {
    private String nomePessoa;
    private String nomeRacha;
    private double valor;

    public ResumoItem(String nomePessoa, String nomeRacha, double valor) {
        this.nomePessoa = nomePessoa;
        this.nomeRacha = nomeRacha;
        this.valor = valor;
    }

    public String getNomePessoa() { return nomePessoa; }
    public String getNomeRacha() { return nomeRacha; }
    public double getValor() { return valor; }
}