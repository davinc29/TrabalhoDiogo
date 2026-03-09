document.addEventListener("DOMContentLoaded", function () {

    const camposSenha = document.querySelectorAll(".validar-senha");

    function validarForcaSenha(senha) {
        const temTamanho = senha.length >= 8;
        const temLetra = /[a-zA-Z]/.test(senha);
        const temNumero = /[0-9]/.test(senha);
        const temEspecial = /[^A-Za-z0-9]/.test(senha);

        return {
            temTamanho,
            temLetra,
            temNumero,
            temEspecial,
            valida: temTamanho && temLetra && temNumero && temEspecial
        };
    }

    camposSenha.forEach(function (senhaInput) {
        const form = senhaInput.form;
        if (!form) return;

        const aviso = document.createElement("div");

        aviso.style.position = "absolute";
        aviso.style.top = "100%";
        aviso.style.left = "50%";
        aviso.style.transform = "translateX(-50%)";
        aviso.style.marginTop = "8px";
        aviso.style.fontSize = "14px";
        aviso.style.fontWeight = "500";
        aviso.style.color = "#cc2a41";
        aviso.style.background = "#ffe5e8";
        aviso.style.padding = "6px 12px";
        aviso.style.borderRadius = "8px";
        aviso.style.whiteSpace = "nowrap";
        aviso.style.boxShadow = "0 2px 6px rgba(0,0,0,0.15)";
        aviso.style.display = "none";
        aviso.style.zIndex = "10";

        aviso.innerHTML = `
            <span class="req-tamanho" style="color:#cc2a41;">8 caracteres</span> -
            <span class="req-letra" style="color:#cc2a41;">letra</span> -
            <span class="req-numero" style="color:#cc2a41;">número</span> -
            <span class="req-especial" style="color:#cc2a41;">caractere especial</span>
        `;

        senhaInput.parentNode.style.position = "relative";
        senhaInput.parentNode.appendChild(aviso);

        const tamanho = aviso.querySelector(".req-tamanho");
        const letra = aviso.querySelector(".req-letra");
        const numero = aviso.querySelector(".req-numero");
        const especial = aviso.querySelector(".req-especial");

        function atualizarSenha() {
            const senha = senhaInput.value;
            const resultado = validarForcaSenha(senha);

            if (senha.length === 0) {
                aviso.style.display = "none";
                senhaInput.style.border = "";
                return;
            }

            aviso.style.display = resultado.valida ? "none" : "block";

            tamanho.style.color = resultado.temTamanho ? "green" : "#cc2a41";
            letra.style.color = resultado.temLetra ? "green" : "#cc2a41";
            numero.style.color = resultado.temNumero ? "green" : "#cc2a41";
            especial.style.color = resultado.temEspecial ? "green" : "#cc2a41";

            senhaInput.style.border = resultado.valida ? "2px solid green" : "2px solid #cc2a41";
        }

        senhaInput.addEventListener("input", atualizarSenha);

        form.addEventListener("submit", function (event) {
            const resultado = validarForcaSenha(senhaInput.value);

            if (!resultado.valida) {
                event.preventDefault();
                atualizarSenha();
                senhaInput.focus();

                const erroSenha = document.getElementById("erroSenha");
                if (erroSenha) {
                    erroSenha.innerText = "A senha deve ter 8 caracteres, letra, número e caractere especial.";
                }

                return;
            }

            const novaSenha = document.getElementById("novaSenha");
            const confirmarSenha = document.getElementById("confirmarSenha");
            const erroSenha = document.getElementById("erroSenha");

            if (novaSenha && confirmarSenha && erroSenha) {
                if (novaSenha.value !== confirmarSenha.value) {
                    event.preventDefault();
                    erroSenha.innerText = "Senha nova e confirmar senha devem ser iguais";
                    confirmarSenha.style.border = "2px solid #cc2a41";
                    confirmarSenha.focus();
                    return;
                } else {
                    erroSenha.innerText = "";
                    confirmarSenha.style.border = "";
                }
            }
        });
    });
});