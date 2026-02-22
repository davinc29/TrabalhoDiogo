package com.model;

import java.util.UUID;

public class Observacao {

    private Integer id;
    private String textoObservacao;
    private UUID idAluno;
    private Integer idDisciplina;

    public Observacao(Integer id, String textoObservacao, UUID idAluno, Integer idDisciplina) {
        this.id = id;
        this.textoObservacao = textoObservacao;
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
    }

    public Observacao(String textoObservacao, UUID idAluno, Integer idDisciplina) {
        this.textoObservacao = textoObservacao;
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTextoObservacao() {
        return textoObservacao;
    }

    public void setTextoObservacao(String textoObservacao) {
        this.textoObservacao = textoObservacao;
    }

    public UUID getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(UUID idAluno) {
        this.idAluno = idAluno;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    @Override
    public String toString() {
        return "Observacao{" +
                "id=" + id +
                ", textoObservacao='" + textoObservacao + '\'' +
                ", idAluno=" + idAluno +
                ", idDisciplina=" + idDisciplina +
                '}';
    }
}
