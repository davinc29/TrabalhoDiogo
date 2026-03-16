<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.Disciplina" %>
<%@ page import="com.dto.ProfessorDTO" %>

<%
    Disciplina disciplina = (Disciplina) request.getAttribute("disciplina");
    List<ProfessorDTO> professores = (List<ProfessorDTO>) request.getAttribute("professores");
%>

<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Editar Disciplina</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-admin/disciplinas-editar.css" />
</head>
<body>
<section class="d-sm-flex align-items-center vh-100 d-none home">
    <aside class="bg-primary sidebar">
        <nav class="text-secondary">
            <ul>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readAlunos">Alunos</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readProfessores">Professores</a>
                </li>
                <li class="page-item active">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readDisciplinas">Disciplinas</a>
                </li>
            </ul>
        </nav>
    </aside>

    <div class="w-100 m-5">
        <header class="d-flex w-100 justify-content-between">
            <div class="lh-1">
                <p class="fs-5 fw-bold">Portal do Administrador</p>
                <p class="fs-5 text-primary">
                    <span class="fw-bold"><%= session.getAttribute("diaSemana") %></span>, <%= session.getAttribute("data") %>
                </p>
            </div>
        </header>

        <main>
            <div class="aluno-card d-flex justify-content-between flex-column">
                <div class="d-flex justify-content-between">
                    <div class="aluno-informacoes">
                        <h2 class="aluno-nome fs-3">Editar disciplina</h2>
                    </div>
                </div>

                <hr />

                <% if (disciplina != null) { %>
                <div class="input-container">
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="action" value="updateDisciplina" />
                        <input type="hidden" name="id" value="<%=disciplina.getIdDisciplina()%>" />

                        <div class="d-flex flex-column">
                            <div class="linha-cima d-flex">
                                <div class="campo d-flex flex-column">
                                    <label for="nome">Nome:</label>
                                    <input type="text" id="nome" name="nome" value="<%=disciplina.getNome()%>" required />
                                </div>
                            </div>

                            <div class="linha-baixo d-flex mt-3">
                                <div class="campo d-flex flex-column">
                                    <label for="idProfessor">Professor:</label>
                                    <select id="idProfessor" name="idProfessor" required>
                                        <option value="">Selecione um professor</option>
                                        <%
                                            if (professores != null) {
                                                for (ProfessorDTO professor : professores) {
                                                    boolean selecionado = professor.getId().equals(disciplina.getIdProfessor());
                                        %>
                                        <option value="<%=professor.getId()%>" <%= selecionado ? "selected" : "" %>>
                                            <%=professor.getNome()%> - <%=professor.getEmail()%>
                                        </option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="opcoes d-flex" style="margin-top:60px">
                            <div class="save-container">
                                <button class="save" type="submit">Salvar</button>
                            </div>

                            <div class="return-button">
                                <a href="${pageContext.request.contextPath}/admin?action=readDisciplinas">Cancelar</a>
                            </div>
                        </div>
                    </form>
                </div>
                <% } else { %>
                <p>Disciplina não encontrada.</p>
                <% } %>
            </div>
        </main>
    </div>
</section>
</body>
</html>