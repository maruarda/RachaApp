package com.rachapp.data.model;

import java.util.List;

public class ResumoDTO {
    private Double totalA_Receber;
    private Double totalA_Pagar;
    private List<ResumoItemDTO> listaReceber;
    private List<ResumoItemDTO> listaPagar;

    // Getters
    public Double getTotalA_Receber() { return totalA_Receber; }
    public Double getTotalA_Pagar() { return totalA_Pagar; }
    public List<ResumoItemDTO> getListaReceber() { return listaReceber; }
    public List<ResumoItemDTO> getListaPagar() { return listaPagar; }
}