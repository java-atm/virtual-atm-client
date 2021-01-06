package com.utils.enums;

public enum Banknote {

    BANKNOTE_100K(100000.00),
    BANKNOTE_50K(50000.00),
    BANKNOTE_20K(20000.00),
    BANKNOTE_10K(10000.00),
    BANKNOTE_5K(5000.00),
    BANKNOTE_1K(1000.00),
    BANKNOTE_500(500.00),
    BANKNOTE_200(200.00),
    BANKNOTE_100(100.00),
    BANKNOTE_50(50.00),
    BANKNOTE_20(20.00),
    BANKNOTE_10(10.00);

    private final double banknote;
    public static final double MINIMAL_BANKNOTE = BANKNOTE_10.getBanknote();

    Banknote(double banknote) {
        this.banknote = banknote;
    }

    public double getBanknote() {
        return banknote;
    }
}
