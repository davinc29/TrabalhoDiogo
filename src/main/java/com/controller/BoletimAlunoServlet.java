package com.controller;

import com.dao.BoletimDAO;
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

@WebServlet("/boletim")
public class BoletimAlunoServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "portal-aluno/boletim.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean erro = true;

        try {
            String idAluno = req.getParameter("id_aluno").trim();
            UUID idAlunoUuid = UUID.fromString(idAluno);

            List<BoletimViewDTO> boletins = listarPorAluno(idAlunoUuid);
            req.setAttribute("boletins", boletins);

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

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno) throws SQLException, ClassNotFoundException{
        try (BoletimDAO dao = new BoletimDAO()) {

            return dao.listarPorAluno(idAluno);
        }
    }
}
