<%@ page import="com.dto.ProfessorDTO" %>
<%@ page import="com.model.Professor" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dto.ObservacaoViewDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Pegando dados diretos do banco
    ProfessorDTO professor = (ProfessorDTO) session.getAttribute("usuario");
    List<String> notasPendentes = (List<String>) request.getAttribute("notasPendentes");
    List<ObservacaoViewDTO> observacoes = (List<ObservacaoViewDTO>) request.getAttribute("observacoes");

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
    <title>Capelus - Home</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-professor/home.css" />
    <script src="${pageContext.request.contextPath}/javascript/mobile-navbar.js"></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico">
  </head>
  <body>
    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none home">
      <aside class="bg-primary sidebar">
        <nav class="text-secondary">
          <ul class="">
            <li class="page-item active">
              <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-professor/home.jsp">Home</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/alunos-professor?action=notas">Notas</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="${pageContext.request.contextPath}/alunos-professor?action=observacoes">Observações</a>
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
            <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=professor.getNome()%></p>
          </div>
        </header>
        <main>
          <div class="box-one d-flex justify-content-between mb-5">
            <div class="ms-5">
              <h1 class="fs-1 fw-bold">Olá, <%=professor.getNome()%>!</h1>
              <p class="fs-3">
                Pronto para começar seu dia dando alguns<br />feedbacks?
              </p>
            </div>
            <img
              class="teacher"
              src="${pageContext.request.contextPath}/assets/professora-figure.svg"
              alt="Professora figure"
            />
          </div>

          <div class="box-container d-flex justify-content-around">
            <div class="box-two">
              <h2 class="fs-4 fw-bold text-center">Minhas Observações</h2>
                <div class="turma-container d-flex">
                  <div class="coluna-esquerda">
                      <%%>
                      <%
                            int contador = 0;
                            for (ObservacaoViewDTO observacao : observacoes) {
                                if (contador > 1) {
                                    break;
                                }
                      %>
                      <div class="turma-box">
                          <h2 class="correction-text"><%=observacao.getNomeDisciplina()%></h2>
                          <h3 class="correction-text"><%=observacao.getNomeAluno()%> / <%=observacao.getTurmaAno()%></h3>
                          <p class="correction-text"><%=observacao.getObservacao()%></p>
                      </div>
                      <%contador++;}%>
                  </div>

                  <div class="coluna-direita">
                      <%
                          contador = 0;
                          for (ObservacaoViewDTO observacao : observacoes) {
                              if (contador <= 1) {
                                  contador++;
                                  continue;
                              } else if (contador > 3) {
                                  break;
                              }
                      %>
                      <div class="turma-box">
                          <h2 class="correction-text"><%=observacao.getNomeDisciplina()%></h2>
                          <h3 class="correction-text"><%=observacao.getNomeAluno()%> - <%=observacao.getTurmaAno()%></h3>
                          <p class="correction-text"><%=observacao.getObservacao()%></p>
                      </div>
                      <%contador++;}%>
                  </div>
                </div>

                <div class="d-flex justify-content-end me-4">
                  <a href="observacoes.jsp" class="text-decoration-none" style="color: black">Ver mais ></a>
                </div>
            </div>

            <div class="box-column w-50 ms-4">
              <div class="box-three">
                <h2 class="fs-4 fw-bold text-center mb-4">Notas pendentes</h2>
                <div class="d-flex check-list">
                  <div>
                      <%
                          contador = 0;
                          for (String notaPendente : notasPendentes) {
                              if (contador > 5) {
                                  break;
                              }
                      %>

                      <div class="d-flex">
                          <div class="box-empty"></div>
                          <p class="ms-2 fw-bold"><%=notaPendente%></p>
                      </div>

                      <%contador++;}%>
                  </div>
                </div>

                <div class="d-flex justify-content-end me-4">
                  <a href="notas.jsp" class="text-decoration-none" style="color: black">Ver mais ></a>
                </div>

              </div>
            </div>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none">
      <header>
        <nav class="text-secondary">
          <div class="mobile-menu">
            <div class="line1"></div>
            <div class="line2"></div>
            <div class="line3"></div>
          </div>
          <ul class="nav-list">
            <li class="page-item active">
              <a class="page-text" href="#">Home</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="#">Boletim</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="#">Observações</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="#">Notificações</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="#">Conta</a>
            </li>
          </ul>
        </nav>
      </header>
    </section>
  </body>
</html>
