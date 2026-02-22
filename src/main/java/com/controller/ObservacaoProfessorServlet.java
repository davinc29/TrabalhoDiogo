package com.controller;

import com.dao.ObservacaoDAO;
import com.dto.ObservacaoViewDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Observacao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/observacao-professor")
public class ObservacaoProfessorServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "portal-professor/observacao.jsp";
    private static final String PAGINA_CADASTRO = "portal-professor/cadastrar-observacao.jsp";
    private static final String PAGINA_EDICAO = "jsp/editar-observacao.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        action = (action == null ? "read" : action.trim());

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> {
                    String idAluno = req.getParameter("id_aluno");
                    String turmaAno = req.getParameter("turma_ano");
                    String nomeDisciplina = req.getParameter("nome_disciplina");
                    List<ObservacaoViewDTO> observacoes = new ArrayList<>();

                    if (!idAluno.isEmpty()) {
                        idAluno = idAluno.trim();
                        UUID idAlunoUuid = UUID.fromString(idAluno);

                        observacoes = listarPorAluno(idAlunoUuid);
                    } else if (!turmaAno.isEmpty() && !nomeDisciplina.isEmpty()) {
                        turmaAno = turmaAno.trim();
                        nomeDisciplina = nomeDisciplina.trim();

                        observacoes = listarPorSala(turmaAno, nomeDisciplina);
                    }

                    req.setAttribute("observacoes", observacoes);
                    destino = PAGINA_PRINCIPAL;
                }

                case "create" -> {
                    destino = PAGINA_CADASTRO;
                }

                case "update" -> {

                    Observacao observacao = pesquisarPorId(req);
                    req.setAttribute("observacao", observacao);

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

        temp = req.getParameter("texto_observacao");
        String textoObservacao = (temp != null && !temp.isBlank() ? temp.trim() : null);

        Observacao observacao = new Observacao(textoObservacao, idAluno, idDisciplina);

        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            dao.cadastrar(observacao);
        }
    }

    public void atualizar(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP{
        String temp = req.getParameter("id_observacao").trim();
        Integer idObservacao = Integer.parseInt(temp);

        temp = req.getParameter("id_aluno").trim();
        UUID idAluno = UUID.fromString(temp);

        temp = req.getParameter("id_disciplina").trim();
        Integer idDisciplina = Integer.parseInt(temp);

        temp = req.getParameter("texto_observacao");
        String textoObservacao = (temp != null && !temp.isBlank() ? temp.trim() : null);

        Observacao observacaoAlterado = new Observacao(idObservacao, textoObservacao, idAluno, idDisciplina);

        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            Observacao observacaoOriginal = dao.pesquisarPorId(idObservacao);

            dao.atualizar(observacaoOriginal, observacaoAlterado);
        }
    }

    public List<ObservacaoViewDTO> listarPorAluno(UUID idAluno) throws SQLException, ClassNotFoundException{
        try (ObservacaoDAO dao = new ObservacaoDAO()) {

            return dao.listarPorAluno(idAluno);
        }
    }

    public List<ObservacaoViewDTO> listarPorSala(String turmaAno, String nomeDisiciplina) throws SQLException, ClassNotFoundException{
        try(ObservacaoDAO dao = new ObservacaoDAO()) {

            return dao.listarPorSala(turmaAno, nomeDisiciplina);
        }

    }

    public void deletar(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_observacao");
        Integer idObservacao = Integer.parseInt(temp.trim());

        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            dao.deletar(idObservacao);
        }
    }

    public Observacao pesquisarPorId(HttpServletRequest req) throws SQLException, ClassNotFoundException{
        String temp = req.getParameter("id_observacao");
        Integer idObservacao = Integer.parseInt(temp.trim());

        try (ObservacaoDAO dao = new ObservacaoDAO()) {
            return dao.pesquisarPorId(idObservacao);
        }
    }
}
