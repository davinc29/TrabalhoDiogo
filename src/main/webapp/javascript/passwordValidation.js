function validarSenha(event) {

    let senhaNova = document.getElementById("novaSenha");
    let confirmarSenha = document.getElementById("confirmarSenha");
    let erro = document.getElementById("erroSenha");

    const senha = senhaNova.value;

    const temTamanho = senha.length >= 8;
    const temLetra = /[a-zA-Z]/.test(senha);
    const temNumero = /[0-9]/.test(senha);

    if (!(temTamanho && temLetra && temNumero)) {
        event.preventDefault();
        erro.innerText = "A senha deve ter 8 caracteres, letra e número.";
        senhaNova.focus();
        return;
    }

    if (senhaNova.value !== confirmarSenha.value) {
        event.preventDefault();
        erro.innerText = "Senha nova e confirmar senha devem ser iguais";
        confirmarSenha.style.border = "2px solid #cc2a41";
        confirmarSenha.focus();
        return;
    }

    erro.innerText = "";
    confirmarSenha.style.border = "";
}