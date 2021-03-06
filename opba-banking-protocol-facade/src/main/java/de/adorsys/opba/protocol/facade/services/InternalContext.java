package de.adorsys.opba.protocol.facade.services;

import de.adorsys.opba.db.domain.entity.sessions.AuthSession;
import de.adorsys.opba.db.domain.entity.sessions.ServiceSession;
import de.adorsys.opba.protocol.api.dto.context.Context;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Delegate;

@Getter
@Builder(toBuilder = true)
@Value
public class InternalContext<T> {
    @Delegate
    Context<T> ctx;
    AuthSession authSession;
    ServiceSession session;
}
