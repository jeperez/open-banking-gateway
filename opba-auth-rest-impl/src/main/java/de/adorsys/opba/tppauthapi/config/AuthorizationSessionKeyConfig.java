package de.adorsys.opba.tppauthapi.config;

import de.adorsys.opba.api.security.internal.EnableTokenBasedApiSecurity;
import de.adorsys.opba.api.security.internal.service.TokenBasedAuthService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

import static de.adorsys.opba.restapi.shared.HttpHeaders.AUTHORIZATION_SESSION_KEY;

@Configuration
@EnableTokenBasedApiSecurity
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthorizationSessionKeyConfig {

    private final TokenBasedAuthService authService;

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AuthorizationSessionKeyFromHttpRequest getAuthorizationSessionKeyFromHttpRequest(HttpServletRequest httpServletRequest) {
        log.info("REQUEST COMMING IN {}",  httpServletRequest.getRequestURI());
        if (null == httpServletRequest.getCookies()) {
            log.info("rest request without authorization {}", httpServletRequest.getRequestURI());
            return new AuthorizationSessionKeyFromHttpRequest(Optional.empty());
        }

        String authCookieValue = Arrays.stream(httpServletRequest.getCookies())
                .filter(it -> AUTHORIZATION_SESSION_KEY.equals(it.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalStateException("Authorization-Session-Key cookie is missing"));

        return new AuthorizationSessionKeyFromHttpRequest(Optional.of(authService.validateTokenAndGetSubject(authCookieValue)));
    }

    @AllArgsConstructor
    public static class AuthorizationSessionKeyFromHttpRequest {
        private Optional<String> key;
        public String get() {
            return key.get();
        }
        public boolean isPresent() {
            return key.isPresent();
        }
    }
}