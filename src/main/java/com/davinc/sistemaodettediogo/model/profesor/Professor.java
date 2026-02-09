package com.davinc.sistemaodettediogo.model.profesor;

public class Professor {
    String nome;
    String disciplina;
    String username;
    String senha;

    public Professor(String nome, String disciplina, String username, String senha) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.username = username;
        this.senha = senha;
    }

    public Professor() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String toString() {
        return String.format("""
                Professor:
                Nome: %s
                Disciplina: %s
                Username: %s
                Senha: %s""", nome, disciplina, username, senha);
    }
}
