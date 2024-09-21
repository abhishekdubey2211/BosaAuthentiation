package com.bosa.security;

import com.bosa.jwtservice.JWTAuthenticationFilter;
import com.bosa.service.EndUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    EndUserDetailService endUserService;

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    JWTAuthenticationFilter JWTAuthenticationFilter;

    @Autowired
    OAuth2SucessHandler oauth2SucessHandler;

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/bosa/home", "/bosa/version", "/bosa/register/user", "/bosa/authenticate", "/login").permitAll();
                    registry.requestMatchers("/bosa/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/bosa/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();
                })
                //                  .formLogin(httpSecurityFormLoginCongigurer
                //                        -> httpSecurityFormLoginCongigurer
                //                        .successHandler(customAuthenticationSuccessHandler)
                //                        .loginPage("/bosa/login")
                //                        .permitAll())
                .formLogin(formLogin -> formLogin
                .defaultSuccessUrl("/bosa/home", true)
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Login
                        -> oauth2Login
                        //                        .loginPage("/bosa/login")
                        .defaultSuccessUrl("/bosa/home", true)
                        .successHandler(oauth2SucessHandler)
                        .userInfoEndpoint(userInfoEndpoint
                                -> userInfoEndpoint
                                .oidcUserService(oidcUserService())
                                .userService(oAuth2UserService())
                        ))
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public DefaultOAuth2UserService oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

//    @Bean
//    public UserDetailsService inMemoryUserDetailsService() {
//        UserDetails adminUser = User.builder()
//                .username("abdubey42@gmail")
//                .password(passwordEncoder().encode("Abhi@123"))
////                .password("$argon2id$v=19$m=131072,t=5,p=1$8V7yncWwVCPFFIbZucuJhg$I5DQb6iRrBsVQPaHmzBMPA5ySrFk1s8BRslN47aQ7G8")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(adminUser);
//    }
    @Bean
    public UserDetailsService userDetailsService() {
        return endUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(endUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        int saltLength = 16;
        int hashLength = 32;
        int parallelism = 1;
        int memory = 131072;
        int iterations = 5;
        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }

    @Bean
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder().matches(rawPassword, encodedPassword);
    }

//    
//    public static void main(String[] args) {
//        String a = SecurityConfiguration.passwordEncoder().encode("Abhi@123");
//        boolean b = checkPassword("Abhi@123", a);
//        System.out.println(a);
//        System.out.println(b);
//    }
}
