package com.utils.enums;

public enum Banknote {

    BANKNOTE_100K(100000),
    BANKNOTE_50K(50000),
    BANKNOTE_20K(20000),
    BANKNOTE_10K(10000),
    BANKNOTE_5K(5000),
    BANKNOTE_1K(1000),
    BANKNOTE_500(500),
    BANKNOTE_200(200),
    BANKNOTE_100(100),
    BANKNOTE_50(50),
    BANKNOTE_20(20),
    BANKNOTE_10(10);

    private final int banknote;

    Banknote(int banknote) {
        this.banknote = banknote;
    }

    public int getBanknote() {
        return banknote;
    }
}
