package de.adorsys.opba.api.security.external.domain.signdata;

import de.adorsys.opba.api.security.external.domain.OperationType;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AisListAccountsDataToSign {
    UUID xRequestId;
    Instant instant;
    OperationType operationType;

    String bankId;
    String fintechUserId;
    String redirectOk;
    String redirectNok;
}
