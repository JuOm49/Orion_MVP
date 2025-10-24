package com.openclassrooms.mddapi.security.configurations;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.openclassrooms.mddapi.security.interceptors.AuthByIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * The secret key used for JWT token signing and validation.
     * This value is injected from the application.properties file using the "jwt.secret" property.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Configures the security filter chain for the application.
     * Disables CSRF protection, sets session management to stateless,stateless: for no creation of session, just use the token.
     * and configures authorization rules for various endpoints.
     * Also sets up JWT-based authentication for OAuth2 resource server.
     *
     * @param http the HttpSecurity instance to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return  http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/login", "/api/register").permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())).build();
    }

    /**
     * Web configuration class that implements WebMvcConfigurer to customize Spring MVC behavior.
     * Configures CORS mappings for cross-origin requests and registers the authentication interceptor
     * to handle JWT validation and user ID extraction for protected endpoints.
     */
    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Autowired
        private AuthByIdInterceptor authByIdInterceptor;

        /**
         * Configures CORS (Cross-Origin Resource Sharing) mappings for the API endpoints.
         * Allows requests from the Angular frontend running on localhost:4200 with
         * credentials and all standard HTTP methods.
         *
         * @param registry the CorsRegistry to configure CORS mappings
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }

        /**
         * Registers the authentication interceptor to handle JWT validation and user ID extraction.
         * The interceptor is applied to all API endpoints except login and register endpoints
         * which are publicly accessible.
         *
         * @param registry the InterceptorRegistry to register interceptors
         */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authByIdInterceptor)
                    .excludePathPatterns("/api/login", "/api/register")
                    .addPathPatterns("/api/**");
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtSecret.getBytes()));
    }

    /**
     * Creates and configures a JwtDecoder bean for JWT token validation.
     * Uses HMAC SHA-256 algorithm with the application's secret key to decode and validate
     * JWT tokens received from clients. This decoder is used by Spring Security's
     * OAuth2 resource server configuration for authentication.
     *
     * @return a configured NimbusJwtDecoder instance using HS256 algorithm
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtSecret.getBytes(), 0, this.jwtSecret.getBytes().length, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
