<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/Capelus-icon.ico">
      <script defer src="${pageContext.request.contextPath}/javascript/passwordRequirements.js"></script>
    <title>Cadastro</title>
  </head>
  <body>
    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none">
      <div class="login-container container-md">
          <form action="${pageContext.request.contextPath}/cadastro-aluno" method="post">
              <input type="hidden" name="action" value="cadastrar">

              <div class="mb-3">
                  <h1 class="fs-1 fw-bold text-secondary">Cadastro</h1>
                  <p class="fs-5 fw-semibold text-secondary">
                      Preencha os dados abaixo:
                  </p>
              </div>

              <div class="mb-3 d-flex flex-column gap-3 text-secondary">
                  <div class="d-flex flex-column">
                      <label for="username" class="form-label">Nome</label>
                      <input type="text" class="form-control" id="username" name="nome" required />
                  </div>

                  <div class="d-flex flex-column">
                      <label for="matricula" class="form-label">Matrícula</label>
                      <input type="text" class="form-control" id="matricula" name="matricula" required />
                  </div>

                  <div class="d-flex flex-column">
                      <label for="email" class="form-label">Email</label>
                      <input type="email" class="form-control" id="email" name="email" required />
                  </div>

                  <div class="mb-3 d-flex flex-column">
                      <label for="senha" class="form-label">Senha</label>
                      <input type="password" class="form-control" id="senha" class="validar-senha" name="senha" required />
                  </div>

                  <div class="button-text-box d-flex flex-column text-center">
                      <button type="submit" class="btn btn-secondary opacity-75">
                          Cadastrar
                      </button>

                      <div class="cadastro-box d-flex justify-content-center">
                          <p>
                              Já tem conta?
                              <a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-primary fw-bold">Logar-se</a>
                          </p>
                      </div>
                  </div>
              </div>
          </form>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section
      class="d-flex flex-column align-items-center vh-100 w-100 d-lg-none text-primary justify-content-center"
    >
      <div class="header">
        <p>Unicode - Faculdade de Medicina</p>
      </div>

      <div class="logo p-5">
        <img src="assets/capelus-logo.svg" alt="Logo Unicode" />
          <div class="subtitle">
              <h2>Bem-Vindo de volta!</h2>
              <p>Tudo começa por você, e pra você.</p>
          </div>
      </div>

      <div class="login-container container-md w-75">
        <h2 class="fs-4 w-100 mb-3">Crie sua conta</h2>

        <form>
          <div class="d-flex flex-column gap-3">
            <div class="d-flex flex-column">
              <label for="username" class="form-label">Usuário</label>
              <input type="text" class="form-control" id="username" />
            </div>

            <div class="d-flex flex-column">
              <label for="email" class="form-label">Email</label>
              <input type="email" class="form-control" id="email" />
            </div>

            <div class="d-flex flex-column">
              <label for="password" class="form-label">Senha</label>
              <input type="password" class="form-control" id="password" />
            </div>

            <div class="button-text-box d-flex flex-column text-center mt-5">
              <button type="submit" class="btn btn-primary rounded-pill fs-5">
                Cadastrar
              </button>
              <p class="">
                Já tem conta?
                <a
                  href="mobile_login.html"
                  class="text-decoration-none text-primary fw-bold"
                >
                  Entre
                </a>
              </p>
            </div>
          </div>
        </form>
      </div>
    </section>
  </body>
</html>
