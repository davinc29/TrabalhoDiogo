package com.controller;

import com.dao.AlunoDAO;
import com.dto.AlunoViewDTO;
import com.dto.ProfessorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/alunos-professor")
public class AlunoProfessorServlet extends HttpServlet {

    private static final String PAGINA_NOTAS = "/jsp/portal-professor/notas.jsp";
    private static final String PAGINA_OBSERVACOES = "/jsp/portal-professor/observacoes.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action").trim();
        String destino = PAGINA_ERRO;
        boolean erro = true;

        try {
            List<AlunoViewDTO> alunos = listaAlunos(req);

            req.setAttribute("alunos", alunos);
            erro = false;
            destino = (action.equals("notas") ? PAGINA_NOTAS : PAGINA_OBSERVACOES);

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);

        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
    }

    public List<AlunoViewDTO> listaAlunos(HttpServletRequest req) throws SQLException{
        String temp = req.getParameter("nome");
        String nome = (temp == null || temp.isBlank() ? null : temp.trim());

        temp = req.getParameter("matricula");
        String matricula = (temp == null || temp.isBlank() ? null : temp.trim());

        temp = req.getParameter("turmaAno");
        String turmaAno = (temp == null || temp.isBlank() ? null : temp.trim());

        try (AlunoDAO dao = new AlunoDAO()) {
            return dao.listarAlunos(nome, matricula, turmaAno);
        }
     }
}
