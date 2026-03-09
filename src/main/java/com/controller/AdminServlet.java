package com.controller;

import com.dao.AlunoDAO;
import com.dao.DisciplinaDAO;
import com.dao.ProfessorDAO;
import com.dto.AlunoCadastrarDTO;
import com.dto.AlunoViewDTO;
import com.dto.DisciplinaViewDTO;
import com.dto.ProfessorDTO;
import com.model.Disciplina;
import com.model.Professor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private static final String ALUNOS = "/jsp/portal-admin/alunos.jsp";
    private static final String ALUNOS_ADD = "/jsp/portal-admin/alunos-adicionar.jsp";
    private static final String ALUNOS_EDIT = "/jsp/portal-admin/alunos-editar.jsp";

    private static final String PROFESSORES = "/jsp/portal-admin/professores.jsp";
    private static final String PROFESSORES_ADD = "/jsp/portal-admin/professores-adicionar.jsp";
    private static final String PROFESSORES_EDIT = "/jsp/portal-admin/professores-editar.jsp";

    private static final String DISCIPLINAS = "/jsp/portal-admin/disciplinas.jsp";
    private static final String DISCIPLINAS_ADD = "/jsp/portal-admin/disciplinas-adicionar.jsp";
    private static final String DISCIPLINAS_EDIT = "/jsp/portal-admin/disciplinas-editar.jsp";

    private static final String ROTA_ALUNOS = "/admin?action=readAlunos";
    private static final String ROTA_PROFESSORES = "/admin?action=readProfessores";
    private static final String ROTA_DISCIPLINAS = "/admin?action=readDisciplinas";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String destino = ALUNOS;

        if (action == null || action.isBlank()) {
            action = "readAlunos";
        }

        try {
            switch (action) {
                case "read", "readAlunos" -> {
                    List<AlunoViewDTO> alunos = listaAlunos(req);
                    req.setAttribute("alunos", alunos);
                    destino = ALUNOS;
                }

                case "addAluno" -> destino = ALUNOS_ADD;

                case "editAluno" -> {
                    String idParam = req.getParameter("id");

                    if (idParam == null || idParam.isBlank()) {
                        resp.sendRedirect(req.getContextPath() + ROTA_ALUNOS);
                        return;
                    }

                    UUID idAluno = UUID.fromString(idParam);

                    try (AlunoDAO alunoDAO = new AlunoDAO()) {
                        AlunoViewDTO aluno = alunoDAO.listarAlunoPorId(idAluno);
                        req.setAttribute("aluno", aluno);
                    }

                    destino = ALUNOS_EDIT;
                }

                case "readProfessores" -> {
                    try (ProfessorDAO professorDAO = new ProfessorDAO()) {
                        List<ProfessorDTO> professores = professorDAO.listar();
                        req.setAttribute("professores", professores);
                    }
                    destino = PROFESSORES;
                }

                case "addProfessor" -> destino = PROFESSORES_ADD;

                case "editProfessor" -> {
                    String idParam = req.getParameter("id");

                    if (idParam == null || idParam.isBlank()) {
                        resp.sendRedirect(req.getContextPath() + ROTA_PROFESSORES);
                        return;
                    }

                    UUID idProfessor = UUID.fromString(idParam);

                    try (ProfessorDAO professorDAO = new ProfessorDAO()) {
                        ProfessorDTO professor = professorDAO.pesquisarPorId(idProfessor);
                        req.setAttribute("professor", professor);
                    }

                    destino = PROFESSORES_EDIT;
                }

                case "readDisciplinas" -> {
                    try (DisciplinaDAO disciplinaDAO = new DisciplinaDAO()) {
                        List<DisciplinaViewDTO> disciplinas = disciplinaDAO.listar();
                        req.setAttribute("disciplinas", disciplinas);
                    }
                    destino = DISCIPLINAS;
                }

                case "addDisciplina" -> {
                    try (ProfessorDAO professorDAO = new ProfessorDAO()) {
                        List<ProfessorDTO> professores = professorDAO.listar();
                        req.setAttribute("professores", professores);
                    }
                    destino = DISCIPLINAS_ADD;
                }

                case "editDisciplina" -> {
                    String idParam = req.getParameter("id");

                    if (idParam == null || idParam.isBlank()) {
                        resp.sendRedirect(req.getContextPath() + ROTA_DISCIPLINAS);
                        return;
                    }

                    Integer idDisciplina = Integer.parseInt(idParam);

                    try (DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                         ProfessorDAO professorDAO = new ProfessorDAO()) {
                        Disciplina disciplina = disciplinaDAO.pesquisarPorId(idDisciplina);
                        List<ProfessorDTO> professores = professorDAO.listar();

                        req.setAttribute("disciplina", disciplina);
                        req.setAttribute("professores", professores);
                    }

                    destino = DISCIPLINAS_EDIT;
                }

                default -> {
                    resp.sendRedirect(req.getContextPath() + ROTA_ALUNOS);
                    return;
                }
            }

            req.getRequestDispatcher(destino).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("Erro no AdminServlet: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String destino = ROTA_ALUNOS;

        try {
            if (action == null || action.isBlank()) {
                resp.sendRedirect(req.getContextPath() + ROTA_ALUNOS);
                return;
            }

            switch (action) {
                case "createAluno" -> destino = criarAluno(req);
                case "updateAluno" -> destino = atualizarAluno(req);
                case "deleteAluno" -> destino = deletarAluno(req);

                case "createProfessor" -> destino = criarProfessor(req);
                case "updateProfessor" -> destino = atualizarProfessor(req);
                case "deleteProfessor" -> destino = deletarProfessor(req);

                case "createDisciplina" -> destino = criarDisciplina(req);
                case "updateDisciplina" -> destino = atualizarDisciplina(req);
                case "deleteDisciplina" -> destino = deletarDisciplina(req);

                default -> destino = ROTA_ALUNOS;
            }

            resp.sendRedirect(req.getContextPath() + destino);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("Erro no AdminServlet: " + e.getMessage());
        }
    }

    private String criarAluno(HttpServletRequest req) throws SQLException {
        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        Integer matricula = Integer.parseInt(req.getParameter("matricula"));
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        AlunoCadastrarDTO dto = new AlunoCadastrarDTO(nome, matricula, email, senha);

        try (AlunoDAO alunoDAO = new AlunoDAO()) {
            alunoDAO.cadastrarAluno(dto);
        }

        return ROTA_ALUNOS;
    }

    private String atualizarAluno(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_ALUNOS;
        }

        UUID idAluno = UUID.fromString(idParam);

        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        String email = req.getParameter("email");

        try (AlunoDAO alunoDAO = new AlunoDAO()) {
            alunoDAO.atualizarNomeAluno(idAluno, nome);
            alunoDAO.atualizarEmailAluno(idAluno, email);
        }

        return ROTA_ALUNOS;
    }

    private String deletarAluno(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_ALUNOS;
        }

        UUID idAluno = UUID.fromString(idParam);

        try (AlunoDAO alunoDAO = new AlunoDAO()) {
            alunoDAO.deletarAlunoPorId(idAluno);
        }

        return ROTA_ALUNOS;
    }

    private String criarProfessor(HttpServletRequest req) throws SQLException {
        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        Professor professor = new Professor(nome, username, email, senha);

        try (ProfessorDAO professorDAO = new ProfessorDAO()) {
            professorDAO.cadastrar(professor);
        }

        return ROTA_PROFESSORES;
    }

    private String atualizarProfessor(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_PROFESSORES;
        }

        UUID id = UUID.fromString(idParam);

        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        ProfessorDTO original;

        try (ProfessorDAO professorDAO = new ProfessorDAO()) {
            original = professorDAO.pesquisarPorId(id);
        }

        ProfessorDTO atualizado = new ProfessorDTO(id, nome, username, email);

        try (ProfessorDAO professorDAO = new ProfessorDAO()) {
            professorDAO.atualizar(original, atualizado);
        }

        return ROTA_PROFESSORES;
    }

    private String deletarProfessor(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_PROFESSORES;
        }

        UUID id = UUID.fromString(idParam);

        try (ProfessorDAO professorDAO = new ProfessorDAO()) {
            professorDAO.deletar(id);
        }

        return ROTA_PROFESSORES;
    }

    private String criarDisciplina(HttpServletRequest req) throws SQLException {
        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        String professorIdParam = req.getParameter("idProfessor");

        if (professorIdParam == null || professorIdParam.isBlank()) {
            return ROTA_DISCIPLINAS;
        }

        UUID idProfessor = UUID.fromString(professorIdParam);
        Disciplina disciplina = new Disciplina(nome, idProfessor);

        try (DisciplinaDAO disciplinaDAO = new DisciplinaDAO()) {
            disciplinaDAO.cadastrar(disciplina);
        }

        return ROTA_DISCIPLINAS;
    }

    private String atualizarDisciplina(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        nome = WordUtils.capitalize(nome);
        String professorIdParam = req.getParameter("idProfessor");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_DISCIPLINAS;
        }

        Integer idDisciplina = Integer.parseInt(idParam);

        try (DisciplinaDAO disciplinaDAO = new DisciplinaDAO()) {
            Disciplina original = disciplinaDAO.pesquisarPorId(idDisciplina);

            if (original == null) {
                return ROTA_DISCIPLINAS;
            }

            UUID idProfessor = professorIdParam == null || professorIdParam.isBlank()
                    ? original.getIdProfessor()
                    : UUID.fromString(professorIdParam);

            Disciplina atualizada = new Disciplina(idDisciplina, nome, idProfessor);
            disciplinaDAO.atualizar(original, atualizada);
        }

        return ROTA_DISCIPLINAS;
    }

    private String deletarDisciplina(HttpServletRequest req) throws SQLException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            return ROTA_DISCIPLINAS;
        }

        Integer idDisciplina = Integer.parseInt(idParam);

        try (DisciplinaDAO disciplinaDAO = new DisciplinaDAO()) {
            disciplinaDAO.deletar(idDisciplina);
        }

        return ROTA_DISCIPLINAS;
    }

    public List<AlunoViewDTO> listaAlunos(HttpServletRequest req) throws SQLException{
        String temp = req.getParameter("nome");
        String nome = (temp.isBlank() ? null : temp.trim());

        temp = req.getParameter("matricula");
        String matricula = (temp.isBlank() ? null : temp.trim());

        temp = req.getParameter("turmaAno");
        String turmaAno = (temp.isBlank() ? null : temp.trim());

        try (AlunoDAO dao = new AlunoDAO()) {
            return dao.listarAlunos(nome, matricula, turmaAno);
        }
    }
}