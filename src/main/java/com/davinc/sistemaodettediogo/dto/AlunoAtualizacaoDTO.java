package com.davinc.sistemaodettediogo.dto;

import java.util.UUID;

public class AlunoAtualizacaoDTO {
    private UUID idAluno;
    private String nome;
    private String email;
    private String senha;

    public AlunoAtualizacaoDTO(UUID idAluno, String nome, String email, String senha) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "AlunoAtualizacaoDTO{" +
                "idAluno=" + idAluno +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
