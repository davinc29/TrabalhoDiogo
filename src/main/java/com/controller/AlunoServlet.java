package com.controller;

import com.dao.AlunoDAO;
import com.dto.AlunoCadastrarDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/aluno")
public class AlunoServlet extends HttpServlet {

    // Páginas / destinos
    private static final String PAGINA_LOGIN = "/jsp/login.jsp";
    private static final String AREA_RESTRITA_ALUNO = "/jsp/portal-aluno/home.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action").trim();

        boolean erro = true;
        String destino = null;

        try {
            // Estrutura padrão (igual seu modelo). Você pode plugar páginas aqui se quiser.
            if (action.equals("read")) {
                destino = AREA_RESTRITA_ALUNO;
            } else {
                throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
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
                case "create" -> cadastrar(req); // era /cadastrar-aluno
                case "update" -> atualizar(req); // era /atualizar-aluno
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            // Mantém a mesma “ideia” do seu modelo: após o POST redireciona
            // (cada método define seu destino lógico)
            destino = (String) req.getAttribute("destinoFinal");

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        resp.sendRedirect(req.getContextPath() + destino);
    }

    // =========================
    // MESMA LÓGICA (SEM MUDAR)
    // =========================

    private void cadastrar(HttpServletRequest req) throws SQLException {
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        String turmaAno = String.valueOf(java.time.Year.now().getValue());

        // Mantive igual ao seu cadastrar: cria DTO, cria DAO e chama cadastrarAluno()
        AlunoCadastrarDTO alunoCadastrarDTO = new AlunoCadastrarDTO(nome, null, email, senha);

        AlunoDAO alunoDAO = new AlunoDAO();
        alunoDAO.cadastrarAluno(alunoCadastrarDTO);

        // mesmo destino do original: volta para login
        req.setAttribute("destinoFinal", PAGINA_LOGIN);
    }

    private void atualizar(HttpServletRequest req) throws SQLException {
        HttpSession session = req.getSession();

        String email = req.getParameter("email").trim();

        String senhaAtual = req.getParameter("senha_atual");
        senhaAtual = (senhaAtual.isBlank() ? null : senhaAtual.trim());

        String novaSenha = req.getParameter("nova_senha");
        novaSenha = (novaSenha.isBlank() ? null : novaSenha.trim());

        try (AlunoDAO dao = new AlunoDAO()) {
            dao.atualizarSenhaPortalAluno(email, senhaAtual, novaSenha);
        }

        req.setAttribute("destinoFinal", AREA_RESTRITA_ALUNO);
    }
}