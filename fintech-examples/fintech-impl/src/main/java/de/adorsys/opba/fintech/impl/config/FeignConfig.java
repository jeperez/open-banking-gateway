package de.adorsys.opba.fintech.impl.config;

import de.adorsys.opba.api.security.external.domain.DataToSign;
import de.adorsys.opba.api.security.external.service.RequestSigningService;
import de.adorsys.opba.api.security.external.domain.OperationType;
import de.adorsys.opba.fintech.impl.properties.TppProperties;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.COMPUTE_PSU_IP_ADDRESS;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.FINTECH_ID;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.X_REQUEST_ID;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.X_OPERATION_TYPE;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.X_REQUEST_SIGNATURE;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.X_TIMESTAMP_UTC;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.BANK_ID;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.FINTECH_USER_ID;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.FINTECH_REDIRECT_URL_OK;
import static de.adorsys.opba.fintech.impl.tppclients.HeaderFields.FINTECH_REDIRECT_URL_NOK;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.DATE_FROM;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.DATE_TO;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.ENTRY_REFERENCE_FROM;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.BOOKING_STATUS;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.DELTA_LIST;
import static de.adorsys.opba.fintech.impl.tppclients.QueryFields.KEYWORD;

/**
 * This class enhances requests, so that the include PSU IP address headers. This header should either become
 * mandatory or we need to expose header-based configuration in API.
 * FIXME: https://github.com/adorsys/open-banking-gateway/issues/474
 * After aforementioned issue is fixed, this class should not exist.
 */
@Configuration
@RequiredArgsConstructor
public class FeignConfig {
    private static final String MISSING_HEADER_ERROR_MESSAGE = " header is missing";
    private final RequestSigningService requestSigningService;
    private final TppProperties tppProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        // This allows OPBA Consent API to compute PSU IP address itself.
        return requestTemplate -> {
            requestTemplate.header(COMPUTE_PSU_IP_ADDRESS, "true");
            fillSecurityHeaders(requestTemplate);
        };
    }

    private void fillSecurityHeaders(RequestTemplate requestTemplate) {
        Instant instant = Instant.now();
        requestTemplate.header(X_REQUEST_SIGNATURE, calculateSignature(requestTemplate, instant));
        requestTemplate.header(FINTECH_ID, tppProperties.getFintechID());
        requestTemplate.header(X_TIMESTAMP_UTC, instant.toString());
    }

    private String calculateSignature(RequestTemplate requestTemplate, Instant instant) {
        String operationType = requestTemplate.headers().get(X_OPERATION_TYPE).stream().findFirst().orElseThrow(() -> new IllegalStateException(X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId = requestTemplate.headers().get(X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));
        DataToSign dataToSign;

        switch (operationType) {
            case "AIS": dataToSign = buildAisDataToSign(requestTemplate, instant, operationType, xRequestId);
            break;
            case "BANK_SEARCH": dataToSign = buildBankSearchDataToSign(requestTemplate, instant, operationType, xRequestId);
            break;
            case "CONFIRM_CONSENT": dataToSign = buildConfirmConsentDataToSign(
                    instant, operationType, xRequestId);
            break;
            default: throw new IllegalArgumentException("Unsupported operation type " + operationType);
        }

        return requestSigningService.signature(dataToSign);
    }

    private DataToSign buildAisDataToSign(RequestTemplate requestTemplate, Instant instant, String operationType, String xRequestId) {
        String bankId = requestTemplate.headers().get(BANK_ID).stream()
                                .findFirst()
                                .orElse("");
        String fintechUserId = requestTemplate.headers().get(FINTECH_USER_ID).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(FINTECH_USER_ID + MISSING_HEADER_ERROR_MESSAGE));
        String redirectOkUrl = requestTemplate.headers().get(FINTECH_REDIRECT_URL_OK).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(FINTECH_REDIRECT_URL_OK + MISSING_HEADER_ERROR_MESSAGE));
        String redirectNokUrl = requestTemplate.headers().get(FINTECH_REDIRECT_URL_NOK).stream()
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException(FINTECH_REDIRECT_URL_NOK + MISSING_HEADER_ERROR_MESSAGE));

        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(BANK_ID, bankId);
        extraValues.put(FINTECH_USER_ID, fintechUserId);
        extraValues.put(FINTECH_REDIRECT_URL_OK, redirectOkUrl);
        extraValues.put(FINTECH_REDIRECT_URL_NOK, redirectNokUrl);

        if (requestTemplate.path().contains("/transactions")) {
            addTransactionsQueries(requestTemplate, extraValues);
        }

        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildBankSearchDataToSign(RequestTemplate requestTemplate, Instant instant, String operationType, String xRequestId) {
        if (!requestTemplate.path().contains("/bank-search")) {
            return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
        }

        String keyword = requestTemplate.queries().get(KEYWORD).stream()
                                  .findFirst()
                                 .orElseThrow(() -> new IllegalStateException(KEYWORD + MISSING_HEADER_ERROR_MESSAGE));
        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(KEYWORD, keyword);
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildConfirmConsentDataToSign(Instant instant, String operationType, String xRequestId) {
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
    }

    private void addTransactionsQueries(RequestTemplate requestTemplate, Map<String, String> extraValues) {
        String dateFrom = requestTemplate.queries().get(DATE_FROM).stream()
                                .findFirst()
                                .orElse("");
        String dateTo = requestTemplate.queries().get(DATE_TO).stream()
                                .findFirst()
                                .orElse("");
        String entryReferenceFrom = requestTemplate.queries().get(ENTRY_REFERENCE_FROM).stream()
                                .findFirst()
                                .orElse("");
        String bookingStatus = requestTemplate.queries().get(BOOKING_STATUS).stream()
                                .findFirst()
                                .orElse("");
        String deltaList = requestTemplate.queries().get(DELTA_LIST).stream()
                                .findFirst()
                                .orElse("");

        extraValues.put(DATE_FROM, dateFrom);
        extraValues.put(DATE_TO, dateTo);
        extraValues.put(ENTRY_REFERENCE_FROM, entryReferenceFrom);
        extraValues.put(BOOKING_STATUS, bookingStatus);
        extraValues.put(DELTA_LIST, deltaList);
    }
}
