<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AlunoViewDTO aluno = (AlunoViewDTO) session.getAttribute("usuario");
%>

<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Conta</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-aluno/conta-edit.css" />
    <script src="${pageContext.request.contextPath}/mobile-navbar.js"></script>
    <script src="${pageContext.request.contextPath}/javascript/passwordRequirements.js" defer></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico" />
</head>
<body>
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

              <form onsubmit="validarSenha(event)" id="atualizarSenha" action="${pageContext.request.contextPath}/aluno?action=update" method="post">
                  <div class="campos">
                      <div class="email d-flex flex-column mb-4" style="margin-top: 40px">
                          <label for="email-id">E-mail atual:</label>
                          <input type="text" id="email-id" value="<%=aluno.getEmail()%>" disabled />
                          <input type="hidden" name="email" value="<%=aluno.getEmail()%>">
                      </div>
                      <div class="senha d-flex flex-column mb-4">
                          <label for="senha-id">Senha atual:</label>
                          <input type="password" id="senhaAtual" class="senha-id" name="senha_atual" required/>
                      </div>
                      <div class="senha d-flex flex-column">
                          <label for="senha-id">Nova senha:</label>
                          <input type="password" id="novaSenha" class="senha-id" name="nova_senha" required />
                      </div>
                      <div class="senha d-flex flex-column" style="margin-top: 20px; margin-bottom: 10px">
                          <label for="senha-id">Confirmar senha:</label>
                          <input type="password" id="confirmarSenha" class="senha-id" required />

                          <p id="erroSenha" style="color: red"></p>
                      </div>
                  </div>

                  <div class="edit-container">
                      <button type="submit" class="edit-button">Salvar</button>
                      <div class="edit-button">
                          <a href="${pageContext.request.contextPath}/jsp/portal-aluno/conta.jsp">Cancelar</a>
                      </div>
                  </div>
              </form>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
