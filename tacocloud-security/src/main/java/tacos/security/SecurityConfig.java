package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SuppressWarnings("deprecation")
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/design", "/orders").hasAuthority("USER")
                .anyExchange().permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        //return new StandardPasswordEncoder("53cr3t");
        return NoOpPasswordEncoder.getInstance();
    }

}
