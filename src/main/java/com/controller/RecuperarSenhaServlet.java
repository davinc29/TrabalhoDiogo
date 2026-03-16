package com.controller;

import com.dao.AlunoDAO;
import com.dao.OtpDAO;
import com.dao.ProfessorDAO;
import com.exception.ExcecaoDeJSP;
import com.utils.EmailUtils;
import com.utils.SenhaUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/recuperar-senha")
public class RecuperarSenhaServlet extends HttpServlet {

    private static final String PAGINA_SOLICITAR = "/jsp/recuperar-senha/solicitar.jsp";
    private static final String PAGINA_VALIDAR   = "/jsp/recuperar-senha/validar-codigo.jsp";
    private static final String PAGINA_REDEFINIR = "/jsp/recuperar-senha/redefinir-senha.jsp";
    private static final String PAGINA_SUCESSO   = "/jsp/recuperar-senha/sucesso.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        action = (action == null ? "solicitar" : action.trim());

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "solicitar" -> destino = PAGINA_SOLICITAR;

                case "validar" -> {
                    verificarSessaoEmail(req);
                    destino = PAGINA_VALIDAR;
                }

                case "redefinir" -> {
                    verificarSessaoCodigo(req);
                    destino = PAGINA_REDEFINIR;
                }

                case "sucesso" -> destino = PAGINA_SUCESSO;  // ← adiciona isso
            }

            erro = false;

        } catch (ExcecaoDeJSP e) {
            // Sessão inválida ou etapa pulada
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher(PAGINA_SOLICITAR).forward(req, resp);
            return;

        } catch (Throwable e) {
            System.err.println("Erro inesperado em RecuperarSenhaServlet.doGet:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);
        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action").trim();
        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "solicitar" -> solicitarCodigo(req);
                case "validar"   -> validarCodigo(req);
                case "redefinir" -> redefinirSenha(req);
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = req.getServletPath() + "?action=" + proximaEtapa(action);

        } catch (ExcecaoDeJSP e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
            return;

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail:");
            e.printStackTrace(System.err);
            req.setAttribute("erro", "Não foi possível enviar o e-mail. Tente novamente.");
            doGet(req, resp);
            return;

        } catch (ClassNotFoundException e) {
            System.err.println("Falha ao carregar o driver do banco:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        resp.sendRedirect(req.getContextPath() + destino);
    }

    private void solicitarCodigo(HttpServletRequest req)
            throws SQLException, ExcecaoDeJSP, MessagingException {

        String email = req.getParameter("email").trim().toLowerCase();

        try (OtpDAO dao = new OtpDAO()) {
            // Limpar os tokens expirados
            dao.limparExpirados();

            String tipoUsuario = dao.verificarTipoUsuarioPorEmail(email);

            if (tipoUsuario == null) {
                throw ExcecaoDeJSP.emailNaoCadastrado();
            }

            String codigo = dao.gerar(email);

            EmailUtils.enviarCodigo(email, codigo);

            HttpSession sessao = req.getSession(true);
            sessao.setAttribute("otpEmail", email);
            sessao.setAttribute("otpTipoUsuario", tipoUsuario);
            sessao.removeAttribute("otpCodigoVerificado");
        }
    }

    private void validarCodigo(HttpServletRequest req)
            throws SQLException, ExcecaoDeJSP {

        verificarSessaoEmail(req);

        String email  = (String) req.getSession(false).getAttribute("otpEmail");
        String codigo = req.getParameter("codigo").trim();

        try (OtpDAO dao = new OtpDAO()) {
            boolean valido = dao.validar(email, codigo);

            if (!valido) {
                throw ExcecaoDeJSP.codigoOtpInvalido();
            }
        }

        // Código válido: autoriza a etapa de redefinição
        req.getSession(false).setAttribute("otpCodigoVerificado", true);
    }

    private void redefinirSenha(HttpServletRequest req)
            throws SQLException, ExcecaoDeJSP, ClassNotFoundException {

        verificarSessaoCodigo(req);

        HttpSession sessao = req.getSession(false);
        String email = (String) sessao.getAttribute("otpEmail");
        String tipoUsuario = (String) sessao.getAttribute("otpTipoUsuario");
        String novaSenha = req.getParameter("novaSenha");
        String confirmaSenha = req.getParameter("confirmaSenha");

        if (novaSenha == null || novaSenha.isBlank()) {
            throw ExcecaoDeJSP.campoNecessarioFaltante("Nova senha");
        }
        if (novaSenha.length() < 8) {
            throw ExcecaoDeJSP.senhaCurta(8);
        }
        if (!novaSenha.equals(confirmaSenha)) {
            throw ExcecaoDeJSP.senhasNaoCoincidem();
        }

        if ("aluno".equals(tipoUsuario)) {
            try (OtpDAO otpDao = new OtpDAO()) {
                UUID idAluno = otpDao.buscarIdAlunoPorEmail(email);

                try (AlunoDAO alunoDao = new AlunoDAO()) {
                    alunoDao.atualizarSenhaAluno(idAluno, novaSenha);
                }
            }
        }
        else {
            try (ProfessorDAO professorDAO = new ProfessorDAO()) {
                professorDAO.recuperarSenhaProfessor(email, novaSenha);
            }
        }

        // Limpa todos os atributos de OTP da sessão
        sessao.removeAttribute("otpEmail");
        sessao.removeAttribute("otpTipoUsuario");
        sessao.removeAttribute("otpCodigoVerificado");
    }

    // Verifica se o usuário tem email verificado
    private void verificarSessaoEmail(HttpServletRequest req) throws ExcecaoDeJSP {
        HttpSession sessao = req.getSession(false);
        if (sessao == null || sessao.getAttribute("otpEmail") == null) {
            throw ExcecaoDeJSP.acessoInvalidoAoFluxoOtp();
        }
    }

    // Verifica se o usuário teve e-mail e código verificados
    private void verificarSessaoCodigo(HttpServletRequest req) throws ExcecaoDeJSP {
        verificarSessaoEmail(req);
        HttpSession sessao = req.getSession(false);
        if (!Boolean.TRUE.equals(sessao.getAttribute("otpCodigoVerificado"))) {
            throw ExcecaoDeJSP.acessoInvalidoAoFluxoOtp();
        }
    }

    // Evita o problema de reenvio de formulário ao dar F5.
    private String proximaEtapa(String actionAtual) {
        return switch (actionAtual) {
            case "solicitar" -> "validar";
            case "validar" -> "redefinir";
            case "redefinir" -> "sucesso";
            default -> "solicitar";
        };
    }
}
