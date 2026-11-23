package com.bijlipay.config;

import com.bijlipay.security.JwtAuthenticationEntryPoint;
import com.bijlipay.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

 /*   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf()
                .disable().authorizeRequests().antMatchers("/**").permitAll();

        return http.build();
    }*/

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.addAllowedOriginPattern("*");
////        configuration.addAllowedOrigin("*");
////        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        configuration.addAllowedHeader("Accept");
//        configuration.addAllowedHeader("Authorization");
//        configuration.addAllowedHeader("Content-Type");
//        configuration.addAllowedMethod("GET");
//        configuration.addAllowedMethod("POST");
//        configuration.addAllowedMethod("PUT");
//        configuration.addAllowedMethod("OPTIONS");
//        configuration.addAllowedMethod("DELETE");
////        configuration.addExposedHeader("Content-Disposition");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    @Autowired
    private SecurityFilter filter;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .headers()
                .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self';")
                .and()
                .httpStrictTransportSecurity() // <-- Add HSTS
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000) // 1 year
                .and()
                .and()
                .csrf().disable().authorizeRequests().antMatchers()
                .authenticated().and().httpBasic().authenticationEntryPoint(unauthorizedHandler).and().authorizeRequests()
                .antMatchers("/document/upload/**","/document/signzy/eSign-event-callbackDetails",
                        "/document/signzy/eSign-callbackDetails","/document/upload/callBackDetails","/document/upload/uploadDocumentFromOnboardLead",
                        "/document/upload/removeLead/**","/document/file/**","/document/upload/agg-reupload-document/**","/document/signzy/contract-callbackDetails/**",
                        "/document/signzy/contract-signer-callbackDetails/**","/document/upload/agreement-manual-upload/**","/document/upload/agg-lead-resubmit/**","/actuator/health",
                        "/document/signzy/contract360-callbackDetails/**","/document/signzy/contract360-signer-callbackDetails/**","/document/signzy/**",
                        "/document/upload/vpaShareUrl/**","/document/signzy/vpaResubmitApi/**","/swagger-ui/**","/swagger-ui.html/**","/v3/api-docs/**","/document/signzy/pan-extraction-signzy/**",
                        "/document/signzy/adhaar-extraction-signzy/**","document/upload/getDetails/**","/get-agreement-details-onboard/**","/actuator","/actuator/**","/document/upload/get-digital-implementation-form/**").permitAll()
                .anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable(); // TO fix document preview issue.
    }

}
