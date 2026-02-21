package com.dto;

public class AdminDTO {
    private String nome;
    private String email;
    private String senha;

    public AdminDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public AdminDTO() {}


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
         return "AdminDTO{" +
                 " email='" + email + '\'' +
                 ", senha='" + senha + '\'' +
                 '}';
     }
}
