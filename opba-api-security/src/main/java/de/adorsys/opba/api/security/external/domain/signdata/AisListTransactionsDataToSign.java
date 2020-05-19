package de.adorsys.opba.api.security.external.domain.signdata;

import de.adorsys.opba.api.security.external.domain.OperationType;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AisListTransactionsDataToSign {
    private final UUID xRequestId;
    private final Instant instant;
    private final OperationType operationType;

    private final String bankId;
    private final String fintechUserId;
    private final String redirectOk;
    private final String redirectNok;

    private final String dateFrom;
    private final String dateTo;
    private final String entryReferenceFrom;
    private final String bookingStatus;
    private final String deltaList;

}
