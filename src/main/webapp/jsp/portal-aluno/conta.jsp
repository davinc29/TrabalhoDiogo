<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <link rel="stylesheet" href="../../css/style.css" />
    <link rel="stylesheet" href="../../css/portal-aluno/conta.css" />
    <script src="mobile-navbar.js"></script>
    <link rel="icon" type="image/x-icon" href="../../assets/Capelus-icon.ico" />
  </head>
  <body>
    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none home">
      <aside class="bg-primary sidebar">
        <nav class="text-secondary">
          <ul class="">
            <li class="page-item can-hover">
              <a class="page-text" href="home.jsp">Home</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="boletim.jsp">Boletim</a>
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="observacoes.jsp"
                >Observações</a
              >
            </li>
            <li class="page-item active">
              <a class="page-text" href="conta.html">Conta</a>
            </li>
          </ul>
        </nav>
      </aside>

      <div class="w-100 m-5">
        <header class="d-flex w-100 justify-content-between">
          <div class="lh-1">
            <p class="fs-5 fw-bold">Portal do Estudante</p>
            <p class="fs-5 text-primary">
              <span class="fw-bold">Quarta-Feira</span>, 04 Fev 2026
            </p>
          </div>
          <div class="d-flex">
            <img
              class="icon m-3"
              src="../../assets/notificao-icon.svg"
              alt="Notificações Icon"
            />
            <img
              class="icon m-3"
              src="../../assets/mensagens-icon.svg"
              alt="Mensagens Icon"
            />
            <div class="bg-primary box-name m-3">
              <p class="fs-4 fw-bold text-secondary">RE</p>
            </div>
            <p class="m-3 mt-4 fs-5 fw-bold text-primary">Gustavo Kenzo</p>
          </div>
        </header>
        <main>
          <div class="account-card">
            <div class="informacoes-topo">
              <h2>Gustavo Kenzo Shirahata Ota</h2>
              <div class="matricula-turma d-flex justify-content-between">
                <p><span class="fw-bold">Matrícula: </span>1234567890</p>
                <p><span class="fw-bold">Turma: </span>1ºJ</p>
              </div>
            </div>

            <div class="campos">
              <div class="email d-flex flex-column mb-4">
                <label for="email-id">E-mail</label>
                <input type="text" id="email-id" required disabled />
              </div>
              <div class="senha d-flex flex-column">
                <label for="senha-id">Senha</label>
                <input type="password" id="senha-id" required disabled />
              </div>
            </div>

            <div class="edit-container justify-content-between">
              <div class="edit-button">
                <a href="../../index.jsp">Sair</a>
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
