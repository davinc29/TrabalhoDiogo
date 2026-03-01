<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Capelus - Professores</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="../../css/style.css" />
    <link rel="stylesheet" href="../../css/admin/professores-editar.css" />
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
              <a
                class="page-text"
                href="administradores.jsp"
                >Administradores</a
              >
            </li>
            <li class="page-item can-hover">
              <a class="page-text" href="alunos.jsp">Alunos</a>
            </li>
            <li class="page-item active">
              <a class="page-text" href="professores.jsp">Professores</a>
            </li>
          </ul>
        </nav>
      </aside>

      <div class="w-100 m-5">
        <header class="d-flex w-100 justify-content-between">
          <div class="lh-1">
            <p class="fs-5 fw-bold">Portal do Administrador</p>
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
            <p class="m-3 mt-4 fs-5 fw-bold text-primary">Ryan Cursino</p>
          </div>
        </header>
        <main>
          <div class="aluno-card d-flex justify-content-between flex-column">
            <div class="d-flex justify-content-between">
              <div class="aluno-informacoes">
                <h2 class="aluno-nome fs-3">Ryan Cursino</h2>
                <div class="d-flex">
                  <p>Administrador</p>
                </div>
              </div>
            </div>

            <hr />
            <div class="input-container">
              <form action="">
                <div class="d-flex flex-column">
                  <div class="linha-cima d-flex">
                    <div class="campo d-flex flex-column">
                      <label for="primeiro-semestre">Id:</label>
                      <input type="text" id="turma-id" required disabled />
                    </div>
                    <div class="campo d-flex flex-column">
                      <label for="primeiro-semestre">Disciplina:</label>
                      <input
                        type="text"
                        id="turma-id"
                        placeholder="Ex: Matemática"
                        required
                      />
                    </div>
                    <div class="campo d-flex flex-column">
                      <label for="turma-id">Professor:</label>
                      <input
                        type="text"
                        id="turma-id"
                        placeholder="Ex: Kleber Onipotente"
                        required
                      />
                    </div>
                  </div>
                  <div class="linha-baixo d-flex mt-3">
                    <div class="campo d-flex flex-column">
                      <label for="primeiro-semestre">Usuário:</label>
                      <input
                        type="text"
                        id="turma-id"
                        placeholder="Ex: KleberNextstage"
                        required
                      />
                    </div>
                    <div class="campo d-flex flex-column">
                      <label for="segundo-semestre">Email:</label>
                      <input
                        type="text"
                        id="turma-id"
                        placeholder="Ex: kleber.oni@gmail.com"
                        required
                      />
                    </div>
                    <div class="campo d-flex flex-column">
                      <label for="media">Senha:</label>
                      <input
                        type="password"
                        id="turma-id"
                        placeholder="Ex: @Senha123"
                        required
                      />
                    </div>
                  </div>
                </div>

                <div class="opcoes d-flex">
                  <div class="save-container">
                    <button class="save" type="submit">Salvar</button>
                  </div>

                  <div class="return-button">
                    <a href="professores.jsp">Cancelar</a>
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
