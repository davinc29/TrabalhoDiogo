package com.dto;

public class ObservacaoViewDTO {

    private String nomeDisciplina;
    private String nomeProfessor;
    private String observacao;

    public ObservacaoViewDTO(String nomeDisciplina, String nomeProfessor, String observacao) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.observacao = observacao;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "ObservacoesViewDTO{" +
                "nomeDisciplina='" + nomeDisciplina + '\'' +
                ", nomeProfessor='" + nomeProfessor + '\'' +
                ", observacao='" + observacao + '\'' +
                '}';
    }
}
