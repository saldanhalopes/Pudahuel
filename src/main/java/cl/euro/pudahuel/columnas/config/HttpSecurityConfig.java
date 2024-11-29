package cl.euro.pudahuel.columnas.config;

import cl.euro.pudahuel.columnas.service.UsuarioService;
import cl.euro.pudahuel.columnas.util.CustomAccessDeniedHandler;
import cl.euro.pudahuel.columnas.util.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


@Configuration
public class HttpSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            @Autowired UsuarioService usuarioService,
            @Autowired PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(usuarioService, passwordEncoder);
    }

    @Bean
    public SecurityFilterChain httpFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf((AbstractHttpConfigurer::disable))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login", "/qrCodeAuth/**", "/alterarSenha/**",
                                "/resources/**", "/css/**", "/img/**", "/js/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .sessionFixation().newSession()
                        .invalidSessionUrl("/login?invalidSession=true")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?loginError=true")
                        .usernameParameter("username").passwordParameter("password")
                        .defaultSuccessUrl("/index")
                        .authenticationDetailsSource(customAuthenticationDetailsSource)
                        .permitAll())
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logoutSuccess=true")
                        .deleteCookies("SESSION"))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true")))
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
}
