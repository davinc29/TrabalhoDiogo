package com.dto;

public class ObservacaoViewDTO {

    private String nomeAluno;
    private String turmaAno;
    private String nomeDisciplina;
    private String nomeProfessor;
    private String observacao;

    public ObservacaoViewDTO(String nomeAluno, String turmaAno, String nomeDisciplina, String nomeProfessor, String observacao) {
        this.turmaAno = turmaAno;
        this.nomeAluno = nomeAluno;
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.observacao = observacao;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTurmaAno() {
        return turmaAno;
    }

    public void setTurmaAno(String turmaAno) {
        this.turmaAno = turmaAno;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "ObservacaoViewDTO{" +
                "nomeAluno='" + nomeAluno + '\'' +
                ", turmaAno='" + turmaAno + '\'' +
                ", nomeDisciplina='" + nomeDisciplina + '\'' +
                ", nomeProfessor='" + nomeProfessor + '\'' +
                ", observacao='" + observacao + '\'' +
                '}';
    }
}
