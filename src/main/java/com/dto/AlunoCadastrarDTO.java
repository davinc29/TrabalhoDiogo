package com.dto;

public class AlunoCadastrarDTO {
    private String nome;
    private Long matricula;
    private String email;
    private String senha;

    public AlunoCadastrarDTO(String nome, Long matricula, String email, String senha) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
    }

    public AlunoCadastrarDTO() {}

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return String.format("""
                Nome: %s
                Matrícula: %d
                Email: %s
                """, nome, matricula, email);
    }
}
