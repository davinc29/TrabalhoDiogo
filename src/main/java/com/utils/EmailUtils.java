package com.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtils {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();
    private static final String EMAIL_REMETENTE = dotenv.get("EMAIL_REMETENTE");
    private static final String SENHA_SMTP = dotenv.get("SENHA_SMTP");

    public static void enviarCodigo(String destinatario, String codigo) throws MessagingException {
        // Configurações do servidor SMTP do Gmail
        // Porta 587 + STARTTLS = padrão seguro para envio autenticado
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session sessao = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMETENTE, SENHA_SMTP);
            }
        });

        // Mensagem de e-mail
        Message mensagem = new MimeMessage(sessao);
        mensagem.setFrom(new InternetAddress(EMAIL_REMETENTE));
        mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensagem.setSubject("Código de recuperação de senha");
        mensagem.setContent(montarCorpo(codigo), "text/html; charset=utf-8");

        // Despacha o e-mail para o servidor SMTP
        Transport.send(mensagem);
    }

    private static String montarCorpo(String codigo) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto;
                        padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;">
                <h1 style="color: #CC2a41;">Secretaria Capelus</h2>
                <h2 style="color: #333;">Recuperação de Senha</h2>
                <p style="color: #333;">Você solicitou a recuperação de senha. Use o código abaixo:</p>
                <div style="text-align: center; margin: 24px 0;">
                    <span style="font-size: 36px; font-weight: bold;
                                 letter-spacing: 8px; color: #CC2a41;">%s</span>
                </div>
                <p style="color: #666; font-size: 14px;">
                    ⏱️ Este código expira em <strong>5 minutos</strong>.
                </p>
                <p style="color: #999; font-size: 12px;">
                    Se você não solicitou a recuperação, ignore este e-mail.
                </p>
            </div>
            """.formatted(codigo);
    }
}
