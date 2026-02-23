package com.controller;

import com.dao.AlunoDAO;
import com.dao.BoletimDAO;
import com.dto.AlunoViewDTO;
import com.dto.BoletimViewDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Boletim;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.IDN;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/boletim-professor")
public class BoletimProfessorServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL_PROFESSOR = "portal-professor/boletim.jsp";
    private static final String PAGINA_PRINCIPAL_ALUNO = "portal-professor/boletim.jsp";
    private static final String PAGINA_CADASTRO = "portal-professor/cadastrar-boletim.jsp";
    private static final String PAGINA_EDICAO = "jsp/editar-boletim.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String usuario = req.getParameter("usuario").trim();

        action = (action == null ? "read" : action.trim());

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> {
                    String idAluno = req.getParameter("id_aluno");
                    List<BoletimViewDTO> boletins = new ArrayList<>();

                    if (!idAluno.isEmpty()) {
                        idAluno = idAluno.trim();
                        UUID idAlunoUuid = UUID.fromString(idAluno);

                        boletins = listarPorAluno(idAlunoUuid);
                    }

                    req.setAttribute("boletins", boletins);

                    destino = (usuario.equals("professor") ? PAGINA_PRINCIPAL_PROFESSOR : PAGINA_PRINCIPAL_ALUNO);
                }

                case "create" -> {
                    AlunoViewDTO aluno = listarAlunoPorId(req);

                    req.setAttribute("aluno", aluno);

                    destino = PAGINA_CADASTRO;
                }

                case "update" -> {
                    AlunoViewDTO aluno = listarAlunoPorId(req);
                    Boletim boletim = pesquisarPorId(req);

                    req.setAttribute("aluno", aluno);
                    req.setAttribute("boletim", boletim);

                    destino = PAGINA_EDICAO;
                }
            }

            erro = false;
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
                case "create" -> cadastrar(req);
                case "update" -> atualizar(req);
                case "delete" -> deletar(req);
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = req.getServletPath();

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

    public void cadastrar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_aluno").trim();
        UUID idAluno = UUID.fromString(temp);

        temp = req.getParameter("id_disciplina").trim();
        Integer idDisciplina = Integer.parseInt(temp);

        temp = req.getParameter("nota1");
        Double nota1 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        temp = req.getParameter("nota2");
        Double nota2 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        Double media = (nota1 == null || nota2 == null ? null : (nota1+nota2)/2);

        Boletim boletim = new Boletim(nota1, nota2, media, idAluno, idDisciplina);

        try (BoletimDAO dao = new BoletimDAO()) {
            dao.cadastrar(boletim);
        }
    }

    public void atualizar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_boletim").trim();
        UUID idBoletim = UUID.fromString(temp);

        temp = req.getParameter("id_aluno").trim();
        UUID idAluno = UUID.fromString(temp);

        temp = req.getParameter("id_disciplina").trim();
        Integer idDisciplina = Integer.parseInt(temp);

        temp = req.getParameter("nota1");
        Double nota1 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        temp = req.getParameter("nota2");
        Double nota2 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        Double media = (nota1 == null || nota2 == null ? null : (nota1+nota2)/2);

        Boletim boletimAlterado = new Boletim(idBoletim, nota1, nota2, media, idAluno, idDisciplina);

        try (BoletimDAO dao = new BoletimDAO()) {
            Boletim boletimOriginal = dao.pesquisarPorId(idBoletim);

            dao.atualizar(boletimOriginal, boletimAlterado);
        }
    }

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno) throws SQLException, ClassNotFoundException{
        try (BoletimDAO dao = new BoletimDAO()) {

            return dao.listarPorAluno(idAluno);
        }
    }

    public void deletar(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String idBoletim = req.getParameter("id_boletim");
        UUID idBoletimUuid = UUID.fromString(idBoletim.trim());

        try (BoletimDAO dao = new BoletimDAO()) {
            dao.deletar(idBoletimUuid);
        }
    }

    public Boletim pesquisarPorId(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String idBoletim = req.getParameter("id_boletim");
        UUID idBoletimUuid = UUID.fromString(idBoletim.trim());

        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.pesquisarPorId(idBoletimUuid);
        }
    }

    public AlunoViewDTO listarAlunoPorId(HttpServletRequest req) throws SQLException{
        String idAluno = req.getParameter("id_aluno");
        UUID idAlunoUuid = UUID.fromString(idAluno.trim());

        try (AlunoDAO dao = new AlunoDAO()) {
            return dao.listarAlunoPorId(idAlunoUuid);
        }
    }
}
