<%@ page import="com.dto.ProfessorDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Pegando dados diretos do banco
    ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
    List<AlunoViewDTO> alunos = (List<AlunoViewDTO>) request.getAttribute("alunos");

    List<AlunoViewDTO> alunos1 = new ArrayList<>();
    List<AlunoViewDTO> alunos2 = new ArrayList<>();
    int cont = 1;
    for (AlunoViewDTO aluno : alunos) {
        if (cont <= alunos.size()/2) {
            alunos1.add(aluno);
        } else {
            alunos2.add(aluno);
        }
        cont++;
    }

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-professor/notas.css" />
    <script src="${pageContext.request.contextPath}/javascript/mobile-navbar.js"></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico">
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
          <div class="filter-box d-flex">
            <div class="filter-name">
              <input type="text" placeholder="Buscar por nome..." />
            </div>
            <div class="filter-name ms-4">
              <input type="text" placeholder="Buscar por matrícula..." />
            </div>
            <div class="filter-name ms-4">
              <input type="text" placeholder="Buscar por turma..." />
            </div>
            <div class="filter-button ms-4">
                <button>Aplicar Filtro</button>
            </div>
          </div>

          <div class="cards">
            <div class="coluna-esquerda">
                <%for (AlunoViewDTO aluno : alunos1) {%>
              <div class="aluno-card d-flex justify-content-between">
                <div class="aluno-informacoes">
                  <h2 class="aluno-nome fs-3"><%=aluno.getNome()%></h2>
                  <p class="aluno-matricula">
                    <span class="fw-bold">Matrícula: </span><%=aluno.getMatricula()%>
                  </p>
                  <p class="aluno-turma">
                    <span class="fw-bold">Turma: </span><%=aluno.getTurma_ano()%>
                  </p>
                </div>
                <div class="aluno-adicionar">
                  <div class="botao-adicionar">
                      <form action="${pageContext.request.contextPath}/boletim?action=read" method="post">
                          <input type="hidden" name="id_aluno" value=<%=aluno.getIdAluno()%>>
                          <input type="hidden" name="usuario" value="professor">
                          <input type="submit" value="+" class="add-btn">
                      </form>
                  </div>
                  <p class="text-primary">Adicionar Nota</p>
                </div>
              </div>
                <%}%>
            </div>

            <div class="coluna-direita">
                <%for (AlunoViewDTO aluno : alunos2) {%>
                <div class="aluno-card d-flex justify-content-between">
                    <div class="aluno-informacoes">
                        <h2 class="aluno-nome fs-3"><%=aluno.getNome()%></h2>
                        <p class="aluno-matricula">
                            <span class="fw-bold">Matrícula: </span><%=aluno.getMatricula()%>
                        </p>
                        <p class="aluno-turma">
                            <span class="fw-bold">Turma: </span><%=aluno.getTurma_ano()%>
                        </p>
                    </div>
                    <div class="aluno-adicionar">
                        <div class="botao-adicionar">
                            <form action="${pageContext.request.contextPath}/boletim?action=read" method="post">
                                <input type="hidden" name="id_aluno" value=<%=aluno.getIdAluno()%>>
                                <input type="hidden" name="usuario" value="professor">
                                <input type="submit" value="+">
                            </form>
                        </div>
                        <p class="text-primary">Adicionar Nota</p>
                    </div>
                </div>
                <%}%>
            </div>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
