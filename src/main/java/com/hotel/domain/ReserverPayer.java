package com.hotel.domain;

public class ReserverPayer {
    private final Identity id;
    private final CreditCard creditCardDetails;

    public ReserverPayer(Identity id, CreditCard creditCardDetails) {
        if (id == null) {
			throw new IllegalArgumentException("Identity cannot be null");
		}
        if (creditCardDetails == null) {
			throw new IllegalArgumentException("Credit card details cannot be null");
		}

        this.id = id;
        this.creditCardDetails = creditCardDetails;
    }

    public Identity getId() {
        return id;
    }

    public CreditCard getCreditCardDetails() {
        return creditCardDetails;
    }
}
