package com.fatihsahin.order_tracking.config;

import com.fatihsahin.order_tracking.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                /*           .csrf(csrf -> csrf
                             .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//csrf tokenini cookie ile saklamak için CookieCsrfTokenRepository kullanıyoruz
                             .ignoringRequestMatchers("/api/v1/users","/api/v1/users/create")//csrf tokenini ignore etmek için ignoringRequestMatchers kullanıyoruz
                     )*/
                .csrf(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)


                /*.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)*///CsrfHeaderFilter sınıfını CsrfFilter sınıfından sonra çalıştırmak için addFilterAfter kullanıyoruz.Header filter ile csrf tokenini header'a ekliyoruz.


                .authorizeHttpRequests(authorize -> authorize

                                .requestMatchers(
                                        "/api/v1/users/getbyid/",
                                        "/api/v1/users/create",
                                        "/api/v1/users/update/",
                                        "/register",
                                        "/login"
                                ).permitAll()//tüm kullanıcılar erişebilir

                                /*  .requestMatchers("/api/v1/users/update/").hasRole("ADMIN")//sadece admin rolüne sahip kullanıcılar erişebilir
                                  .requestMatchers("/api/v1/users/delete/").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")//hem user hemde admin rolüne sahip kullanıcılar erişebilir
                                 */
                                .anyRequest().authenticated()//diğer tüm istekler auth olan kullanıcılar erişebilir

                        //  .requestMatchers("/api/v1/users/**").authenticated()//sadece auth olan kullanıcılar erişebilir
                        //  .requestMatchers("/api/v1/orders/**").hasRole("ADMIN")//sadece admin rolüne sahip kullanıcılar erişebilir
                        //  .requestMatchers("/api/v1/products/**").hasRole("USER")//sadece user rolüne sahip kullanıcılar erişebilir
                        //  .anyRequest().permitAll()//diğer tüm istekler izin verilir
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
