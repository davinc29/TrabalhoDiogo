package com.controller;

import com.dao.DisciplinaDAO;
import com.dao.ObservacaoDAO;
import com.dto.DisciplinaViewDTO;
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

@WebServlet("/disciplinas")
public class DisciplinaAlunoServlet extends HttpServlet {
    private static final String PAGINA_PRINCIPAL = "portal-aluno/disciplinas.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean erro = true;

        try {
            List<DisciplinaViewDTO> disciplinas = listar();
            req.setAttribute("disciplinas", disciplinas);

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

    public List<DisciplinaViewDTO> listar() throws SQLException, ClassNotFoundException{
        try (DisciplinaDAO dao = new DisciplinaDAO()) {
            return dao.listar();
        }
    }
}
