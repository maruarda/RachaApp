package com.rachapp.data.model;

public class ResumoItemDTO {
    private String nomePessoa;
    private String nomeRacha;
    private Double valor;
    private Integer avatarId;
    private Long userId;  // ID of the person (Debtor/Creditor)
    private Long rachaId; // ID of the Racha

    public String getNomePessoa() { return nomePessoa; }
    public String getNomeRacha() { return nomeRacha; }
    public Double getValor() { return valor; }
    public Integer getAvatarId() { return avatarId; }
    public Long getUserId() { return userId; }
    public Long getRachaId() { return rachaId; }
}