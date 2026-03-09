<%@ page import="com.dto.AlunoViewDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.ObservacaoDAO" %>
<%@ page import="com.dto.ObservacaoViewDTO" %>
<%@ page import="com.dao.BoletimDAO" %>
<%@ page import="com.dto.BoletimViewDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AlunoViewDTO aluno = (AlunoViewDTO) session.getAttribute("usuario");

    session.setAttribute("matricula", aluno.getMatricula());
    session.setAttribute("turma", aluno.getTurma_ano());
    session.setAttribute("usuario", aluno);

    HttpSession sessao = request.getSession();
    session.setAttribute("usuario", aluno);

    String matricula = String.valueOf(aluno.getMatricula());
    session.setAttribute("matricula", matricula);

    String turma = aluno.getTurma_ano();
    session.setAttribute("turma", turma);

    String nome = aluno.getNome();
    nome = (nome == null) ? "" : nome.trim();

    String nome2L = "";

    if (!nome.isEmpty()) {
        String[] partesNome = nome.split("\\s+");
        char letra1nome = partesNome[0].charAt(0);
        char letra2nome = (partesNome.length > 1) ? partesNome[partesNome.length - 1].charAt(0) : partesNome[0].charAt(0);
        nome2L = ("" + letra1nome + letra2nome).toUpperCase();
    }

    session.setAttribute("nome2L", nome2L);
    session.setAttribute("nome", nome);

    ObservacaoDAO observacaoDAO = new ObservacaoDAO();
    List<ObservacaoViewDTO> observacoes = observacaoDAO.listarPorAluno(aluno.getIdAluno());

    BoletimDAO boletimDAO = new BoletimDAO();
    List<BoletimViewDTO> boletim = boletimDAO.listarPorAluno(aluno.getIdAluno());

    LocalDate hoje = LocalDate.now();
    Locale brasil = new Locale("pt","BR");
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("EEEE", brasil);
    String diaSemana = hoje.format(formatador);
    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);

    Integer diaNum = hoje.getDayOfMonth();

    List<String> meses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez");
    String mes = meses.get(hoje.getMonthValue()-1);

    Integer ano = hoje.getYear();

    String data = String.format("%d %s %d", diaNum, mes, ano);

    session.setAttribute("data", data);
    session.setAttribute("diaSemana", diaSemana);
%>

<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Home</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-aluno/home.css" />
    <script src="${pageContext.request.contextPath}/mobile-navbar.js"></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico" />
</head>
<body>
<section class="d-sm-flex align-items-center vh-100 d-none home">
    <aside class="bg-primary sidebar">
        <nav class="text-secondary">
            <ul class="">
                <li class="page-item active">
                    <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-aluno/home.jsp">Home</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-aluno/boletim.jsp">Boletim</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/jsp/portal-aluno/observacoes.jsp">Observações</a>
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
                <p class="m-3 mt-4 fs-5 fw-bold text-primary"><%=session.getAttribute("nome")%></p>
            </div>
        </header>

        <main>
            <div class="box-one d-flex justify-content-between mb-5">
                <div class="ms-5">
                    <h1 class="fs-1 fw-bold">Olá, <%=session.getAttribute("nome")%></h1>
                    <p class="fs-3">
                        Pronto para começar seu dia com alguns<br />feedbacks?
                    </p>
                </div>
                <img
                        class="teacher"
                        src="${pageContext.request.contextPath}/assets/Professor-figure.svg"
                        alt="Professora figure"
                />
            </div>

            <div class="box-container d-flex justify-content-around">
                <div class="box-two">
                    <h2 class="fs-4 fw-bold text-center">Minhas Observações</h2>
                    <%
                        if (observacoes == null || observacoes.isEmpty()) {
                    %>
                    <p class="text-center mt-4">Nenhuma observação encontrada.</p>
                    <%
                    } else {
                    %>
                    <div class="turma-container d-flex">
                        <div class="coluna-esquerda">
                            <%
                                int contador = 0;
                                for (ObservacaoViewDTO observacao : observacoes) {
                                    if (contador > 1) break;
                            %>
                            <div class="turma-box">
                                <h2 class="correction-text"><%=observacao.getNomeDisciplina()%></h2>
                                <h3 class="correction-text"><%=observacao.getNomeAluno()%> / <%=observacao.getTurmaAno()%></h3>
                                <p class="correction-text"><%=observacao.getObservacao()%></p>
                            </div>
                            <%
                                    contador++;
                                }
                            %>
                        </div>

                        <div class="coluna-direita">
                            <%
                                contador = 0;
                                for (ObservacaoViewDTO observacao : observacoes) {
                                    if (contador <= 1) {
                                        contador++;
                                        continue;
                                    }
                                    if (contador > 3) break;
                            %>
                            <div class="turma-box">
                                <h2 class="correction-text"><%=observacao.getNomeDisciplina()%></h2>
                                <h3 class="correction-text"><%=observacao.getNomeAluno()%> - <%=observacao.getTurmaAno()%></h3>
                                <p class="correction-text"><%=observacao.getObservacao()%></p>
                            </div>
                            <%
                                    contador++;
                                }
                            %>
                        </div>
                    </div>

                    <div class="d-flex justify-content-end me-4">
                        <a href="${pageContext.request.contextPath}/jsp/portal-aluno/observacoes.jsp"
                           class="text-decoration-none" style="color:black">Ver mais ></a>
                    </div>
                    <%
                        }
                    %>
                </div>

                <div class="box-column w-50 ms-4">
                    <div class="box-three">
                        <h2 class="fs-4 fw-bold text-center mb-4">Meu Boletim</h2>
                        <%
                            if (boletim == null || boletim.isEmpty()) {
                        %>
                        <p class="text-center mt-4">Nenhuma nota encontrada.</p>
                        <%
                        } else {
                        %>
                        <table class="w-100">
                            <tr>
                                <th>Disciplina</th>
                                <th>Nota 1</th>
                                <th>Nota 2</th>
                                <th>Média</th>
                                <th>Situação</th>
                            </tr>
                            <%
                                int contadorBoletim = 0;
                                for (BoletimViewDTO b : boletim) {
                                    if (contadorBoletim > 2) break;
                            %>
                            <tr>
                                <td><p><%= b.getNomeDisciplina() %></p></td>

                                <td>
                                    <p style="color:<%= b.getNota1() >= 7 ? "green" : "red" %>; font-weight:bold;">
                                        <%= b.getNota1() %>
                                    </p>
                                </td>

                                <td>
                                    <p style="color:<%= b.getNota2() >= 7 ? "green" : "red" %>; font-weight:bold;">
                                        <%= b.getNota2() %>
                                    </p>
                                </td>

                                <td>
                                    <p style="color:<%= b.getMedia() >= 7 ? "green" : "red" %>; font-weight:bold;">
                                        <%= b.getMedia() %>
                                    </p>
                                </td>

                                <td>
                                    <p style="color:<%= b.getSituacao().equalsIgnoreCase("Aprovado") ? "green" : "red" %>; font-weight:bold;">
                                        <%= b.getSituacao() %>
                                    </p>
                                </td>
                            </tr>
                            <%
                                    contadorBoletim++;
                                }
                            %>
                        </table>

                        <div class="d-flex justify-content-end me-4">
                            <a href="${pageContext.request.contextPath}/jsp/portal-aluno/boletim.jsp"
                               class="text-decoration-none" style="color:black">Ver mais ></a>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </main>
    </div>
</section>

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