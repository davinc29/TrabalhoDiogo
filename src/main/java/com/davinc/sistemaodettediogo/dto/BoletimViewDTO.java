package com.davinc.sistemaodettediogo.dto;

public class BoletimViewDTO {
    private String matricula;
    private String nomeDisciplina;
    private Integer cargaHoraria;
    private Double media;
    private Integer frequencia;
    private String situacao;

    public BoletimViewDTO(String matricula, String nomeDisciplina, Integer cargaHoraria, Double media, Integer frequencia, String situacao) {
        this.matricula = matricula;
        this.nomeDisciplina = nomeDisciplina;
        this.cargaHoraria = cargaHoraria;
        this.media = media;
        this.frequencia = frequencia;
        this.situacao = situacao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public Integer getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Integer frequencia) {
        this.frequencia = frequencia;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return "BoletimViewDTO{" +
                "matricula='" + matricula + '\'' +
                ", nomeDisciplina='" + nomeDisciplina + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", media=" + media +
                ", frequencia=" + frequencia +
                ", situacao='" + situacao + '\'' +
                '}';
    }
}