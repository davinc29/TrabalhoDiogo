<!doctype html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
    />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/login.css" />
    <link rel="icon" type="image/x-icon" href="assets/Capelus-icon.ico">
  </head>
  <body>
    <!-- d-lg-none = esconde para computador -->
    <!-- d-none d-sm-flex = esconde para celular -->

    <!-- Layout Computer -->
    <section class="d-sm-flex align-items-center vh-100 d-none">
      <div class="login-container container-md">
        <form action="${pageContext.request.contextPath}/sistema-filter?action=login" method="post">
          <div class="mb-3">
            <h1 class="fs-1 fw-bold text-secondary"><a href="portal-adm/administradores/administradores.html" style="text-decoration: none; color: white;">Login</a></h1>
            <p class="fs-5 fw-semibold text-secondary">
              Preencha os dados abaixo:
            </p>
          </div>

          <div class="mb-3 d-flex flex-column gap-3 text-secondary">
            <div class="d-flex flex-column">
              <label for="email" class="form-label">Email</label>
              <input required type="email" class="form-control" id="email" name="email"/>
            </div>

            <div class="mb-3 d-flex flex-column">
              <label for="password" class="form-label">Senha</label>
              <input required type="password" class="form-control" id="password" name="senha" autocomplete="off"/>
            </div>

            <div class="button-text-box d-flex flex-column text-center">
              <button type="submit" class="btn btn-secondary opacity-75">
                Entrar
              </button>
              <div class="cadastro-box d-flex justify-content-center">
                <p>
                  Não tem conta?
                  <a href="${pageContext.request.contextPath}/cadastro-aluno" class="text-decoration-none text-primary fw-bold"
                    >Cadastrar-se</a>
                    <br>
                    <a href="${pageContext.request.contextPath}/recuperar-senha?action=solicitar">
                        Esqueci minha senha
                    </a>
                </p>
              </div>
            </div>
          </div>
        </form>
      </div>

      <div class="text-container container-md">
        <h2 class="fs-1 fw-bold">
          <span class="text-secondary">Bem-</span>Vindo ao <br />
          <span class="text-secondary">Uni</span>code
        </h2>
        <div class="image">
          <img src="assets/login-image.svg" alt="Homem lendo um livro" />
        </div>
      </div>
    </section>

    <!-- Layout Mobile -->
    <section
      class="d-flex flex-column align-items-center vh-100 w-100 d-lg-none text-primary justify-content-center"
    >
      <div class="title-box">
        <h1 class="fw-bold title">Bem-Vindo <br />ao Unicode</h1>
      </div>

      <div class="d-flex flex-column w-75">
        <button class="btn btn-primary rounded-pill">
          <a
            href="mobile_login.html"
            class="fs-2 text-decoration-none text-secondary"
            >Entrar</a
          >
        </button>
        <button class="btn btn-secondary rounded-pill fs-2">
          Criar uma conta
        </button>
      </div>
    </section>
  </body>
</html>
