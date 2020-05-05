package de.adorsys.opba.consentapi.controller;

import de.adorsys.opba.consentapi.model.generated.PsuAuthRequest;
import de.adorsys.opba.consentapi.service.mapper.PisSinglePaymentMapper;
import de.adorsys.opba.protocol.api.dto.context.UserAgentContext;
import de.adorsys.opba.protocol.api.dto.request.FacadeServiceableRequest;
import de.adorsys.opba.protocol.api.dto.request.authorization.SinglePaymentBody;
import de.adorsys.opba.protocol.api.dto.request.payments.InitiateSinglePaymentRequest;
import de.adorsys.opba.protocol.facade.dto.result.torest.FacadeResult;
import de.adorsys.opba.protocol.facade.services.pis.SinglePaymentService;
import de.adorsys.opba.restapi.shared.mapper.FacadeResponseBodyToRestBodyMapper;
import de.adorsys.opba.restapi.shared.service.FacadeResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


// TODO DELETE ME
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final UserAgentContext userAgentContext;
    private final PisSinglePaymentMapper pisSinglePaymentMapper;
    private final FacadeResponseMapper mapper;
    private final SinglePaymentService singlePaymentService;
    private final FacadeServiceableRequest serviceableTemplate;

    @RequestMapping(value = "/v1/payment/{auth-id}/embedded",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public CompletableFuture initiatePaymentPost(
            @RequestHeader(value = "X-Request-ID", required = true) UUID xRequestID,
            @RequestHeader(value = "X-XSRF-TOKEN", required = true) String xXsrfToken,
            @PathVariable("auth-id") String authId,
            @RequestBody PsuAuthRequest body,
            @RequestParam(value = "redirectCode", required = false) String redirectCode) {
        return singlePaymentService.execute(
                InitiateSinglePaymentRequest.builder()
                        .facadeServiceable(serviceableTemplate.toBuilder()
                                                   // Get rid of CGILIB here by copying:
                                                   .uaContext(userAgentContext.toBuilder().build())
                                                   .redirectCode(redirectCode)
                                                   //.authorizationSessionId(authId)
                                                   .requestId(xRequestID)
                                                   .bankId("53c47f54-b9a4-465a-8f77-bc6cd5f0cf46")
                                                   .sessionPassword("123")
                                                   .authorization("SUPER-FINTECH-ID")
                                                   .fintechUserId("user1@fintech.com")
                                                   .fintechRedirectUrlOk("http://google.com")
                                                   .fintechRedirectUrlNok("http://microsoft.com")
                                                   .build()
                        )
                        .singlePayment(null == body.getConsentAuth() ? null : pisSinglePaymentMapper
                                                                                      .map(body.getConsentAuth().getSinglePayment()))
                        /*.scaAuthenticationData(body.getScaAuthenticationData())
                        .extras(extrasMapper.map(body.getExtras()))*/
                        .build()
        ).thenApply((FacadeResult<SinglePaymentBody> result) ->
                            mapper.translate(result, new NoOpMapper<>()));
    }

    public static class NoOpMapper<T> implements FacadeResponseBodyToRestBodyMapper<T, T> {
        public T map(T facadeEntity) {
            return facadeEntity;
        }
    }

}
