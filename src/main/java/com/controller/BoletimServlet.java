package com.controller;

import com.dao.AlunoDAO;
import com.dao.BoletimDAO;
import com.dao.DisciplinaDAO;
import com.dto.AlunoViewDTO;
import com.dto.BoletimViewDTO;
import com.dto.ProfessorDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Boletim;
import com.model.Disciplina;
import com.model.Professor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/boletim")
public class BoletimServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL_PROFESSOR = "/jsp/portal-professor/notas-adicionar.jsp";
    private static final String PAGINA_CADASTRO = "/jsp/portal-professor/notas-cadastro.jsp";
    private static final String PAGINA_EDICAO = "/jsp/portal-professor/notas-editar.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");

        String action = req.getParameter("action").trim();

        boolean erro = true;
        String destino = null;

        try {
            Map<String, Integer> mapNomeIdProfessor = mapNomeIdProfessor(professor.getId());

            switch (action) {
                case "read" -> {
                    String idAluno = req.getParameter("id_aluno");
                    List<BoletimViewDTO> boletins = new ArrayList<>();
                    AlunoViewDTO aluno = null;

                    if (!idAluno.isEmpty()) {
                        idAluno = idAluno.trim();
                        UUID idAlunoUuid = UUID.fromString(idAluno);

                        boletins = listarPorAluno(idAlunoUuid, req);
                        aluno = listarAlunoPorId(req);
                    }

                    req.setAttribute("mapNomeIdProfessor", mapNomeIdProfessor);
                    req.setAttribute("aluno", aluno);
                    req.setAttribute("boletins", boletins);

                    destino = PAGINA_PRINCIPAL_PROFESSOR;
                    erro = false;
                }
                case "create" -> {
                    AlunoViewDTO aluno = listarAlunoPorId(req);

                    req.setAttribute("mapNomeIdProfessor", mapNomeIdProfessor);
                    req.setAttribute("aluno", aluno);

                    destino = PAGINA_CADASTRO;
                }

                case "update" -> {
                    AlunoViewDTO aluno = listarAlunoPorId(req);
                    Boletim boletim = pesquisarPorId(req);

                    req.setAttribute("mapNomeIdProfessor", mapNomeIdProfessor);
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
        HttpSession session = req.getSession(false);
        String action = req.getParameter("action").trim();

        boolean erro = true;
        String destino = PAGINA_ERRO;

        try {

            switch (action) {
                case "read" -> {
                    ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
                    String idAluno = req.getParameter("id_aluno");
                    List<BoletimViewDTO> boletins = new ArrayList<>();
                    Map<String, Integer> mapNomeIdProfessor = mapNomeIdProfessor(professor.getId());
                    AlunoViewDTO aluno = null;

                    if (!idAluno.isEmpty()) {
                        idAluno = idAluno.trim();
                        UUID idAlunoUuid = UUID.fromString(idAluno);

                        boletins = listarPorAluno(idAlunoUuid, req);
                        aluno = listarAlunoPorId(req);
                    }

                    req.setAttribute("mapNomeIdProfessor", mapNomeIdProfessor);
                    req.setAttribute("aluno", aluno);
                    req.setAttribute("boletins", boletins);

                    destino = PAGINA_PRINCIPAL_PROFESSOR;
                    erro = false;
                }
                case "create" -> {
                    cadastrar(req);
                    String idAluno = req.getParameter("id_aluno");
                    destino = "/boletim?action=read&id_aluno="+idAluno;
                }
                case "update" -> {
                    atualizar(req);
                    String idAluno = req.getParameter("id_aluno");
                    destino = "/boletim?action=read&id_aluno="+idAluno;
                }
                case "delete" -> {
                    deletar(req);
                    String idAluno = req.getParameter("id_aluno");
                    destino = "/boletim?action=read&id_aluno="+idAluno;
                }
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

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

        if (erro || !action.equals("read")) {
            resp.sendRedirect(req.getContextPath() + destino);
        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
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

        Boletim boletim = new Boletim(nota1, nota2, idAluno, idDisciplina);

        try (BoletimDAO dao = new BoletimDAO()) {
            dao.cadastrar(boletim);
        }
    }

    public void atualizar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_boletim").trim();
        Integer idBoletim = Integer.parseInt(temp);

        temp = req.getParameter("id_aluno").trim();
        UUID idAluno = UUID.fromString(temp);

        temp = req.getParameter("id_disciplina").trim();
        Integer idDisciplina = Integer.parseInt(temp);

        temp = req.getParameter("nota1");
        Double nota1 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        temp = req.getParameter("nota2");
        Double nota2 = (temp != null && !temp.isBlank() ? Double.parseDouble(temp.trim()) : null);

        Boletim boletimAlterado = new Boletim(nota1, nota2, idAluno, idDisciplina);

        try (BoletimDAO dao = new BoletimDAO()) {
            Boletim boletimOriginal = dao.pesquisarPorId(idBoletim);

            dao.atualizar(boletimOriginal, boletimAlterado);
        }
    }

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno, HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_boletim");
        Integer idBoletim = (temp == null || temp.isBlank() ? null : Integer.parseInt(temp.trim()));

        temp = req.getParameter("nota1");
        Double nota1 = (temp == null || temp.isBlank() ? null : Double.parseDouble(temp.trim()));

        temp = req.getParameter("nota2");
        Double nota2 = (temp == null || temp.isBlank() ? null : Double.parseDouble(temp.trim()));

        temp = req.getParameter("media");
        Double media = (temp == null || temp.isBlank() ? null : Double.parseDouble(temp.trim()));

        temp = req.getParameter("nome_disciplina");
        String nomeDisciplina = (temp == null || temp.isBlank() ? null : temp.trim());

        try (BoletimDAO dao = new BoletimDAO()) {

            return dao.listarPorAluno(idAluno, idBoletim, nota1, nota2, media, nomeDisciplina);
        }
    }

    public void deletar(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_boletim").trim();
        Integer idBoletim = Integer.parseInt(temp);

        try (BoletimDAO dao = new BoletimDAO()) {
            dao.deletar(idBoletim);
        }
    }

    public Boletim pesquisarPorId(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_boletim").trim();
        Integer idBoletim = Integer.parseInt(temp);

        try (BoletimDAO dao = new BoletimDAO()) {
            return dao.pesquisarPorId(idBoletim);
        }
    }

    public AlunoViewDTO listarAlunoPorId(HttpServletRequest req) throws SQLException{
        String idAluno = req.getParameter("id_aluno");
        UUID idAlunoUuid = UUID.fromString(idAluno.trim());

        try (AlunoDAO dao = new AlunoDAO()) {
            return dao.listarAlunoPorId(idAlunoUuid);
        }
    }

    public Map<String, Integer> mapNomeIdProfessor(UUID idProfessor) throws SQLException{
        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            return dao.mapNomeIdProfessor(idProfessor);

        }
    }
}
