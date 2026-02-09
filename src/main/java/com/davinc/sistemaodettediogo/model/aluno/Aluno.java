package com.davinc.sistemaodettediogo.model.aluno;

public class Aluno {
    String nomeCompleto;
    Long matricula;
    String email;
    String senha;

    public Aluno(String nomeCompleto, Long matricula, String email, String senha) {
        this.nomeCompleto = nomeCompleto;
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
    }

    public Aluno(){}

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String toString() {
        return String.format("""
                Aluno:
                Nome Completo: %s
                Matrícula: %d
                Email: %s
                Senha: %s""", nomeCompleto, matricula, email, senha);
    }
}
