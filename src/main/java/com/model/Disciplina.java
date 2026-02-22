package com.model;

import java.util.UUID;

public class Disciplina {
    private Integer idDisciplina;
    private String nome;
    private UUID idProfessor;

    public Disciplina(Integer idDisciplina, String nome, UUID idProfessor) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.idProfessor = idProfessor;
    }

    public Disciplina(String nome, UUID idProfessor) {
        this.nome = nome;
        this.idProfessor = idProfessor;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(UUID idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String toString() {
        return String.format("ID Disciplina: %d\nNome: %s\nID Professor: %s\n", idDisciplina, nome, idProfessor);
    }
}
