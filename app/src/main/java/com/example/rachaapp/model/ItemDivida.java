package com.example.rachaapp.model;

public class ItemDivida {
    private String nome;
    private double valorParcela;

    public ItemDivida(String nome, double valorParcela) {
        this.nome = nome;
        this.valorParcela = valorParcela;
    }

    public String getNome() { return nome; }
    public double getValorParcela() { return valorParcela; }
}