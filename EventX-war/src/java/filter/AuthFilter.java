package filter;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import managedbean.AuthenticationManagedBean;
import managedbean.UserManagedBean;

public class AuthFilter implements Filter {

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    @Inject
    private UserManagedBean userManagedBean;

    public AuthFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        if (userManagedBean == null || userManagedBean.getpId() == -1) {
//redirect to login page if user is not logged in
//and trying to access "secret/*" paths
            ((HttpServletResponse) response).sendRedirect(request1.getContextPath() + "/home.xhtml");
        } else {
//authenticated - continue
            chain.doFilter(request1, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//do nothing
    }

    @Override
    public void destroy() {
//do nothing
    }
}
