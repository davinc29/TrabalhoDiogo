package com.dto;

public class DisciplinaViewDTO {

    private Integer id;
    private String nomeDisciplina;
    private String nomeProfessor;
    private String emailProfessor;

    public DisciplinaViewDTO(Integer id, String nomeDisciplina, String nomeProfessor, String emailProfessor) {
        this.id = id;
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getEmailProfessor() {
        return emailProfessor;
    }

    public void setEmailProfessor(String emailProfessor) {
        this.emailProfessor = emailProfessor;
    }

    @Override
    public String toString() {
        return "DisciplinaViewDTO{" +
                "id=" + id +
                ", nomeDisciplina='" + nomeDisciplina + '\'' +
                ", nomeProfessor='" + nomeProfessor + '\'' +
                ", emailProfessor='" + emailProfessor + '\'' +
                '}';
    }
}
