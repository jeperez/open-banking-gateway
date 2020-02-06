package de.adorsys.opba.restapi.shared.service;

import de.adorsys.opba.protocol.facade.dto.result.torest.FacadeErrorResult;
import de.adorsys.opba.protocol.facade.dto.result.torest.FacadeRedirectResult;
import de.adorsys.opba.protocol.facade.dto.result.torest.FacadeResult;
import de.adorsys.opba.protocol.facade.dto.result.torest.FacadeSuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static de.adorsys.opba.restapi.shared.HttpHeaders.AUTHORIZATION_SESSION_ID;
import static de.adorsys.opba.restapi.shared.HttpHeaders.PSU_CONSENT_SESSION;
import static de.adorsys.opba.restapi.shared.HttpHeaders.REDIRECT_CODE;
import static de.adorsys.opba.restapi.shared.HttpHeaders.SERVICE_SESSION_ID;
import static de.adorsys.opba.restapi.shared.HttpHeaders.X_REQUEST_ID;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@Service
@RequiredArgsConstructor
public class FacadeResponseMapper {

    public <T, E> ResponseEntity<?> translate(FacadeResult<T> result, ErrorResultMapper<FacadeErrorResult, E> toError) {
        if (result instanceof FacadeRedirectResult) {
            return handleRedirect((FacadeRedirectResult) result);
        }

        if (result instanceof FacadeErrorResult) {
            return handleError((FacadeErrorResult) result, toError);
        }

        if (result instanceof FacadeSuccessResult) {
            return handleSuccess((FacadeSuccessResult<T>) result);
        }

        throw new IllegalArgumentException("Unknown result type: " + result.getClass());
    }

    private ResponseEntity<?> handleRedirect(FacadeRedirectResult result) {
        ResponseEntity.BodyBuilder response = putDefaultHeaders(result, ResponseEntity.status(SEE_OTHER));

        return response
                .header(AUTHORIZATION_SESSION_ID, result.getAuthorizationSessionId())
                .header(REDIRECT_CODE, result.getRedirectCode())
                .header(PSU_CONSENT_SESSION, "BAR")
                .location(result.getRedirectionTo())
                .body("Please use redirect link in Location header");
    }

    private <E> ResponseEntity<E> handleError(FacadeErrorResult result, ErrorResultMapper<FacadeErrorResult, E> toError) {
        ResponseEntity.BodyBuilder response = putDefaultHeaders(result, ResponseEntity.status(INTERNAL_SERVER_ERROR));

        return response.body(toError.map(result));
    }

    private <T> ResponseEntity<T> handleSuccess(FacadeSuccessResult<T> result) {
        ResponseEntity.BodyBuilder response = putDefaultHeaders(result, ResponseEntity.status(OK));
        return response.body(result.getBody());
    }

    private ResponseEntity.BodyBuilder putDefaultHeaders(FacadeResult result, ResponseEntity.BodyBuilder builder) {
        builder
                .header(X_REQUEST_ID, result.getXRequestId().toString())
                .header(SERVICE_SESSION_ID, result.getServiceSessionId());
        return builder;
    }
}