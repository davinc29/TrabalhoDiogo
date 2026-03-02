package com.model;

import java.util.UUID;

public class Boletim {
    private Integer id;
    private Double nota1;
    private Double nota2;
    private Double media;
    private UUID idAluno;
    private Integer idDisciplina;

    public Boletim(Integer idBoletim, Double nota1, Double nota2, Double media, UUID idAluno, Integer idDisciplina) {
        this.id = idBoletim;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.media = media;
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
    }

    public Boletim(Double nota1, Double nota2, UUID idAluno, Integer idDisciplina) {
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
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

    public String toString() {
        return String.format("ID Boletim: %d\nNota 1: %.2f\n Nota2: %.2f\nMédia: %.2f\nID Aluno: %s\nID Disciplina: %d\n", id, nota1, nota2, media, idAluno, idDisciplina);
    }
}
