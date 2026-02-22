package com.dto;

import java.util.UUID;

public class ProfessorDTO {
    private UUID id;
    private String nome;
    private String username;
    private String email;

    public ProfessorDTO(UUID id, String nome, String username, String email) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
                ", id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
