package de.adorsys.opba.protocol.xs2a.service.xs2a.dto;

import de.adorsys.opba.protocol.api.dto.payment.PaymentType;
import de.adorsys.opba.protocol.xs2a.service.xs2a.context.pis.Xs2aPisContext;
import lombok.Data;
import org.mapstruct.Mapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static de.adorsys.opba.protocol.xs2a.constant.GlobalConst.SPRING_KEYWORD;
import static de.adorsys.opba.protocol.xs2a.constant.GlobalConst.XS2A_MAPPERS_PACKAGE;

/**
 * XS2A payment describing path parameters.
 */
@Data
public class Xs2aPaymentParameters {

    /**
     * Payment ID that uniquely identifies the payment within ASPSP. Highly sensitive field.
     */
    @NotBlank
    private String paymentId;

    /**
     * Payment service is provided by ASPSP.
     */
    @NotNull
    private PaymentType paymentType;

    /**
     * Payment product is provided by ASPSP.
     */
    @NotBlank
    private String paymentProduct;

    @Mapper(componentModel = SPRING_KEYWORD, implementationPackage = XS2A_MAPPERS_PACKAGE)
    public interface FromCtx extends DtoMapper<Xs2aPisContext, Xs2aPaymentParameters> {
        Xs2aPaymentParameters map(Xs2aPisContext ctx);
    }
}
