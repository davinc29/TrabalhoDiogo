package com.controller;

import com.dao.AlunoDAO;
import com.dto.AlunoCadastrarDTO;
import com.exception.ExcecaoDeJSP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cadastro-aluno")
public class CadastroAlunoServlet extends HttpServlet {

    private static final String PAGINA_CADASTRO = "/cadastro.jsp";
    private static final String PAGINA_LOGIN = "/index.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PAGINA_CADASTRO).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Mantém o padrão do LoginServlet: action + switch
        String action = req.getParameter("action");
        action = (action == null || action.isBlank()) ? "cadastrar" : action.trim();

        boolean erro = true;
        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "cadastrar" -> {
                    String nome = req.getParameter("nome");
                    nome = WordUtils.capitalize(nome);
                    String matriculaStr = req.getParameter("matricula");
                    String email = req.getParameter("email");
                    String senha = req.getParameter("senha");

                    nome = (nome == null) ? "" : nome.trim();
                    email = (email == null) ? "" : email.trim();
                    senha = (senha == null) ? "" : senha.trim();
                    matriculaStr = (matriculaStr == null) ? "" : matriculaStr.trim();

                    if (nome.isBlank() || email.isBlank() || senha.isBlank() || matriculaStr.isBlank()) {
                        throw new ExcecaoDeJSP("Preencha todos os campos.");
                    }

                    Integer matricula;

                    try {
                        matricula = Integer.parseInt(matriculaStr);
                    } catch (NumberFormatException e) {
                        throw new ExcecaoDeJSP("Matrícula inválida.");
                    }

                    AlunoCadastrarDTO dto = new AlunoCadastrarDTO(nome, matricula, email, senha);

                    int resultado = cadastrar(dto);

                    switch (resultado) {
                        case 1 -> {
                            resp.sendRedirect(req.getContextPath() + PAGINA_LOGIN);
                            return;
                        }
                        case 2 -> throw new ExcecaoDeJSP("Matrícula não existe na pré-matrícula.");
                        case 3 -> throw new ExcecaoDeJSP("Matrícula já cadastrada.");
                        default -> throw new ExcecaoDeJSP("Não foi possível cadastrar. Tente novamente.");
                    }
                }

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }
        }
        // Mesmo padrão do LoginServlet
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

        if (erro) {
            resp.sendRedirect(req.getContextPath() + destino);
        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
    }

    private int cadastrar(AlunoCadastrarDTO dto) throws SQLException, ClassNotFoundException {
        try (AlunoDAO dao = new AlunoDAO()) {
            return dao.cadastrarAluno(dto);
        }
    }
}