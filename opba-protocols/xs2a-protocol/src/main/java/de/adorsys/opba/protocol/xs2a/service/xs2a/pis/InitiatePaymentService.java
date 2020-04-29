package de.adorsys.opba.protocol.xs2a.service.xs2a.pis;

import de.adorsys.opba.protocol.xs2a.domain.dto.messages.ProcessResponse;
import de.adorsys.opba.protocol.xs2a.service.ValidatedExecution;
import de.adorsys.opba.protocol.xs2a.service.dto.ValidatedPathHeadersBody;
import de.adorsys.opba.protocol.xs2a.service.mapper.PathHeadersBodyMapperTemplate;
import de.adorsys.opba.protocol.xs2a.service.xs2a.context.pis.Xs2aPisContext;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.DtoMapper;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.Xs2aPaymentParameters;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.payment.PaymentInitiateBody;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.payment.PaymentInitiateHeaders;
import de.adorsys.opba.protocol.xs2a.service.xs2a.validation.Xs2aValidator;
import de.adorsys.xs2a.adapter.service.PaymentInitiationService;
import de.adorsys.xs2a.adapter.service.RequestParams;
import de.adorsys.xs2a.adapter.service.Response;
import de.adorsys.xs2a.adapter.service.model.PaymentInitiationRequestResponse;
import de.adorsys.xs2a.adapter.service.model.SinglePaymentInitiationBody;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service("xs2aInitiatePaymentService")
@RequiredArgsConstructor
public class InitiatePaymentService extends ValidatedExecution<Xs2aPisContext> {
    private final ApplicationEventPublisher eventPublisher;
    private final Xs2aValidator validator;
    private final PaymentInitiationService pis;
    private final Extractor extractor;

    @Override
    protected void doValidate(DelegateExecution execution, Xs2aPisContext context) {
        validator.validate(execution, extractor.forValidation(context));
    }

    @Override
    protected void doRealExecution(DelegateExecution execution, Xs2aPisContext context) {
        ValidatedPathHeadersBody<Xs2aPaymentParameters, PaymentInitiateHeaders, SinglePaymentInitiationBody> params = extractor.forExecution(context);
        Response<PaymentInitiationRequestResponse> initiationPaymentResponse = pis.initiateSinglePayment(params.getPath().getPaymentType().getValue(),
                                                                                                         params.getHeaders().toHeaders(),
                                                                                                         RequestParams.empty(),
                                                                                                         params.getBody());

        eventPublisher.publishEvent(
                new ProcessResponse(execution.getRootProcessInstanceId(), execution.getId(), initiationPaymentResponse.getBody())
        );
    }

    @Service
    public static class Extractor extends PathHeadersBodyMapperTemplate<Xs2aPisContext,
                                                                               Xs2aPaymentParameters,
                                                                               PaymentInitiateHeaders,
                                                                               PaymentInitiateBody,
                                                                               SinglePaymentInitiationBody> {

        public Extractor(
                DtoMapper<Xs2aPisContext, PaymentInitiateBody> toValidatableBody,
                DtoMapper<PaymentInitiateBody, SinglePaymentInitiationBody> toBody,
                DtoMapper<Xs2aPisContext, PaymentInitiateHeaders> toHeaders,
                DtoMapper<Xs2aPisContext, Xs2aPaymentParameters> toParameters) {

            super(toValidatableBody, toBody, toHeaders, toParameters);
        }
    }
}
