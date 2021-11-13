package filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import services.AccountService;

/**
 *
 * @author mikep
 */
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");
        System.out.println("Email: " + email + " Password: " + password);
        AccountService as = new AccountService();
        User user = as.login(email, password);

        if (user == null) {
            // not logged in
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("login");

            System.out.println("They are not logged in, send them to login!");
            return; // very important. we want to stop the code call
        }

        if (user.getRole().getRoleId() == 1) {
            //they are an admin user
            System.out.println("its an admin let them in!");
            

        } else if (user.getRole().getRoleId() != 1) {
            // regular user case
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("notes");

            System.out.println("They are a regular user, send them to the notes!!");
            return; // very important. we want to stop the code call
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("login");

        }

        // this will either call the enxt filter in the chain
        // or it will load the requested servlet
        chain.doFilter(request, response);

    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
