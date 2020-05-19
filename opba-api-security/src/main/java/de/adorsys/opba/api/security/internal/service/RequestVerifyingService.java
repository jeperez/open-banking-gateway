package de.adorsys.opba.api.security.internal.service;

import de.adorsys.opba.api.security.external.domain.signdata.AisListAccountsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.AisListTransactionsDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankProfileDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.BankSearchDataToSign;
import de.adorsys.opba.api.security.external.domain.signdata.ConfirmConsentDataToSign;

public interface RequestVerifyingService {

    /**
     * Verifies data for /v1/banking/ais/accounts endpoint
     *
     * @param signature Encoded string to be verified
     * @param encodedPublicKey Public key, used for the verification
     * @param aisListAccountsDataToSign request given data, to verify with given signature
     * @return String signature representation
     */
    boolean verify(String signature, String encodedPublicKey, AisListAccountsDataToSign aisListAccountsDataToSign);

    /**
     * Verifies data for /v1/banking/ais/accounts/{account-id}/transactions endpoint
     *
     * @param signature Encoded string to be verified
     * @param encodedPublicKey Public key, used for the verification
     * @param aisListTransactionsDataToSign request given data, to verify with given signature
     * @return String signature representation
     */
    boolean verify(String signature, String encodedPublicKey, AisListTransactionsDataToSign aisListTransactionsDataToSign);

    /**
     * Verifies data for /v1/banking/search/bank-search endpoint
     *
     * @param signature Encoded string to be verified
     * @param encodedPublicKey Public key, used for the verification
     * @param bankSearchDataToSign v data, to verify with given signature
     * @return String signature representation
     */
    boolean verify(String signature, String encodedPublicKey, BankSearchDataToSign bankSearchDataToSign);

    /**
     * Verifies data for /v1/banking/search/bank-profile endpoint
     *
     * @param signature Encoded string to be verified
     * @param encodedPublicKey Public key, used for the verification
     * @param bankProfileDataToSign request given data, to verify with given signature
     * @return String signature representation
     */
    boolean verify(String signature, String encodedPublicKey, BankProfileDataToSign bankProfileDataToSign);

    /**
     * Verifies data for /v1/banking/consents/{auth-id}/confirm endpoint
     *
     * @param signature Encoded string to be verified
     * @param encodedPublicKey Public key, used for the verification
     * @param confirmConsentDataToSign request given data, to verify with given signature
     * @return String signature representation
     */
    boolean verify(String signature, String encodedPublicKey, ConfirmConsentDataToSign confirmConsentDataToSign);
}
