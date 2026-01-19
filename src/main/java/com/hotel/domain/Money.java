package com.hotel.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Represents a monetary value with currency.
 * Ensures amounts are always non-negative.
 */
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    /**
     * Creates Money with BigDecimal amount and Currency object.
     */
    public Money(BigDecimal amount, Currency currency) {
        validateAmount(amount);
        validateCurrency(currency);

        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Convenience constructor for creating Money from double and currency code.
     * Example: new Money(100.50, "USD")
     */
    public Money(double amountValue, String currencyCode) {
        this(BigDecimal.valueOf(amountValue), Currency.getInstance(currencyCode));
    }

    /**
     * Validates that amount is not null and is non-negative.
     */
    private void validateAmount(BigDecimal amountToCheck) {
        if (amountToCheck == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (isNegative(amountToCheck)) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
    }

    /**
     * Checks if the given amount is negative.
     */
    private boolean isNegative(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Validates that currency is not null.
     */
    private void validateCurrency(Currency curr) {
        if (curr == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
    }

    /**
     * Checks if this money amount is positive (greater than zero).
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if this money amount is zero.
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Money otherMoney = (Money) other;
        return Objects.equals(amount, otherMoney.amount) &&
                Objects.equals(currency, otherMoney.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getSymbol();
    }
}
