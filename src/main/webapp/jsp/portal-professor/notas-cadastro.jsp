<%@ page import="com.dto.ProfessorDTO" %>
<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Pegando dados diretos do banco
    ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
    AlunoViewDTO aluno = (AlunoViewDTO) request.getAttribute("aluno");
    Map<String, Integer> mapNomeIdProfessor = (Map<String, Integer>) request.getAttribute("mapNomeIdProfessor");

    // Pegando o dia da semana
    LocalDate hoje = LocalDate.now();
    Locale brasil = new Locale("pt","BR");
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("EEEE", brasil);
    String diaSemana = hoje.format(formatador);
    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);

    // Pegando o dia de hoje
    Integer diaNum = hoje.getDayOfMonth();

    // Pegando o mês do ano
    List<String> meses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez");
    String mes = meses.get(hoje.getMonthValue()-1);

    // Pegando o ano
    Integer ano = hoje.getYear();

    // Data retornada
    String data = String.format("%d %s %d", diaNum, mes, ano);


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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-professor/notas-cadastro.css" />
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
              <a class="page-text" href="notas.jsp">Notas</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="observacoes.jsp">Observações</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="conta.jsp">Conta</a>
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
              <p class="fs-4 fw-bold text-secondary">RE</p>
            </div>
            <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=professor.getNome()%></p>
          </div>
        </header>
        <main>
          <div class="aluno-card d-flex justify-content-between flex-column">
            <div class="d-flex justify-content-between">
              <div class="aluno-informacoes">
                <h2 class="aluno-nome fs-3"><%=aluno.getNome()%></h2>
                <div class="d-flex">
                  <p class="aluno-matricula">
                    <span class="fw-bold">Matrícula: </span><%=aluno.getMatricula()%>
                  </p>
                  <p class="aluno-turma ms-3">
                    <span class="fw-bold">Turma: </span><%=aluno.getTurma_ano()%>
                  </p>
                </div>
              </div>
            </div>

            <hr />
            <div class="input-container">
              <form action="${pageContext.request.contextPath}/boletim?action=create&usuario=professor" method="post">
                  <input type="hidden" name="id_aluno" value=<%=aluno.getIdAluno()%>>
                <div class="d-flex">
                  <!-- <div class="campo d-flex flex-column">
                    <label for="disciplina">Disciplina:</label>
                    <input type="text" id="disciplina" disabled />
                  </div> -->
                    <div class="campo d-flex flex-column">
                    <label for="id_disciplina"
                    >Disciplina:</label>
                    <select id="id_disciplina" name="id_disciplina" required>
                        <option value="" selected>Selecione a Fábrica a qual o pagamento se refere</option>

                        <% for (String disciplina : mapNomeIdProfessor.keySet()) { %>
                        <option value="<%= mapNomeIdProfessor.get(disciplina)%>">
                            <%= disciplina %>
                        </option>
                        <% } %>
                    </select>
                  </div>
                  <div class="campo d-flex flex-column">
                    <label for="primeiro-semestre"
                      >Nota do Primeiro Semestre:</label
                    >
                    <input
                      name="nota1"
                      type="number"
                      step="0.01"
                      id="primeiro-semestre"
                      min="0"
                      max="10"
                      placeholder="Ex: 6,70"
                      required
                    />
                  </div>
                  <div class="campo d-flex flex-column">
                    <label for="segundo-semestre"
                      >Nota do Segundo Semestre:</label
                    >
                    <input
                      name="nota2"
                      type="number"
                      step="0.01"
                      id="segundo-semestre"
                      min="0"
                      max="10"
                      placeholder="Ex: 8,59"
                    />
                  </div>
                  <div class="campo d-flex flex-column">
                    <label for="media">Média Final:</label>
                    <input
                      type="number"
                      step="0.01"
                      id="media"
                      min="0"
                      max="10"
                      disabled
                    />
                  </div>
                </div>

                <div class="opcoes d-flex">
                  <div class="save-container">
                    <button class="save" type="submit">Criar</button>
                  </div>

                  <div class="return-button">
                    <a href="${pageContext.request.contextPath}/boletim?action=read&usuario=professor&id_aluno=<%=aluno.getIdAluno()%>">Cancelar</a>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
