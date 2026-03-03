package com.controller;

import com.dao.AdminDAO;
import com.dao.AlunoDAO;
import com.dto.AdminDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private static final String AREA_RESTRITA_ADMIN = "/portal-admin/index.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action").trim();

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> destino = AREA_RESTRITA_ADMIN;

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            erro = false;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action").trim();

        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "login" -> logarAdmin(req);
                case "updateAluno" -> atualizarAlunoAdmin(req);

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = AREA_RESTRITA_ADMIN;

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        resp.sendRedirect(req.getContextPath() + destino);
    }


    private void logarAdmin(HttpServletRequest req) throws Exception {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        AdminDTO adminDTO = new AdminDTO(email, senha);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.logarAdmin(adminDTO);
    }

    private void atualizarAlunoAdmin(HttpServletRequest req) throws SQLException {
        HttpSession session = req.getSession();

        String nomeAluno = req.getParameter("nome").trim();
        String emailAluno = req.getParameter("email").trim();
        String senhaAluno = req.getParameter("senha").trim();

        UUID idAluno = (UUID) session.getAttribute("idAluno");

        AlunoDAO alunoDAO = new AlunoDAO();

        alunoDAO.atualizarEmailAluno(idAluno, emailAluno);
        alunoDAO.atualizarSenhaAluno(idAluno, senhaAluno);
        alunoDAO.atualizarNomeAluno(idAluno, nomeAluno);

        session.setAttribute("emailAluno", emailAluno);
        session.setAttribute("senhaAluno", senhaAluno);
    }
}