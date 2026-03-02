<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verificar Código</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recuperar-senha.css">
</head>
<body>

<div class="card">

    <h1>Verificar Código</h1>
    <p class="subtitulo">
        Enviamos um código de 6 dígitos para <strong id="emailMascarado"></strong>.
        Ele expira em 5 minutos.
    </p>

    <% if (request.getAttribute("erro") != null) { %>
    <div class="erro">
        <%= request.getAttribute("erro") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="action" value="validar" />

        <div class="campo-grupo">
            <label for="codigo">Código de verificação</label>
            <input
                type="text"
                id="codigo"
                name="codigo"
                class="input-codigo"
                placeholder="000000"
                maxlength="6"
                pattern="[0-9]{6}"
                required
                autofocus
                autocomplete="off"
            />
        </div>

        <button type="submit" class="btn-primary">Verificar código</button>
    </form>

    <div class="link-voltar">
        <a href="${pageContext.request.contextPath}/recuperar-senha?action=solicitar">
            ← Solicitar novo código
        </a>
    </div>

</div>

<script>
    // Máscara de e-mail para exibição: "usuario@escola.com" → "usua***@escola.com"
    const emailCompleto = "${sessionScope.otpEmail}";

    function mascararEmail(email) {
        const partes = email.split("@");
        if (partes.length !== 2) return email;
        const visiveis = partes[0].substring(0, Math.min(4, partes[0].length));
        return visiveis + "***@" + partes[1];
    }

    document.getElementById("emailMascarado").textContent = mascararEmail(emailCompleto);

    // Aceita apenas dígitos no campo do código
    document.getElementById("codigo").addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").substring(0, 6);
    });
</script>

</body>
</html>
