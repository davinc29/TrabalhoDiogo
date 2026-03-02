<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Senha Redefinida</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recuperar-senha.css">
</head>
<body>

<div class="card" style="text-align: center;">

    <div style="font-size: 56px; margin-bottom: 16px;">✅</div>

    <h1 style="color: #27ae60;">Senha redefinida!</h1>

    <p style="color: #666; margin: 12px 0 28px;">
        Sua senha foi alterada com sucesso.<br>
        Faça login com a nova senha.
    </p>

    <a href="${pageContext.request.contextPath}/index.jsp" style="text-decoration: none;">
        <button class="btn-primary">Ir para o Login</button>
    </a>

</div>

</body>
</html>
