package com.davinc.sistemaodettediogo.dto;

import java.util.UUID;

public class ProfessorDTO {
    private UUID idProfessor;
    private String nome;
    private String username;

    public ProfessorDTO(UUID idProfessor, String nome, String username) {
        this.idProfessor = idProfessor;
        this.nome = nome;
        this.username = username;
    }

    public UUID getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(UUID idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
                "idProfessor=" + idProfessor +
                ", nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
