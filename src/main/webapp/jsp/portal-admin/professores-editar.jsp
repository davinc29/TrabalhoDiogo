<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dto.ProfessorDTO" %>

<%
    ProfessorDTO professor = (ProfessorDTO) request.getAttribute("professor");
%>

<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Editar Professor</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <script defer src="${pageContext.request.contextPath}/javascript/passwordRequirements.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-admin/professores-editar.css" />
</head>
<body>
<section class="d-sm-flex align-items-center vh-100 d-none home">
    <aside class="bg-primary sidebar">
        <nav class="text-secondary">
            <ul>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readAlunos">Alunos</a>
                </li>
                <li class="page-item active">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readProfessores">Professores</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readDisciplinas">Disciplinas</a>
                </li>
            </ul>
        </nav>
    </aside>

    <div class="w-100 m-5">
        <main>
            <div class="aluno-card d-flex justify-content-between flex-column">
                <div class="d-flex justify-content-between">
                    <div class="aluno-informacoes">
                        <h2 class="aluno-nome fs-3">Editar professor</h2>
                    </div>
                </div>

                <hr />

                <div class="input-container">
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="action" value="updateProfessor" />
                        <input type="hidden" name="id" value="<%=professor.getId()%>" />

                        <div class="d-flex flex-column">
                            <div class="linha-cima d-flex">
                                <div class="campo d-flex flex-column">
                                    <label for="username">Username:</label>
                                    <input type="text" id="username" name="username" value="<%=professor.getUsername()%>" required />
                                </div>
                            </div>

                            <div class="linha-baixo d-flex mt-3">
                                <div class="campo d-flex flex-column">
                                    <label for="nome">Nome:</label>
                                    <input type="text" id="nome" name="nome" value="<%=professor.getNome()%>" required />
                                </div>

                                <div class="campo d-flex flex-column">
                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" value="<%=professor.getEmail()%>" required />
                                </div>

                                <div class="campo d-flex flex-column">
                                    <label for="senha">Senha:</label>
                                    <input type="password" id="senha" class="validar-senha" name="senha" />
                                </div>
                            </div>
                        </div>

                        <div class="opcoes d-flex" style="margin-top: 60px">
                            <div class="save-container">
                                <button class="save" type="submit">Salvar</button>
                            </div>

                            <div class="return-button">
                                <a href="${pageContext.request.contextPath}/admin?action=readProfessores">Cancelar</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</section>
</body>
</html>