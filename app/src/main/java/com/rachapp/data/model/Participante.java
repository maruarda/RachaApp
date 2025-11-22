package com.rachapp.data.model;

import java.util.ArrayList;
import java.util.List;

public class Participante {
    private String nome;
    private List<ItemDivida> itens; // Lista do que a pessoa consumiu/deve
    private boolean selecionado;    // Usado no checkbox de dividir conta
    private boolean pagante;        // Usado para mostrar a estrela (se pagou a conta toda)
    private boolean pago;           // (Opcional) Se a pessoa já pagou a parte dela

    public Participante(String nome) {
        this.nome = nome;
        this.itens = new ArrayList<>();
        this.selecionado = true; // Por padrão, já vem marcado para dividir
        this.pagante = false;    // Por padrão, não é o pagante da conta total
        this.pago = false;       // Por padrão, ainda não pagou sua parte
    }

    // Adiciona uma dívida à lista dessa pessoa
    public void adicionarDivida(String nomeItem, double valor) {
        this.itens.add(new ItemDivida(nomeItem, valor));
    }

    // Soma tudo o que a pessoa deve percorrendo a lista de itens
    public double getTotalPagar() {
        double total = 0;
        for (ItemDivida item : itens) {
            total += item.getValorParcela();
        }
        return total;
    }

    // --- Getters e Setters ---

    public String getNome() {
        return nome;
    }

    public List<ItemDivida> getItens() {
        return itens;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public boolean isPagante() {
        return pagante;
    }

    public void setPagante(boolean pagante) {
        this.pagante = pagante;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }
}