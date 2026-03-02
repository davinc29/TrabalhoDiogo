package com.filter;

import com.dto.ProfessorDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Servlet para filtrar o acesso a páginas JSP do portal-professor, uma vez que não estamos utilizando WEB-INF

@WebFilter(filterName = "sistema-filter", urlPatterns = "/jsp/*")
public class SistemaFilter extends HttpFilter {

    private static final String PAGINA_LOGIN = "/index.jsp";

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Não tem usuário, redireciona para página de login
        if (session != null && session.getAttribute("usuario") instanceof ProfessorDTO) {
            chain.doFilter(req, resp);

        } else {
            resp.sendRedirect(req.getContextPath() + PAGINA_LOGIN);
        }
    }
}
