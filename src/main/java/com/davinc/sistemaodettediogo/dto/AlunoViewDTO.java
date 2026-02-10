package com.davinc.sistemaodettediogo.dto;

import java.util.UUID;

public class AlunoViewDTO {
    private UUID idAluno;
    private String nome;
    private Long matricula;
    private String email;

    public AlunoViewDTO(UUID idAluno, String nome, Long matricula, String email) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
    }

    public UUID getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(UUID idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AlunoViewDTO{" +
                "idAluno=" + idAluno +
                ", nome='" + nome + '\'' +
                ", matricula=" + matricula +
                ", email='" + email + '\'' +
                '}';
    }
}
