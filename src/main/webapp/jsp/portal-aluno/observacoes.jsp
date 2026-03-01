<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Observações</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="../../css/style.css" />
    <link rel="stylesheet" href="../../css/portal-aluno/observacoes.css" />
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
            <li class="page-item active">
              <a class="page-text" href="observacoes.html">Observações</a>
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
          <div class="filter-box d-flex flex-column">
            <div class="linha-cima d-flex">
              <div class="filter-name">
                <input type="text" placeholder="Buscar por professor..." />
              </div>
              <div class="filter-name ms-4">
                <input
                  type="text"
                  placeholder="Buscar por disciplina..."
                />
              </div>
              <div class="filter-name ms-4">
                <input
                  type="text"
                  placeholder="Buscar por observação..."
                />
              </div>
            </div>
            <div class="linha-baixo d-flex mt-3">
              <div class="filter-button">
                <button>Aplicar Filtro</button>
              </div>
            </div>
          </div>

          <div class="tabela-container">
            <table class="tabela-observacoes">
              <tr>
                <th>Professor</th>
                <th>Disciplina</th>
                <th>Observação</th>
              </tr>
              <tr>
                <td>
                  <p>Rahquel Korzh</p>
                </td>
                <td>
                  <p>Português</p>
                </td>
                <td class="coluna-observacao">
                  <p>
                    Você precisa melhorar, pois vc é muito ruim, Meu Deus HAHA
                    HAA AHAHAH AHAHAH AHAHAH AHA HAHA HAH HAHAH AHAH HAHA HA HA
                    HA HA HAH AH AH AHHA HA HAH AH HA
                  </p>
                </td>
              </tr>
            </table>
          </div>
        </main>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section class="vh-100 w-100 d-lg-none"></section>
  </body>
</html>
