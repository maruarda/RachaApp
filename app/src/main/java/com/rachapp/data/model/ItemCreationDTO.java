package com.rachapp.data.model;

import java.util.List;

public class ItemCreationDTO {
    private String nome;
    private Double preco;
    private Long rachaId;
    private Long payerId; // This field was missing or the constructor wasn't updated
    private List<Long> participantesIds;

    // Constructor MUST have 5 arguments
    public ItemCreationDTO(String nome, Double preco, Long rachaId, Long payerId, List<Long> participantesIds) {
        this.nome = nome;
        this.preco = preco;
        this.rachaId = rachaId;
        this.payerId = payerId;
        this.participantesIds = participantesIds;
    }

    // Getters
    public String getNome() { return nome; }
    public Double getPreco() { return preco; }
    public Long getRachaId() { return rachaId; }
    public Long getPayerId() { return payerId; }
    public List<Long> getParticipantesIds() { return participantesIds; }
}