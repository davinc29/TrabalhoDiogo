package com.filter;

import com.dto.AdminDTO;
import com.dto.AlunoViewDTO;
import com.dto.ProfessorDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

// Servlet para filtrar o acesso a páginas JSP do portal-professor, uma vez que não estamos utilizando WEB-INF

@WebFilter(filterName = "sistema-filter", urlPatterns = "/jsp/*")
public class SistemaFilter extends HttpFilter {

    private static final String PAGINA_LOGIN = "/index.jsp";

    private static final Set<String> URLS_PUBLICAS = Set.of(
            "/jsp/recuperar-senha",
            "/jsp/recuperar-senha/",
            "/jsp/recuperar-senha/solicitar.jsp",
            "/jsp/recuperar-senha/validar-codigo.jsp",
            "/jsp/recuperar-senha/redefinir-senha.jsp",
            "/jsp/recuperar-senha/sucesso.jsp",
            "/index.jsp",
            "/cadastro.jsp"
    );

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // Obtém o caminho da requisição sem o contextPath
        String caminho = req.getRequestURI().substring(req.getContextPath().length());

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        // Se a URL for pública, deixa passar sem verificar sessão
        if (URLS_PUBLICAS.contains(caminho)) {
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);

        // Não tem usuário, redireciona para página de login
        if (session != null && (session.getAttribute("usuario") instanceof ProfessorDTO || session.getAttribute("usuario") instanceof AlunoViewDTO || session.getAttribute("usuario") instanceof AdminDTO)) {
            chain.doFilter(req, resp);

        } else {
            resp.sendRedirect(req.getContextPath() + PAGINA_LOGIN);
        }
    }
}
