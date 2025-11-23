package com.rachapp.data.model;

import java.util.List;

public class BalanceDTO {
    private Long userId;
    private String nome;
    private Integer avatarId;
    private Double totalPago;
    private Double totalConsumido;
    private Double saldo;
    private List<ItemRacha> itensConsumidos;

    public Long getUserId() { return userId; }
    public String getNome() { return nome; }
    public Integer getAvatarId() { return avatarId; }
    public Double getTotalPago() { return totalPago; }
    public Double getTotalConsumido() { return totalConsumido; }
    public Double getSaldo() { return saldo; }
    public List<ItemRacha> getItensConsumidos() { return itensConsumidos; }
}