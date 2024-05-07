package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

	@SuppressWarnings("removal")
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
            .authorizeHttpRequests(auth -> {
            	 auth.requestMatchers("/dir", "/swagger-ui/index.html", "/swagger-ui/**").permitAll();
                auth.anyRequest().authenticated();
            })
            .formLogin(login -> login
                            .successHandler(successHandler())
                            .permitAll()
            )
            .sessionManagement(management -> management
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login")//si se crea una sesion mal se regrese al login
                    .maximumSessions(1)
                    .sessionRegistry(sessionRegistry())
                    .and()
                    .sessionFixation()
                    .migrateSession())
            .build();
	}
	
	public SessionRegistry sessionRegistry(){
		return new SessionRegistryImpl();
	}
	
	public AuthenticationSuccessHandler successHandler(){
		return (  (request, response, authentication) ->  {
			response.sendRedirect("/api/v1/index");
		});
		
	}

}
