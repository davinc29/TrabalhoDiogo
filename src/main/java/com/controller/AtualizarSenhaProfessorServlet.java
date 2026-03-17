package com.controller;

import com.dao.ProfessorDAO;
import com.exception.ExcecaoDeJSP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/professores")
public class AtualizarSenhaProfessorServlet extends HttpServlet {

    private static final String CONTA_PROFESSOR = "/jsp/portal-professor/conta.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String destino = PAGINA_ERRO;

        try {
            atualizarSenha(req);
            destino = CONTA_PROFESSOR;
        }
        // Se houver alguma exceção de JSP, aciona o método doGet
        catch (ExcecaoDeJSP e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
            return;

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (ClassNotFoundException e) {
            System.err.println("Falha ao carregar o driver postgresql:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        resp.sendRedirect(req.getContextPath() + destino);
    }

    public void atualizarSenha(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String email = req.getParameter("email").trim();

        String senhaAtual = req.getParameter("senha_atual");
        senhaAtual = (senhaAtual.isBlank() ? null : senhaAtual.trim());

        String novaSenha = req.getParameter("nova_senha");
        novaSenha = (novaSenha.isBlank() ? null : novaSenha.trim());

        try (ProfessorDAO dao = new ProfessorDAO()) {
            dao.atualizarSenhaProfessor(email, senhaAtual, novaSenha);
        }
    }
}