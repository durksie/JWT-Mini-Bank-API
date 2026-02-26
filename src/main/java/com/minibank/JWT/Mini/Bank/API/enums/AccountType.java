package com.minibank.JWT.Mini.Bank.API.enums;



public enum AccountType {
    EASY("Easy Account", 0.0, 10000.0),
    ASPIRE("Aspire Account", 1000.0, 50000.0),
    PREMIER("Premier Account", 10000.0, 200000.0),
    PRIVATE_CLIENTS("Private Clients Account", 50000.0, 500000.0),
    PRIVATE_WEALTH("Private Wealth Account", 200000.0, Double.MAX_VALUE);

    private final String displayName;
    private final double minimumBalance;
    private final double maximumBalance;

    AccountType(String displayName, double minimumBalance, double maximumBalance) {
        this.displayName = displayName;
        this.minimumBalance = minimumBalance;
        this.maximumBalance = maximumBalance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public double getMaximumBalance() {
        return maximumBalance;
    }

    public static AccountType fromDisplayName(String displayName) {
        for (AccountType type : AccountType.values()) {
            if (type.displayName.equalsIgnoreCase(displayName) ||
                    type.name().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + displayName);
    }
}
