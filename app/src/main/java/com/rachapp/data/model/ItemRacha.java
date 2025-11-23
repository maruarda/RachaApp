package com.rachapp.data.model;

public class ItemRacha {
    private Long idItemRacha;
    private String nome;
    private Double preco;
    private RachaIdObject racha;
    private Usuario payer;

    public ItemRacha(String nome, Double preco, Long rachaId, Usuario payer) {
        this.nome = nome;
        this.preco = preco;
        this.racha = new RachaIdObject(rachaId);
        this.payer = payer;
    }

    public Long getIdItemRacha() { return idItemRacha; }
    public String getNome() { return nome; }
    public Double getPreco() { return preco; }

    // Getter for Payer
    public Usuario getPayer() { return payer; }

    private static class RachaIdObject {
        private Long idRacha;
        public RachaIdObject(Long id) { this.idRacha = id; }
    }
}