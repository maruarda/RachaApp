package com.rachapp.data.model;

import java.util.List;

public class Racha {
    private Long idRacha;
    private String nome;
    private String localNome;
    private Double latitude;
    private Double longitude;
    private String status;
    private Long ownerId; // NEW: Send owner ID to backend
    private List<ItemRacha> itens;

    public Racha() {}

    public Racha(String nome, String localNome, Double latitude, Double longitude, Long ownerId) {
        this.nome = nome;
        this.localNome = localNome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerId = ownerId;
    }

    public Long getIdRacha() { return idRacha; }
    public String getNome() { return nome; }
    public String getLocalNome() { return localNome; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getStatus() { return status; }
    public Long getOwnerId() { return ownerId; }
    public List<ItemRacha> getItens() { return itens; }
}