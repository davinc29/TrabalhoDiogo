package com.controller;

import com.dao.BoletimDAO;
import com.dao.LoginDAO;
import com.dao.ObservacaoDAO;
import com.dto.*;
import com.exception.ExcecaoDeJSP;
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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL_ALUNO = "/jsp/portal-aluno/home.jsp";
    private static final String PAGINA_PRINCIPAL_PROFESSOR = "/jsp/portal-professor/home.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean erro = true;
        String destino = PAGINA_ERRO;

        try {
            HttpSession session = req.getSession(false);
            String usuario = req.getParameter("usuario").trim();

            if (usuario.equals("professor")) {
                ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
                List<ObservacaoViewDTO> observacoes = listarPorProfessor(professor.getId());
                List<String> notasPendentes = notasPendentes(professor.getId());

                req.setAttribute("observacoes", observacoes);
                req.setAttribute("notasPendentes", notasPendentes);

                destino = PAGINA_PRINCIPAL_PROFESSOR;
            } else {
                AlunoViewDTO aluno = (AlunoViewDTO) session.getAttribute("usuario");

                List<ObservacaoViewDTO> observacoes = listarObservacoesPorAluno(aluno.getIdAluno());
                List<BoletimViewDTO> boletim = listarBoletimPorAluno(aluno.getIdAluno());

                req.setAttribute("boletim", boletim);
                req.setAttribute("observacoes", observacoes);

                destino = PAGINA_PRINCIPAL_ALUNO;
            }

            erro = false;
        }
        // Exceções que ocorrem de forma básica
        catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            resp.sendRedirect(req.getContextPath() + destino);
        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
    }

    // === LOGIN ===
    private Integer login(LoginDTO credenciais) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        try (LoginDAO dao = new LoginDAO()) {
            return dao.login(credenciais);
        }
    }

    private ProfessorDTO encontrarProfessor(LoginDTO credenciais) throws SQLException{
        try (LoginDAO dao = new LoginDAO()) {
            return dao.encontrarProfessor(credenciais);
        }
    }

    private AlunoViewDTO encontrarAluno(LoginDTO credenciais) throws SQLException {
        try (LoginDAO dao = new LoginDAO()) {
            return dao.encontrarAluno(credenciais);
        }
    }

    private List<BoletimViewDTO> listarBoletimPorAluno(UUID idAluno) throws SQLException{
        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.listarPorAluno(idAluno, null, null, null, null,null);
        }
    }

    private List<ObservacaoViewDTO> listarObservacoesPorAluno(UUID idAluno) throws SQLException{
        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            return dao.listarPorAluno(idAluno, null, null, null, null);
        }
    }

    public List<String> notasPendentes(UUID idProfessor) throws SQLException{
        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.notasPendentes(idProfessor);
        }
    }

    public List<ObservacaoViewDTO> listarPorProfessor(UUID idProfessor) throws SQLException{
        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            return dao.listarPorProfessor(idProfessor);
        }
    }
}
