package com.controller;

import com.dao.AdminDAO;
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

@WebServlet("/sistema-filter")
public class LoginServlet extends HttpServlet {

    private static final String AREA_RESTRITA_ALUNO = "/jsp/portal-aluno/home.jsp";
    private static final String AREA_RESTRITA_PROFESSOR = "/jsp/portal-professor/home.jsp";
    private static final String AREA_RESTRITA_ADMIN = "/jsp/portal-admin/alunos.jsp";

    private static final String PAGINA_LOGIN = "/index.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logout(req);
        resp.sendRedirect(req.getContextPath() + PAGINA_LOGIN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        boolean erro = true;
        String destino = PAGINA_ERRO;

        try {
            String email = req.getParameter("email").trim();
            String senha = req.getParameter("senha").trim();

            LoginDTO credenciais = new LoginDTO(email, senha);

            // Login que retorna objeto de professor para futura exibição
            Integer num = login(credenciais);

            switch (num) {
                case 0 -> {
                    AdminDTO admin = encontrarAdmin(credenciais);

                    if (admin == null) {
                        throw ExcecaoDeJSP.falhaLogin();
                    }
                    HttpSession session = req.getSession(true);

                    session.setAttribute("usuario", admin);
                    session.setAttribute("senha", senha);

                    destino = "/admin?action=readAlunos";
                    erro = false;
                }

                case 1 -> {
                    AlunoViewDTO aluno = encontrarAluno(credenciais);
                    HttpSession session = req.getSession(true);

                    List<ObservacaoViewDTO> observacoes = listarObservacoesPorAluno(aluno.getIdAluno());
                    List<BoletimViewDTO> boletim = listarBoletimPorAluno(aluno.getIdAluno());

                    req.setAttribute("observacoes", observacoes);
                    req.setAttribute("boletim", boletim);

                    session.setAttribute("usuario", aluno);
                    destino = AREA_RESTRITA_ALUNO;
                    erro = false;
                }

                case 2 -> {
                    ProfessorDTO professor = encontrarProfessor(credenciais);
                    HttpSession session = req.getSession(true);

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

    private ProfessorDTO encontrarProfessor(LoginDTO credenciais) throws SQLException {
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

    public List<String> notasPendentes(ProfessorDTO professor) throws SQLException {
        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.notasPendentes(professor.getId());
        }
    }

    public List<ObservacaoViewDTO> listarPorProfessor(ProfessorDTO professor) throws SQLException {
        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            return dao.listarPorProfessor(professor.getId());
        }
    }

    // === ADMIN ===
    private AdminDTO encontrarAdmin(LoginDTO credenciais) throws Exception {
        AdminDTO tentativa = new AdminDTO(credenciais.getEmail(), credenciais.getSenha());

        try (AdminDAO adminDAO = new AdminDAO()) {
            boolean logou = adminDAO.logarAdmin(tentativa);

            if (!logou) {
                return null;
            }

            return new AdminDTO(credenciais.getEmail(), null);
        }
    }

    // === LOGOUT ===
    private void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        // Finaliza a sessão do usuário
        if (session != null) {
            session.invalidate();
        }
    }
}