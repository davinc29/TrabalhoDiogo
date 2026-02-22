package com.controller;

import com.dao.BoletimDAO;
import com.dao.ObservacaoDAO;
import com.dto.BoletimViewDTO;
import com.dto.ObservacaoViewDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/observacao-aluno")
public class ObservacaoAlunoServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "portal-professor/boletim.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean erro = true;

        try {
            String idAluno = req.getParameter("id_aluno").trim();
            UUID idAlunoUuid = UUID.fromString(idAluno);

            List<ObservacaoViewDTO> observacoes = listarPorAluno(idAlunoUuid);
            req.setAttribute("observacoes", observacoes);

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
            req.getRequestDispatcher(PAGINA_PRINCIPAL).forward(req, resp);
        }
    }

    public List<ObservacaoViewDTO> listarPorAluno(UUID idAluno) throws SQLException, ClassNotFoundException{
        try (ObservacaoDAO dao = new ObservacaoDAO()) {

            return dao.listarPorAluno(idAluno);
        }
    }
}
