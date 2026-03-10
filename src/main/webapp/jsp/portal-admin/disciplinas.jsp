<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dto.DisciplinaViewDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    LocalDate hoje = LocalDate.now();
    Locale brasil = new Locale("pt", "BR");
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("EEEE", brasil);
    String diaSemana = hoje.format(formatador);
    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);

    Integer diaNum = hoje.getDayOfMonth();
    List<String> meses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez");
    String mes = meses.get(hoje.getMonthValue() - 1);
    Integer ano = hoje.getYear();

    String data = String.format("%d %s %d", diaNum, mes, ano);

    session.setAttribute("data", data);
    session.setAttribute("diaSemana", diaSemana);

    List<DisciplinaViewDTO> disciplinas = (List<DisciplinaViewDTO>) request.getAttribute("disciplinas");
%>

<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Disciplinas</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal-admin/disciplinas.css" />
    <script src="${pageContext.request.contextPath}/mobile-navbar.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/javascript/delete.js" defer></script>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico">
</head>
<body>
<section class="d-sm-flex align-items-center vh-100 d-none home">
    <aside class="bg-primary sidebar">
        <nav class="text-secondary">
            <ul>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readAlunos">Alunos</a>
                </li>
                <li class="page-item can-hover">
                    <a class="page-text" href="${pageContext.request.contextPath}/admin?action=readProfessores">Professores</a>
                </li>
                <li class="page-item active">
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
                    <span class="fw-bold"><%=session.getAttribute("diaSemana")%></span>, <%=session.getAttribute("data")%>
                </p>
            </div>

            <div class="d-flex">
                <img class="icon m-3" src="${pageContext.request.contextPath}/assets/notificao-icon.svg" alt="Notificações Icon" />
                <img class="icon m-3" src="${pageContext.request.contextPath}/assets/mensagens-icon.svg" alt="Mensagens Icon" />
                <div class="bg-primary box-name m-3 position-relative">
                    <a href="${pageContext.request.contextPath}/index.jsp" id="logoutPopup" class="logout-popup">Logout</a>
                    <p class="fs-4 fw-bold text-secondary" id="botaoLogout" style="cursor:pointer;">ADM</p>
                </div>
            </div>
        </header>

        <main>
            <form action="${pageContext.request.contextPath}/admin" method="get">
                <input type="hidden" name="action" value="readDisciplinas" />

                <div class="filter-box d-flex flex-column">
                    <div class="linha-um d-flex">
                        <div class="filter-name">
                            <input type="text" name="nome" placeholder="Buscar por nome da disciplina..." />
                        </div>
                        <div class="filter-name ms-4">
                            <input type="text" name="id" placeholder="Buscar por id da disciplina..." />
                        </div>
                        <div class="filter-name ms-4">
                            <input type="text" name="nomeProfessor" placeholder="Buscar por nome do professor..." />
                        </div>
                    </div>

                    <div class="linha-tres d-flex mt-3 justify-content-between">
                        <div class="d-flex lado-esquerdo">
                            <div class="filter-button">
                                <button type="submit">Aplicar Filtro</button>
                            </div>
                        </div>

                        <div class="d-flex lado-direito">
                            <div class="add-button">
                                <a href="${pageContext.request.contextPath}/admin?action=addDisciplina">+ Adicionar</a>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <div class="tabela-container">
                <table class="tabela-notas">
                    <tr>
                        <th>Id</th>
                        <th>Disciplina</th>
                        <th>Professor</th>
                        <th>Email Professor</th>
                        <th>Ações</th>
                    </tr>

                    <%
                        if (disciplinas != null && !disciplinas.isEmpty()) {
                            for (DisciplinaViewDTO disciplina : disciplinas) {
                    %>
                    <tr>
                        <td><p><%=disciplina.getId()%></p></td>
                        <td><p><%=disciplina.getNomeDisciplina()%></p></td>
                        <td><p><%=disciplina.getNomeProfessor()%></p></td>
                        <td><p><%=disciplina.getEmailProfessor()%></p></td>
                        <td class="action-box">
                            <form action="<%=request.getContextPath()%>/admin" method="get">
                                <input type="hidden" name="action" value="editDisciplina" />
                                <input type="hidden" name="id" value="<%=disciplina.getId()%>" />
                                <button type="submit" class="action-btn">
                                    <img class="table-icon" src="<%=request.getContextPath()%>/assets/editar.svg" alt="Editar Icon" />
                                </button>
                            </form>

                            <form action="<%=request.getContextPath()%>/admin" method="post" onsubmit="confirmarDelete(event)">
                                <input type="hidden" name="action" value="deleteDisciplina" />
                                <input type="hidden" name="id" value="<%=disciplina.getId()%>" />
                                <button type="submit" class="action-btn">
                                    <img class="table-icon" src="<%=request.getContextPath()%>/assets/apagar.svg" alt="Deletar Icon" />
                                </button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="5"><p>Nenhuma disciplina encontrada.</p></td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
        </main>
    </div>
</section>

<section class="vh-100 w-100 d-lg-none"></section>

<script>
    const botaoLogout = document.getElementById("botaoLogout");
    const logoutPopup = document.getElementById("logoutPopup");

    botaoLogout.addEventListener("click", () => {
        if (logoutPopup.style.display === "block") {
            logoutPopup.style.display = "none";
        } else {
            logoutPopup.style.display = "block";
        }
    });
</script>

</body>
</html>