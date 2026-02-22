package com.model;

import java.util.UUID;

public class PreMatricula {
    private String matricula;
    private String turmaAno;

    public PreMatricula(String matricula, String turmaAno) {
        this.matricula = matricula;
        this.turmaAno = turmaAno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTurmaAno() {
        return turmaAno;
    }

    public void setTurmaAno(String turmaAno) {
        this.turmaAno = turmaAno;
    }

    public String toString() {
        return String.format("Matricula: %s\nTurma e Ano: %s\n", matricula, turmaAno);
    }
}
