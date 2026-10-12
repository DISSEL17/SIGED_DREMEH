package pe.edu.utp.siged.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/** Bloquea el acceso a las paginas internas si no hay un usuario en sesion. */
@WebFilter("*.xhtml")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) req;
        HttpServletResponse rs = (HttpServletResponse) res;
        HttpSession sesion = rq.getSession(false);
        boolean logueado = sesion != null && sesion.getAttribute("usuario") != null;
        String ruta = rq.getRequestURI();
        boolean libre = ruta.endsWith("/login.xhtml") || ruta.contains("/jakarta.faces.resource/");
        if (logueado || libre) {
            chain.doFilter(req, res);
        } else {
            rs.sendRedirect(rq.getContextPath() + "/login.xhtml");
        }
    }
}
