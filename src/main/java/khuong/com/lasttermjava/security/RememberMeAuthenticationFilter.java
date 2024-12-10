package khuong.com.lasttermjava.security;


import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@WebFilter(urlPatterns = "/**")
public class RememberMeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String REMEMBER_ME_COOKIE_NAME = "rememberMe";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_ME_COOKIE_NAME.equals(cookie.getName())) {
                    String username = cookie.getValue();
                    UserDetails userDetails = loadUserByUsername(username);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    return authentication;
                }
            }
        }
        return super.attemptAuthentication(request, response);
    }

    private UserDetails loadUserByUsername(String username) {
        return new User(username, "", true, true, true, true, null);  // Chưa thực hiện xác thực mật khẩu, cần thêm logic xác thực mật khẩu
    }

//    @Override
//    public void destroy() {
//
//    }
}
