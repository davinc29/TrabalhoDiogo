<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="com.dto.BoletimViewDTO" %>
<%@ page import="com.dao.BoletimDAO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AlunoViewDTO aluno = (AlunoViewDTO) session.getAttribute("usuario");

    List<BoletimViewDTO> boletim = (List<BoletimViewDTO>) request.getAttribute("boletim");
%>

<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Boletim</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-aluno/boletim.css" />
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
            <li class="page-item active">
              <a class="page-text" href="#">Boletim</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/observacoes?usuario=aluno">Observações</a>
            </li>
            <li class="page-item can-hover">
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
            <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=aluno.getNome()%></p>
          </div>
        </header>
        <main>
          <div class="filter-box d-flex flex-column">
              <form action="${pageContext.request.contextPath}/boletim">
              <input type="hidden" name="usuario" value="aluno">
              <input type="hidden" name="id_aluno" value="<%=aluno.getIdAluno()%>">
                  <div class="linha-cima d-flex">
                      <div class="filter-name ms-4">
                          <input
                                  type="number" step="0.01" name="nota1"
                                  placeholder="Buscar por nota do primeiro semestre..."
                          />
                      </div>
                      <div class="filter-name ms-4">
                          <input
                                  type="number" step="0.01" name="nota2"
                                  placeholder="Buscar por nota do segundo semestre..."
                          />
                      </div>
                      <div class="filter-name" style="width: 46%;">
                          <input type="number" step="0.01" name="media" placeholder="Buscar por média..." />
                      </div>
                  </div>
                  <div class="linha-baixo d-flex mt-3 justify-content-between">
                      <div class="d-flex lado-esquerdo">
                          <div class="filter-name" style="width: 46%;">
                              <input type="text" name="nome_disciplina" placeholder="Buscar por nome da disciplina..." />
                          </div>
                          <div class="filter-button ms-4">
                              <button type="submit">Aplicar Filtro</button>
                          </div>
                      </div>
                  </div>
              </form>
          </div>

            <div class="tabela-container">
                <table class="tabela-notas">
                    <tr>
                        <th style="text-align:left;">Disciplina</th>
                        <th style="text-align:center;">Primeiro Semestre</th>
                        <th style="text-align:center;">Segundo Semestre</th>
                        <th style="text-align:center;">Média</th>
                        <th style="text-align:center;">Status</th>
                    </tr>
                    <%
                        if (boletim != null && !boletim.isEmpty()) {

                            for (int i = 0; i < boletim.size(); i++) {
                                BoletimViewDTO b = boletim.get(i);
                    %>
                    <tr>
                        <td>
                            <p><%= b.getNomeDisciplina() %></p>
                        </td>

                        <td style="text-align:center;">
                            <p style="color:<%= b.getNota1() >= 7 ? "green" : "red" %>">
                                <%= b.getNota1() %>
                            </p>
                        </td>

                        <td style="text-align:center;">
                            <p style="color:<%= b.getNota2() >= 7 ? "green" : "red" %>">
                                <%=(b.getStatus().equalsIgnoreCase("PROCESSANDO") ? "-" : b.getNota2())%>
                            </p>
                        </td>

                        <td style="text-align:center;">
                            <p style="color:<%= b.getMedia() >= 7 ? "green" : "red" %>">
                                <%= b.getMedia() %>
                            </p>
                        </td>

                        <%
                            String cor;
                            String status = b.getStatus().toUpperCase();

                            if (status.equals("APROVADO")) {
                                cor = "green";
                            } else if (status.equals("REPROVADO")) {
                                cor = "red";
                            } else {
                                cor = "#FF8C00";
                            }
                        %>
                        <td>
                            <p style="color:<%=cor%>">
                                    <%= b.getStatus() %>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="5">
                            <p>Nenhuma nota encontrada.</p>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
