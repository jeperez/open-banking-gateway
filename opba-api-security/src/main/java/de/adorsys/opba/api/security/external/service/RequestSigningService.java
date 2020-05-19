package de.adorsys.opba.api.security.external.service;

import de.adorsys.opba.api.security.external.domain.signdata.AisListAccountsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.AisListTransactionsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankProfileDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankSearchDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.ConfirmConsentDataToSign;

public interface RequestSigningService {

    /**
     * Signs data for /v1/banking/ais/accounts endpoint
     *
     * @param aisListAccountsDataToSign Header data, required for signing
     * @return String signature representation
     */
    String signature(AisListAccountsDataToSign aisListAccountsDataToSign);

    /**
     * Signs data for /v1/banking/ais/accounts/{account-id}/transactions endpoint
     *
     * @param aisListTransactionsDataToSign Header and query data, required for signing
     * @return String signature representation
     */
    String signature(AisListTransactionsDataToSign aisListTransactionsDataToSign);

    /**
     * Signs data for /v1/banking/search/bank-search endpoint
     *
     * @param bankSearchDataToSign Header and query data, required for signing
     * @return String signature representation
     */
    String signature(BankSearchDataToSign bankSearchDataToSign);

    /**
     * Signs data for /v1/banking/search/bank-profile endpoint
     *
     * @param bankProfileDataToSign Header data, required for signing
     * @return String signature representation
     */
    String signature(BankProfileDataToSign bankProfileDataToSign);

    /**
     * Signs data for /v1/banking/consents/{auth-id}/confirm endpoint
     *
     * @param confirmConsentDataToSign Header data, required for signing
     * @return String signature representation
     */
    String signature(ConfirmConsentDataToSign confirmConsentDataToSign);
}
