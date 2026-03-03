<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Senha</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recuperar-senha.css">
</head>
<body>

<div class="card">

    <h1>Recuperar Senha</h1>
    <p class="subtitulo">
        Informe seu e-mail cadastrado. Enviaremos um código de verificação.
    </p>

    <% if (request.getAttribute("erro") != null) { %>
    <div class="erro">
        <%= request.getAttribute("erro") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="action" value="solicitar" />

        <div class="campo-grupo">
            <label for="email">E-mail</label>
            <input
                type="email"
                id="email"
                name="email"
                placeholder="seu@email.com"
                required
                autofocus
            />
        </div>

        <button type="submit" class="btn-primary">Enviar código</button>
    </form>

    <div class="link-voltar">
        <a href="${pageContext.request.contextPath}/index.jsp">← Voltar ao login</a>
    </div>

</div>

</body>
</html>
