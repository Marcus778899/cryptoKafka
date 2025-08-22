package com.example.schema;

public enum CryptoCategory {
    BTC("Bitcoin"),
    ETH("Ethereum"),
    SOL("Solana");

    private final String fullName;

    CryptoCategory(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
