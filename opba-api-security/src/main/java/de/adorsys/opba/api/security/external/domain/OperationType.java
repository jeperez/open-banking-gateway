package de.adorsys.opba.api.security.external.domain;

public enum OperationType {
    AIS {
        @Override
        public boolean isTransactionsPath(String path) {
            return path.contains("/transactions");
        }
    },

    BANK_SEARCH {
        @Override
        public boolean isBankSearchPath(String path) {
            return path.contains("/bank-search");
        }
    },

    CONFIRM_CONSENT;

    public boolean isTransactionsPath(String path) {
        return false;
    }

    public boolean isBankSearchPath(String path) {
        return false;
    }
}
