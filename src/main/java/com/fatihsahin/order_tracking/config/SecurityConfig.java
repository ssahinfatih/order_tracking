package com.fatihsahin.order_tracking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/v1/users").permitAll()//tüm kullanıcılar erişebilir
                                .requestMatchers("/api/v1/users/update/").hasRole("ADMIN")//sadece admin rolüne sahip kullanıcılar erişebilir
                                .requestMatchers("/api/v1/users/delete/").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")//hem user hemde admin rolüne sahip kullanıcılar erişebilir
                                .anyRequest().authenticated()//diğer tüm istekler auth olan kullanıcılar erişebilir


                        //  .requestMatchers("/api/v1/users/**").authenticated()//sadece auth olan kullanıcılar erişebilir
                        //   .requestMatchers("/api/v1/orders/**").hasRole("ADMIN")//sadece admin rolüne sahip kullanıcılar erişebilir
                        //   .requestMatchers("/api/v1/products/**").hasRole("USER")//sadece user rolüne sahip kullanıcılar erişebilir
                        //  .anyRequest().permitAll()//diğer tüm istekler izin verilir
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
