package cl.euro.pudahuel.columnas.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;


public class CustomAuthenticationDetails extends WebAuthenticationDetails {

    private final String user2FaCode;

    public CustomAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.user2FaCode = request.getParameter("user2FaCode");
    }

    public String getUser2FaCode() {
        return user2FaCode;
    }
}
