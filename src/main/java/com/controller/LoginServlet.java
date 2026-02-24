package com.controller;

import com.dao.LoginDAO;
import com.dto.AlunoViewDTO;
import com.dto.LoginDTO;
import com.dto.ProfessorDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Professor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sistema-filter")
public class LoginServlet extends HttpServlet {

    private static final String AREA_RESTRITA_ALUNO = "/portal-aluno/index.jsp";
    private static final String AREA_RESTRITA_PROFESSOR = "/portal-professor/index.jsp";
    private static final String PAGINA_LOGIN = "jsp/professorLogin.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PAGINA_LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action").trim();
        HttpSession session = req.getSession();

        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "login" -> {
                    String email = req.getParameter("email").trim();
                    String senha = req.getParameter("senha").trim();

                    LoginDTO credenciais = new LoginDTO(email, senha);

                    // Login que retorna objeto de professor para futura exibição
                    Integer num = login(credenciais);

                    switch (num) {
                        case 0 -> throw ExcecaoDeJSP.falhaLogin();

                        case 1 -> {
                            AlunoViewDTO aluno = encontrarAluno(credenciais);
                            session.setAttribute("usuario", aluno);
                            destino = AREA_RESTRITA_ALUNO;
                        }

                        case 2 -> {
                            ProfessorDTO professor = encontrarProfessor(credenciais);
                            session.setAttribute("usuario", professor);
                            destino = AREA_RESTRITA_PROFESSOR;
                        }
                    }
                }

                case "logout" -> {
                    // Logout que encerra a session e redireciona para a página de login
                    logout(req);
                    doGet(req, resp);
                    return;
                }

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }
        }
        // Se houver alguma exceção de JSP (que acontece em execução e com interação do user), aciona o método doGet
        catch (ExcecaoDeJSP e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
            return;

        }
        // Exceções que ocorrem de forma básica
        catch (SQLException e) {
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

    // === LOGOUT ===
    private void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        // Finaliza a sessão do usuário
        if (session != null) {
            session.removeAttribute("usuario");
        }
    }
}
