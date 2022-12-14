package me.potato.udemyspringsocket.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;

@RequiredArgsConstructor
@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class RSocketSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RSocketStrategiesCustomizer strategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }

    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        var messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(strategies);
        messageHandler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

    @Bean
    public PayloadSocketAcceptorInterceptor interceptor(RSocketSecurity security) {
        return security
                .simpleAuthentication(Customizer.withDefaults())
                .authorizePayload(authorize -> authorize
                        .setup().hasRole("TRUSTED_CLIENT")
                        .route("*.service.*.table").hasRole("ADMIN")
                        .route("*.service.secured.square").hasRole("USER")
                        .anyRequest().authenticated()
                ).build();
    }
}
