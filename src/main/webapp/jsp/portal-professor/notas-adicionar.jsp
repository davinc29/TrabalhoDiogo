<%@ page import="com.dto.ProfessorDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dto.BoletimViewDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
   // Pegando dados diretos do banco
    ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
    AlunoViewDTO aluno = (AlunoViewDTO) request.getAttribute("aluno");
    List<BoletimViewDTO> boletins = (List<BoletimViewDTO>) request.getAttribute("boletins");
    Map<String, Integer> mapNomeIdProfessor = (Map<String, Integer>) request.getAttribute("mapNomeIdProfessor");

    // Pegando dia da semana e data
    String data = (String) session.getAttribute("data");
    String diaSemana = (String) session.getAttribute("diaSemana");
    String nome2L = (String) session.getAttribute("nome2L");

%>
<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Notas</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-professor/notas-adicionar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-admin/disciplinas.css" />
    <script src="${pageContext.request.contextPath}/javascript/mobile-navbar.js" defer></script>
    <script src="${pageContext.request.contextPath}/javascript/delete.js" defer></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  </head>
  <body>
    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none home">
      <aside class="bg-primary sidebar">
        <nav class="text-secondary">
          <ul class="">
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/home?usuario=professor">Home</a>
            </li>
            <li class="page-item active">
              <a class="page-text" href="#">Notas</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/alunos-professor?action=observacoes">Observações</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-professor/conta.jsp">Conta</a>
            </li>
          </ul>
        </nav>
      </aside>

      <div class="w-100 m-5">
        <header class="d-flex w-100 justify-content-between">
          <div class="lh-1">
            <p class="fs-5 fw-bold">Portal do Professor</p>
            <p class="fs-5 text-primary">
              <span class="fw-bold"><%=diaSemana%></span>, <%=data%>
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
                  <p class="fs-4 fw-bold text-secondary"><%=nome2L%></p>
              </div>
            <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=professor.getNome()%></p>
          </div>
        </header>
        <main>
          <div class="filter-box d-flex flex-column">
          <form action="${pageContext.request.contextPath}/boletim">
            <input type="hidden" name="action" value="read">
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

              <div class="d-flex lado-direito">
                <div class="add-button ms-4">
                  <a href="${pageContext.request.contextPath}/boletim?action=create&id_aluno=<%=aluno.getIdAluno()%>&id_professor=<%=professor.getId()%>">+ Adicionar</a>
                </div>
                <div class="return-button ms-4">
                  <a href="${pageContext.request.contextPath}/alunos-professor?action=notas">< Voltar</a>
                </div>
              </div>
            </div>
          </form>
          </div>

          <div class="tabela-container">
            <table class="tabela-notas">
              <tr>
                <th>Nome</th>
                <th>Matrícula</th>
                <th>Turma</th>
                <th>Disciplina</th>
                <th>Primeiro Semestre</th>
                <th>Segundo Semestre</th>
                <th>Média</th>
                <th>Status</th>
              </tr>
                <%for (BoletimViewDTO boletim : boletins) {%>
                <tr>
                    <td>
                        <p><%=aluno.getNome()%></p>
                    </td>
                    <td>
                        <p><%=aluno.getMatricula()%></p>
                    </td>
                    <td>
                        <p><%=aluno.getTurma_ano()%></p>
                    </td>
                    <td>
                        <p><%=boletim.getNomeDisciplina()%></p>
                    </td>
                    <td>
                        <p><%=boletim.getNota1()%></p>
                    </td>
                    <td>
                        <p><%=boletim.getNota2()%></p>
                    </td>
                    <td>
                        <p><%=boletim.getMedia()%></p>
                    </td>
                    <%
                        String cor;
                        String status = boletim.getStatus().toUpperCase();

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
                                <%= boletim.getStatus() %>
                    </td>
                    <%if (mapNomeIdProfessor.get(boletim.getNomeDisciplina()) != null) {%>
                    <td class="action-box">
                        <form action="${pageContext.request.contextPath}/boletim" method="get">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id_boletim" value=<%=boletim.getId()%>>
                            <input type="hidden" name="id_aluno" value=<%=aluno.getIdAluno()%>>
                            <button type="submit" id="editar" class="action-btn">
                                <img
                                    class="table-icon"
                                    src="${pageContext.request.contextPath}/assets/editar.svg"
                                    alt="Editar Icon"
                                />
                            </button>
                        </form>
                        <form action="${pageContext.request.contextPath}/boletim" method="post" onsubmit="confirmarDelete(event)">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id_boletim" value=<%=boletim.getId()%>>
                            <input type="hidden" name="id_aluno" value=<%=aluno.getIdAluno()%>>
                            <button type="submit" class="action-btn">
                                <img
                                    class="table-icon"
                                    src="${pageContext.request.contextPath}/assets/apagar.svg"
                                    alt="Deletar Icon"
                                />
                            </button>
                        </form>
                    </td>
                    <%}%>
                </tr>
                <%}%>
            </table>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
