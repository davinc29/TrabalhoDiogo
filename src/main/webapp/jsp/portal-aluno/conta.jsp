<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="com.dao.AlunoDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AlunoViewDTO aluno = (AlunoViewDTO) session.getAttribute("usuario");

    // Transformar a senha em asteriscos
    String senhaAsteriscos = "********";
%>

<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Conta</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-aluno/conta.css" />
    <script src="${pageContext.request.contextPath}/mobile-navbar.js"></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico" />
  </head>
  <body>
    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none home">
      <aside class="bg-primary sidebar">
        <nav class="text-secondary">
          <ul class="">
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/home?usuario=aluno">Home</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/boletim?usuario=aluno">Boletim</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/observacoes?usuario=aluno"
                >Observações</a
              >
            </li>
            <li class="page-item active">
              <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-aluno/conta.jsp">Conta</a>
            </li>
          </ul>
        </nav>
      </aside>

      <div class="w-100 m-5">
        <header class="d-flex w-100 justify-content-between">
          <div class="lh-1">
            <p class="fs-5 fw-bold">Portal do Estudante</p>
            <p class="fs-5 text-primary">
              <span class="fw-bold"><%=session.getAttribute("diaSemana")%></span>, <%=session.getAttribute("data")%>
            </p>
          </div>
          <div class="d-flex">
            <img
              class="icon m-3"
              src="${pageContext.request.contextPath}/assets/notificao-icon.svg"
              alt="Notificações Icon"
            />
            <img
              class="icon m-3"
              src="${pageContext.request.contextPath}/assets/mensagens-icon.svg"
              alt="Mensagens Icon"
            />
            <div class="bg-primary box-name m-3">
              <p class="fs-4 fw-bold text-secondary"><%=session.getAttribute("nome2L")%></p>
            </div>
            <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=session.getAttribute("nome")%></p>
          </div>
        </header>
        <main>
          <div class="account-card">
            <div class="informacoes-topo">
              <h2><%=session.getAttribute("nome")%></h2>
              <div class="matricula-turma d-flex justify-content-between">
                <p><span class="fw-bold">Matrícula: </span><%=session.getAttribute("matricula")%></p>
                <p><span class="fw-bold">Turma: </span><%=session.getAttribute("turma")%></p>
              </div>
            </div>

            <div class="campos">
              <div class="email d-flex flex-column mb-4">
                <label for="email-id">E-mail</label>
                <input type="text" id="email-id" value="<%=aluno.getEmail()%>" required disabled />
              </div>
              <div class="senha d-flex flex-column">
                <label for="senha-id">Senha</label>
                <input type="password" id="senha-id" value="<%=senhaAsteriscos%>" required disabled />
              </div>
            </div>

            <div class="edit-container justify-content-between">
              <div class="edit-button">
                <a href="${pageContext.request.contextPath}/sistema-filter">Sair</a>
              </div>

              <div class="edit-button">
                <a href="conta-edit.jsp">Editar</a>
              </div>
            </div>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
