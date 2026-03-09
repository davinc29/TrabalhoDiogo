<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Adicionar Aluno</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-admin/alunos-adicionar.css" />
    <script src="${pageContext.request.contextPath}/mobile-navbar.js"></script>
    <script defer src="${pageContext.request.contextPath}/javascript/passwordRequirements.js"></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico" />
</head>
<body>
<section class="d-sm-flex align-items-center vh-100 d-none home">
    <aside class="bg-primary sidebar">
        <nav class="text-secondary">
            <ul>
                <li class="page-item active">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readAlunos">Alunos</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readProfessores">Professores</a>
                </li>
                <li class="page-item can-hover">
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
                        <h2 class="aluno-nome fs-3">Novo aluno</h2>
                    </div>
                </div>

                <hr />

                <div class="input-container">
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="action" value="createAluno" />

                        <div class="d-flex flex-column">
                            <div class="linha-cima d-flex">
                                <div class="campo d-flex flex-column">
                                    <label for="nome">Nome:</label>
                                    <input type="text" id="nome" name="nome" placeholder="Ex: Kleber Silva" required />
                                </div>

                                <div class="campo d-flex flex-column">
                                    <label for="matricula">Matrícula:</label>
                                    <input type="number" id="matricula" name="matricula" placeholder="Ex: 12345" required />
                                </div>
                            </div>

                            <div class="linha-baixo d-flex mt-3">
                                <div class="campo d-flex flex-column">
                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" placeholder="Ex: kleber.silva@gmail.com" required />
                                </div>

                                <div class="campo d-flex flex-column">
                                    <label for="senha">Senha:</label>
                                    <input type="password" id="senha" name="senha" class="validar-senha" placeholder="Ex: @Senha123" required />
                                </div>
                            </div>
                        </div>

                        <div class="opcoes d-flex">
                            <div class="save-container">
                                <button class="save" type="submit">Criar</button>
                            </div>

                            <div class="return-button">
                                <a href="${pageContext.request.contextPath}/admin?action=readAlunos">Cancelar</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</section>

<section class="vh-100 w-100 d-lg-none"></section>
</body>
</html>