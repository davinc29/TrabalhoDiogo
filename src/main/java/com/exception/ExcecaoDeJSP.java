package com.exception;

/*
 * Classe que cria excessões com mensagens específicas de acordo com a constraint violada, deixando
 * o retorno da excessão mais semântico e de claro entendimento da equipe interna.
 */

public class ExcecaoDeJSP extends Exception {

    public ExcecaoDeJSP(String message) {
        super(message);
    }

    public ExcecaoDeJSP(String message, Throwable cause) {
        super(message, cause);
    }

    // Constraint -> campo UNIQUE
    public static ExcecaoDeJSP campoUnicoDuplicado(String campo) {
        String msg = "Este %s já está em uso".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }

    // Chamadas do método 'campoUnicoDuplicado' em cada campo que tem essa constraint
    public static ExcecaoDeJSP cnpjDuplicado() {
        return campoUnicoDuplicado("CNPJ");
    }

    public static ExcecaoDeJSP emailDuplicado() {
        return campoUnicoDuplicado("Email");
    }

    public static ExcecaoDeJSP nomeDuplicado() {
        return campoUnicoDuplicado("Nome");
    }

    // Constraint -> .{8,}
    public static ExcecaoDeJSP senhaCurta(int tamanhoMinimo) {
        String msg = "Sua senha deve ter pelo menos %d caracteres.".formatted(tamanhoMinimo);
        return new ExcecaoDeJSP(msg);
    }

    // Constraint -> campo NOT NULL
    public static ExcecaoDeJSP campoNecessarioFaltante(String campo) {
        String msg = "Por favor, preencha o campo %s.".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }

    // Constraint -> senha ou email diferentes do acesso ao banco de dados
    public static ExcecaoDeJSP falhaLogin() {
        return new ExcecaoDeJSP("Login falhou. Verifique se Email e Senha.");
    }

    // Constraint -> senha diferente da registrada no banco de dados
    public static ExcecaoDeJSP senhaIncorreta() {
        return new ExcecaoDeJSP("Senha incorreta. Tente novamente.");
    }

    // Constraint -> tipo_valor != tipo_campo
    public static ExcecaoDeJSP valorInvalido(String campo) {
        String msg = "O valor informado para %s é inválido.".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }

    public static ExcecaoDeJSP textoMuitoLongo(String campo) {
        String msg = "O valor informado para %s é muito longo".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }

    public static ExcecaoDeJSP emailNaoCadastrado() {
        return new ExcecaoDeJSP("E-mail não encontrado. Verifique e tente novamente.");
    }

    public static ExcecaoDeJSP codigoOtpInvalido() {
        return new ExcecaoDeJSP("Código inválido ou expirado. Solicite um novo código.");
    }

    public static ExcecaoDeJSP senhasNaoCoincidem() {
        return new ExcecaoDeJSP("As senhas não coincidem. Verifique e tente novamente.");
    }

    public static ExcecaoDeJSP acessoInvalidoAoFluxoOtp() {
        return new ExcecaoDeJSP("Sessão expirada ou acesso inválido. Reinicie o processo.");
    }
}
