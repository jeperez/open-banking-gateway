package de.adorsys.opba.api.security.external.service;

import de.adorsys.opba.api.security.external.domain.DataToSign;
import de.adorsys.opba.api.security.external.domain.HttpHeaders;
import de.adorsys.opba.api.security.external.domain.OperationType;
import de.adorsys.opba.api.security.external.domain.QueryParams;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RequestDataExtractingService {

    public DataToSign extractData(HttpServletRequest request, Instant instant) {
        String operationType = request.getHeader(HttpHeaders.X_OPERATION_TYPE);
        DataToSign result;

        switch (operationType) {
            case "AIS":
                result = buildAisDataToSign(request, instant, operationType);
                break;
            case "BANK_SEARCH":
                result = buildBankSearchDataToSign(request, instant, operationType);
                break;
            case "CONFIRM_CONSENT":
                result = buildConfirmConsentDataToSign(request, instant, operationType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation type " + operationType);
        }

        return result;
    }

    private DataToSign buildAisDataToSign(HttpServletRequest request, Instant instant, String operationType) {
        String xRequestId = request.getHeader(HttpHeaders.X_REQUEST_ID);
        String bankId = request.getHeader(HttpHeaders.BANK_ID);
        String fintechUserId = request.getHeader(HttpHeaders.FINTECH_USER_ID);
        String redirectOkUrl = request.getHeader(HttpHeaders.FINTECH_REDIRECT_URL_OK);
        String redirectNokUrl = request.getHeader(HttpHeaders.FINTECH_REDIRECT_URL_OK);

        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(HttpHeaders.BANK_ID, bankId);
        extraValues.put(HttpHeaders.FINTECH_USER_ID, fintechUserId);
        extraValues.put(HttpHeaders.FINTECH_REDIRECT_URL_OK, redirectOkUrl);
        extraValues.put(HttpHeaders.FINTECH_REDIRECT_URL_NOK, redirectNokUrl);

        if (request.getRequestURI().contains("/transactions")) {
            addTransactionsQueries(request, extraValues);
        }

        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildBankSearchDataToSign(HttpServletRequest request, Instant instant, String operationType) {
        String xRequestId = request.getHeader(HttpHeaders.X_REQUEST_ID);
        if (!request.getRequestURI().contains("/bank-search")) {
            return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
        }

        String keyword = request.getParameter(QueryParams.KEYWORD);
        Map<String, String> extraValues = new HashMap<>();
        extraValues.put(QueryParams.KEYWORD, keyword);
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), extraValues);
    }

    private DataToSign buildConfirmConsentDataToSign(HttpServletRequest request, Instant instant, String operationType) {
        String xRequestId = request.getHeader(HttpHeaders.X_REQUEST_ID);
        return new DataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
    }

    private void addTransactionsQueries(HttpServletRequest request, Map<String, String> extraValues) {
        String dateFrom = request.getParameter(QueryParams.DATE_FROM);
        String dateTo = request.getParameter(QueryParams.DATE_TO);
        String entryReferenceFrom = request.getParameter(QueryParams.ENTRY_REFERENCE_FROM);
        String bookingStatus = request.getParameter(QueryParams.BOOKING_STATUS);
        String deltaList = request.getParameter(QueryParams.DELTA_LIST);

        extraValues.put(QueryParams.DATE_FROM, dateFrom);
        extraValues.put(QueryParams.DATE_TO, dateTo);
        extraValues.put(QueryParams.ENTRY_REFERENCE_FROM, entryReferenceFrom);
        extraValues.put(QueryParams.BOOKING_STATUS, bookingStatus);
        extraValues.put(QueryParams.DELTA_LIST, deltaList);
    }
}
