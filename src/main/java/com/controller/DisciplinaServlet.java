package com.controller;

import com.dao.DisciplinaDAO;
import com.dao.ProfessorDAO;
import com.dto.DisciplinaViewDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Disciplina;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/disciplinas")
public class DisciplinaServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL_ADMIN = "admin/disciplina.jsp";
    private static final String PAGINA_PRINCIPAL_ALUNO = "portal-aluno/disciplina.jsp";
    private static final String PAGINA_CADASTRO = "admin/cadastrar-disciplina.jsp";
    private static final String PAGINA_EDICAO = "admin/editar-disciplina.jsp";
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
                    List<DisciplinaViewDTO> disciplinas = listar();

                    req.setAttribute("disciplinas", disciplinas);

                    destino = (usuario.equals("admin") ? PAGINA_PRINCIPAL_ADMIN : PAGINA_PRINCIPAL_ALUNO);
                }

                case "create" -> {
                    Map<String, UUID> mapProfessor = mapNomeId();

                    req.setAttribute("mapProfessor", mapProfessor);

                    destino = PAGINA_CADASTRO;
                }

                case "update" -> {
                    Map<String, UUID> mapProfessor = mapNomeId();
                    Disciplina disciplina = pesquisarPorId(req);

                    req.setAttribute("mapProfessor", mapProfessor);
                    req.setAttribute("disciplina", disciplina);

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

    private static void cadastrar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_professor").trim();
        UUID idProfessor = UUID.fromString(temp);

        String nome = req.getParameter("nome");

        Disciplina disciplina = new Disciplina(nome, idProfessor);

        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            dao.cadastrar(disciplina);
        }
    }

    private static void atualizar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_disciplina").trim();
        Integer idDisciplina = Integer.parseInt(temp);

        temp = req.getParameter("id_professor").trim();
        UUID idProfessor = UUID.fromString(temp);

        String nome = req.getParameter("nome");

        Disciplina disciplinaAlterado = new Disciplina(idDisciplina, nome, idProfessor);

        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            Disciplina disciplinaOriginal = dao.pesquisarPorId(idDisciplina);

            dao.atualizar(disciplinaOriginal, disciplinaAlterado);
        }
    }

    private static void deletar(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_disciplina");
        Integer idDisciplina = Integer.parseInt(temp.trim());

        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            dao.deletar(idDisciplina);
        }
    }

    private static Disciplina pesquisarPorId(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_disciplina");
        Integer idDisciplina = Integer.parseInt(temp.trim());

        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            return dao.pesquisarPorId(idDisciplina);
        }
    }

    private static Map<String, UUID> mapNomeId() throws SQLException{
        try (ProfessorDAO dao = new ProfessorDAO()) {
            return dao.mapNomeId();
        }
    }

    private static List<DisciplinaViewDTO> listar() throws SQLException{
        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            return dao.listar();
        }
    }
}
