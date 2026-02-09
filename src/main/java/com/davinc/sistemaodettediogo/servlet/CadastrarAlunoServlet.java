package com.davinc.sistemaodettediogo.servlet;

import com.davinc.sistemaodettediogo.model.aluno.Aluno;
import com.davinc.sistemaodettediogo.model.aluno.AlunoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CadastrarAlunoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeCompleto = request.getParameter("nome");
        String matricula = request.getParameter("matricula");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Aluno aluno = new Aluno(nomeCompleto, Long.parseLong(matricula), email, senha);

        AlunoDAO alunoDAO = new AlunoDAO();

        boolean sucesso = alunoDAO.CadastrarAluno(aluno);

        if (sucesso) {
            //pagina de sucesso
        } else {
            //pagina de erro
        }


    }
}
