package de.adorsys.opba.api.security.external.mapper;

import de.adorsys.opba.api.security.external.domain.HttpHeaders;
import de.adorsys.opba.api.security.external.domain.OperationType;
import de.adorsys.opba.api.security.external.domain.QueryParams;
import de.adorsys.opba.api.security.external.domain.signdata.AisListAccountsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.AisListTransactionsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankProfileDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankSearchDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.ConfirmConsentDataToSign;
import feign.RequestTemplate;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FeignTemplateToDataToSignMapper {
    private static final String MISSING_HEADER_ERROR_MESSAGE = " header is missing";

    public AisListAccountsDataToSign mapToListAccounts(RequestTemplate requestTemplate, Instant instant) {
        Map<String, Collection<String>> headers = requestTemplate.headers();

        String operationType = headers.get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                headers.get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));
        String bankId = headers.get(HttpHeaders.BANK_ID).stream()
                                .findFirst()
                                .orElse("");
        String fintechUserId = headers.get(HttpHeaders.FINTECH_USER_ID).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_USER_ID + MISSING_HEADER_ERROR_MESSAGE));
        String redirectOkUrl = headers.get(HttpHeaders.FINTECH_REDIRECT_URL_OK).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_OK + MISSING_HEADER_ERROR_MESSAGE));
        String redirectNokUrl = headers.get(HttpHeaders.FINTECH_REDIRECT_URL_NOK).stream()
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_NOK + MISSING_HEADER_ERROR_MESSAGE));

        return new AisListAccountsDataToSign(
                UUID.fromString(xRequestId),
                instant,
                OperationType.valueOf(operationType),
                bankId,
                fintechUserId,
                redirectOkUrl,
                redirectNokUrl
        );
    }

    public AisListTransactionsDataToSign mapToListTransactions(RequestTemplate requestTemplate, Instant instant) {
        Map<String, Collection<String>> headers = requestTemplate.headers();
        Map<String, Collection<String>> queries = requestTemplate.queries();

        String operationType = headers.get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                headers.get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));
        String bankId = headers.get(HttpHeaders.BANK_ID).stream()
                                .findFirst()
                                .orElse("");
        String fintechUserId = headers.get(HttpHeaders.FINTECH_USER_ID).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_USER_ID + MISSING_HEADER_ERROR_MESSAGE));
        String redirectOkUrl = headers.get(HttpHeaders.FINTECH_REDIRECT_URL_OK).stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_OK + MISSING_HEADER_ERROR_MESSAGE));
        String redirectNokUrl = headers.get(HttpHeaders.FINTECH_REDIRECT_URL_NOK).stream()
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException(HttpHeaders.FINTECH_REDIRECT_URL_NOK + MISSING_HEADER_ERROR_MESSAGE));
        String dateFrom = queries.get(QueryParams.DATE_FROM).stream()
                                  .findFirst()
                                  .orElse("");
        String dateTo = queries.get(QueryParams.DATE_TO).stream()
                                .findFirst()
                                .orElse("");
        String entryReferenceFrom = queries.get(QueryParams.ENTRY_REFERENCE_FROM).stream()
                                            .findFirst()
                                            .orElse("");
        String bookingStatus = queries.get(QueryParams.BOOKING_STATUS).stream()
                                       .findFirst()
                                       .orElse("");
        String deltaList = queries.get(QueryParams.DELTA_LIST).stream()
                                   .findFirst()
                                   .orElse("");

        return new AisListTransactionsDataToSign(
                UUID.fromString(xRequestId),
                instant,
                OperationType.valueOf(operationType),
                bankId,
                fintechUserId,
                redirectOkUrl,
                redirectNokUrl,
                dateFrom,
                dateTo,
                entryReferenceFrom,
                bookingStatus,
                deltaList
        );
    }

    public BankProfileDataToSign mapToBankProfile(RequestTemplate requestTemplate, Instant instant) {
        Map<String, Collection<String>> headers = requestTemplate.headers();

        String operationType = headers.get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                headers.get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));

        return new BankProfileDataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
    }

    public BankSearchDataToSign mapToBankSearch(RequestTemplate requestTemplate, Instant instant) {
        Map<String, Collection<String>> headers = requestTemplate.headers();
        Map<String, Collection<String>> queries = requestTemplate.queries();

        String operationType = headers.get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                headers.get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));
        String keyword = queries.get(QueryParams.KEYWORD).stream()
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalStateException(QueryParams.KEYWORD + MISSING_HEADER_ERROR_MESSAGE));

        return new BankSearchDataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType), keyword);
    }

    public ConfirmConsentDataToSign mapToConfirmConsent(RequestTemplate requestTemplate, Instant instant) {
        Map<String, Collection<String>> headers = requestTemplate.headers();

        String operationType = headers.get(HttpHeaders.X_OPERATION_TYPE).stream().findFirst()
                                       .orElseThrow(() -> new IllegalStateException(HttpHeaders.X_OPERATION_TYPE + MISSING_HEADER_ERROR_MESSAGE));
        String xRequestId =
                headers.get(HttpHeaders.X_REQUEST_ID).stream().findFirst().orElseThrow(() -> new IllegalStateException(HttpHeaders.X_REQUEST_ID + MISSING_HEADER_ERROR_MESSAGE));

        return new ConfirmConsentDataToSign(UUID.fromString(xRequestId), instant, OperationType.valueOf(operationType));
    }
}
