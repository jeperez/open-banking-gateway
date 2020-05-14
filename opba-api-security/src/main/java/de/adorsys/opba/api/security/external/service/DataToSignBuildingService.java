package de.adorsys.opba.api.security.external.service;

import de.adorsys.opba.api.security.external.domain.DataToSign;
import de.adorsys.opba.api.security.external.domain.HttpHeaders;
import de.adorsys.opba.api.security.external.domain.OperationType;
import de.adorsys.opba.api.security.external.domain.QueryParams;
import de.adorsys.opba.api.security.external.domain.RequestDataToSign;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DataToSignBuildingService {
    private static final String MISSING_HEADER_ERROR_MESSAGE = " header is missing";

    public DataToSign buildDataToSign(RequestDataToSign requestData, Instant instant) {
        String operationType = requestData.getHeaders().get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                requestData.getHeaders().get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));
        DataToSign result;

        switch (operationType) {
            case "AIS":
                result = buildAisDataToSign(requestData, instant, operationType, xRequestId);
                break;
            case "BANK_SEARCH":
                result = buildBankSearchDataToSign(requestData, instant, operationType, xRequestId);
                break;
            case "CONFIRM_CONSENT":
                result = buildConfirmConsentDataToSign(instant, operationType, xRequestId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation type " + operationType);
        }

        return result;
    }

    private DataToSign buildAisDataToSign(RequestDataToSign requestData, Instant instant, String operationType, String xRequestId) {
        String bankId = requestData.getHeaders().get(HttpHeaders.BANK_ID).stream()
                                .findFirst()
                                .orElse("");
        String fintechUserId = requestData.getHeaders().get(HttpHeaders.FINTECH_USER_ID).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_USER_ID + MISSING_HEADER_ERROR_MESSAGE));
        String redirectOkUrl = requestData.getHeaders().get(HttpHeaders.FINTECH_REDIRECT_URL_OK).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_OK + MISSING_HEADER_ERROR_MESSAGE));
        String redirectNokUrl = requestData.getHeaders().get(HttpHeaders.FINTECH_REDIRECT_URL_NOK).stream()
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_NOK + MISSING_HEADER_ERROR_MESSAGE));

        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(HttpHeaders.BANK_ID, bankId);
        extraValues.put(HttpHeaders.FINTECH_USER_ID, fintechUserId);
        extraValues.put(HttpHeaders.FINTECH_REDIRECT_URL_OK, redirectOkUrl);
        extraValues.put(HttpHeaders.FINTECH_REDIRECT_URL_NOK, redirectNokUrl);

        if (requestData.getPath().contains("/transactions")) {
            addTransactionsQueries(requestData, extraValues);
        }

        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildBankSearchDataToSign(RequestDataToSign requestData, Instant instant, String operationType, String xRequestId) {
        if (!requestData.getPath().contains("/bank-search")) {
            return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
        }

        String keyword = requestData.getQueries().get(QueryParams.KEYWORD).stream()
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalStateException(QueryParams.KEYWORD + MISSING_HEADER_ERROR_MESSAGE));
        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(QueryParams.KEYWORD, keyword);
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildConfirmConsentDataToSign(Instant instant, String operationType, String xRequestId) {
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
    }

    private void addTransactionsQueries(RequestDataToSign requestData, Map<String, String> extraValues) {
        String dateFrom = requestData.getQueries().get(QueryParams.DATE_FROM).stream()
                                  .findFirst()
                                  .orElse("");
        String dateTo = requestData.getQueries().get(QueryParams.DATE_TO).stream()
                                .findFirst()
                                .orElse("");
        String entryReferenceFrom = requestData.getQueries().get(QueryParams.ENTRY_REFERENCE_FROM).stream()
                                            .findFirst()
                                            .orElse("");
        String bookingStatus = requestData.getQueries().get(QueryParams.BOOKING_STATUS).stream()
                                       .findFirst()
                                       .orElse("");
        String deltaList = requestData.getQueries().get(QueryParams.DELTA_LIST).stream()
                                   .findFirst()
                                   .orElse("");

        extraValues.put(QueryParams.DATE_FROM, dateFrom);
        extraValues.put(QueryParams.DATE_TO, dateTo);
        extraValues.put(QueryParams.ENTRY_REFERENCE_FROM, entryReferenceFrom);
        extraValues.put(QueryParams.BOOKING_STATUS, bookingStatus);
        extraValues.put(QueryParams.DELTA_LIST, deltaList);
    }
}
