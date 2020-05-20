package de.adorsys.opba.api.security.external.domain.signdata;

import de.adorsys.opba.api.security.external.domain.OperationType;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class BankSearchDataToSign {
    UUID xRequestId;
    Instant instant;
    OperationType operationType;

    String keyword;
}
