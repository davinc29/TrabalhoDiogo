<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Senha</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recuperar-senha.css">
</head>
<body>

<div class="card">

    <h1>Criar Nova Senha</h1>
    <p class="subtitulo">
        Escolha uma senha com pelo menos 8 caracteres.
    </p>

    <% if (request.getAttribute("erro") != null) { %>
    <div class="erro">
        <%= request.getAttribute("erro") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/recuperar-senha" method="post" id="formSenha">
        <input type="hidden" name="action" value="redefinir" />

        <div class="campo-grupo">
            <label for="novaSenha">Nova senha</label>
            <input
                type="password"
                id="novaSenha"
                name="novaSenha"
                placeholder="Mínimo 8 caracteres"
                minlength="8"
                required
                autofocus
            />
        </div>

        <div class="campo-grupo">
            <label for="confirmaSenha">Confirmar nova senha</label>
            <input
                type="password"
                id="confirmaSenha"
                name="confirmaSenha"
                placeholder="Repita a senha"
                minlength="8"
                required
            />
            <span id="erroSenhaInline" style="color:#c0392b; font-size:12px; display:none;">
                As senhas não coincidem.
            </span>
        </div>

        <button type="submit" class="btn-primary" id="btnSalvar">Salvar nova senha</button>
    </form>

</div>

<script>
    const nova = document.getElementById("novaSenha");
    const confirma = document.getElementById("confirmaSenha");
    const erro = document.getElementById("erroSenhaInline");
    const btn = document.getElementById("btnSalvar");

    function checar() {
        if (confirma.value && nova.value !== confirma.value) {
            erro.style.display = "block";
            btn.disabled = true;
        } else {
            erro.style.display = "none";
            btn.disabled = false;
        }
    }

    nova.addEventListener("input", checar);
    confirma.addEventListener("input", checar);
</script>

</body>
</html>
