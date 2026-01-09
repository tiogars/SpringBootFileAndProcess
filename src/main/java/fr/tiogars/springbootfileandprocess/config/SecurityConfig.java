package fr.tiogars.springbootfileandprocess.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Configures HTTP Basic authentication and endpoint access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Username for API access, configured via application.yml.
     */
    @Value("${app.security.username:admin}")
    private String username;

    /**
     * Password for API access, configured via application.yml.
     */
    @Value("${app.security.password:admin}")
    private String password;

    /**
     * Configures the security filter chain with endpoint protection rules.
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http)
            throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Require authentication for /process/** endpoints
                .requestMatchers("/process/**").authenticated()
                // Protect actuator endpoints except health
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/**").authenticated()
                // Public access to Swagger/OpenAPI endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Public access to system and file endpoints
                .requestMatchers("/system/**", "/file/**").permitAll()
                // All other requests are permitted
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            // Disable CSRF for stateless REST API with Basic Auth
            // CSRF protection is not required for APIs that don't use session-based
            // authentication and don't set cookies. This API uses HTTP Basic auth
            // which is stateless and doesn't rely on cookies for authentication.
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Configures the user details service with an in-memory user.
     *
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username(username)
            .password(passwordEncoder().encode(password))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Provides a password encoder bean.
     *
     * @return the BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
