package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean //Este intersepta la solicitudes http
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
    		.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
            	//Configurar end point publicos
                auth.requestMatchers(HttpMethod.GET, "/dir").permitAll();
                
              //Configurar end point privados
                auth.requestMatchers(HttpMethod.GET, "/names").hasAuthority("READ");
              //Configurar los servicios restantes como seguros
                auth.anyRequest().denyAll();//permitAll()
            })
            .formLogin(login -> login
                            .successHandler(successHandler())
                            .permitAll()
            )
            .sessionManagement(management -> management
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .build();
	}
	
	// Este es quien administra la authenticacion
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	} 
	
	@Bean //Este sera mi proveedor de authenticacion 
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailService());
		return provider;
	} 
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	// Esto se conecta a DB
	@Bean
	public UserDetailsService userDetailService() {
		
		UserDetails userDetails = 
				User
				.withUsername("pablo")
				.password("123")
				.roles("ADMIN")
				.authorities("READ", "CREATE")
				.build();
		
		return new InMemoryUserDetailsManager(userDetails);
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
