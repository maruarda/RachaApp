package com.rachapp.data.model;

public class PagamentoDTO {
    private Double valor;
    private RachaObj racha;
    private UserObj devedor;
    private UserObj credor;

    public PagamentoDTO(Double valor, Long rachaId, Long devedorId, Long credorId) {
        this.valor = valor;
        this.racha = new RachaObj(rachaId);
        this.devedor = new UserObj(devedorId);
        this.credor = new UserObj(credorId);
    }

    private static class RachaObj {
        Long idRacha;
        RachaObj(Long id) { idRacha = id; }
    }
    private static class UserObj {
        Long idUsuario;
        UserObj(Long id) { idUsuario = id; }
    }
}