package Application.MBBS.config;

import Application.MBBS.service.CustomUserDetailsService;
import Application.MBBS.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    private final UserService userLoginService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;

    public SecurityConfig(CustomAuthSuccessHandler customAuthSuccessHandler) {
        this.customAuthSuccessHandler = customAuthSuccessHandler;
    }

    //
//    public SecurityConfig(UserService userLoginService) {
//        this.userLoginService = userLoginService;
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/admin/").hasRole("ADMIN")
//                        .requestMatchers("/po/").hasRole("PO")
//                        .requestMatchers("/pc/").hasRole("PC")
//                        .requestMatchers("/sno/").hasRole("SNO")
//                        .requestMatchers("/rd/").hasRole("RD")
//                        .requestMatchers("/resources/", "/static/", "/assets/","/css/","img/","js/","jquery-3.6.0/").permitAll()
                        .requestMatchers("/login","/register","/home","/api/**").permitAll() //,"/","/about"
                        //						.requestMatchers().permitAll()
                                .requestMatchers("/css/**", "/images/**").permitAll()
                        .anyRequest()
                        //.permitAll()
                        .authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthSuccessHandler)
					    //.defaultSuccessUrl("/login")
                        .failureUrl("/login?error=Authentication Error")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .sessionManagement(
                        (session) -> session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .sessionFixation()
                                .migrateSession()
                                .invalidSessionUrl("/")
                                .maximumSessions(5)
                                .maxSessionsPreventsLogin(true));


        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
        return customUserDetailsService; // Uses already defined service
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
