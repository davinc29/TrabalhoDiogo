package com.controller;

import com.dao.BoletimDAO;
import com.dao.LoginDAO;
import com.dao.ObservacaoDAO;
import com.dto.AlunoViewDTO;
import com.dto.LoginDTO;
import com.dto.ObservacaoViewDTO;
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
import java.net.http.HttpRequest;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sistema-filter")
public class LoginServlet extends HttpServlet {

    private static final String AREA_RESTRITA_ALUNO = "/jsp/portal-aluno/home.jsp";
    private static final String AREA_RESTRITA_PROFESSOR = "/jsp/portal-professor/home.jsp";
    private static final String PAGINA_LOGIN = "/index.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PAGINA_LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action").trim();
        HttpSession session = req.getSession(true);

        boolean erro = true;
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
                            erro = false;
                        }

                        case 2 -> {
                            ProfessorDTO professor = encontrarProfessor(credenciais);
                            List<ObservacaoViewDTO> observacoes = listarPorProfessor(professor);
                            List<String> notasPendentes = notasPendentes(professor);

                            req.setAttribute("observacoes", observacoes);
                            req.setAttribute("notasPendentes", notasPendentes);
                            session.setAttribute("usuario", professor);

                            destino = AREA_RESTRITA_PROFESSOR;
                            erro = false;
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

    public List<String> notasPendentes(ProfessorDTO professor) throws SQLException{
        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.notasPendentes(professor.getId());
        }
    }

    public List<ObservacaoViewDTO> listarPorProfessor(ProfessorDTO professor) throws SQLException{
        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            return dao.listarPorProfessor(professor.getId());
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
