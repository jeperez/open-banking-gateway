package de.adorsys.opba.protocol.hbci.service.consent.authenticate;

import de.adorsys.opba.protocol.bpmnshared.service.exec.ValidatedExecution;
import de.adorsys.opba.protocol.hbci.context.HbciContext;
import de.adorsys.opba.protocol.hbci.service.validation.HbciValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Slf4j
@Service("hbciAuthenticateConsentWithScaChallenge")
@RequiredArgsConstructor
public class HbciAuthenticateConsentWithScaChallenge extends ValidatedExecution<HbciContext> {

    private final HbciValidator validator;
    private final Extractor extractor;

    @Override
    protected void doValidate(DelegateExecution execution, HbciContext context) {
        validator.validate(execution, extractor.forValidation(context));
    }

    @Override
    protected void doRealExecution(DelegateExecution execution, HbciContext context) {
    }

    @Service
    static class Extractor {

        Extractor() {
        }

        Object forValidation(HbciContext context) {
            return null;
        }
    }
}