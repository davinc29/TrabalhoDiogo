package com.dto;

import java.util.UUID;

public class AlunoViewDTO {
    private UUID idAluno;
    private String nome;
    private String matricula;
    private String email;
    private String turma_ano;

    public AlunoViewDTO(UUID idAluno, String nome, String matricula, String email, String turma_ano) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.turma_ano = turma_ano;
    }

    public AlunoViewDTO() {}

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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTurma_ano() {
        return turma_ano;
    }

    public void setTurma_ano(String turma_ano) {
        this.turma_ano = turma_ano;
    }

    @Override
    public String toString() {
        return "AlunoViewDTO{" +
                "idAluno=" + idAluno +
                ", nome='" + nome + '\'' +
                ", matricula=" + matricula +
                ", email='" + email + '\'' +
                ", turma_ano='" + turma_ano + '\'' +
                '}';
    }
}
