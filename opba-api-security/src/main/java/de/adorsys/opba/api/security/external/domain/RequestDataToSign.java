package de.adorsys.opba.api.security.external.domain;

import lombok.Value;

import java.util.Collection;
import java.util.Map;

@Value
public class RequestDataToSign {
    private final Map<String, Collection<String>> headers;
    private final Map<String, Collection<String>> queries;
    private final String path;
}
