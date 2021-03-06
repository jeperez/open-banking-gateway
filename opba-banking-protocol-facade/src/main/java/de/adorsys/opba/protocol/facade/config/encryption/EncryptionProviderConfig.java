package de.adorsys.opba.protocol.facade.config.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionProviderConfig {

    @Bean
    ConsentAuthorizationEncryptionServiceProvider consentAuthEncryptionProvider(ConsentSpecSecretKeyConfig specSecretKeyConfig) {
        return new ConsentAuthorizationEncryptionServiceProvider(
                new EncryptionWithInitVectorOper(specSecretKeyConfig)
        );
    }

    @Bean
    PsuConsentEncryptionServiceProvider psuConsentEncryptionProvider(PsuKeyPairConfig psuKeyPairConfig) {
        return new PsuConsentEncryptionServiceProvider(new CmsEncryptionOper(psuKeyPairConfig));
    }

}
