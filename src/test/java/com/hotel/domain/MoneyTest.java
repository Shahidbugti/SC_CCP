package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.util.Currency;

class MoneyTest {

    @Test
    void testMoneyCreation_Success() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.50");
        Currency usd = Currency.getInstance("USD");

        // Act
        Money money = new Money(amount, usd);

        // Assert
        assertEquals(amount, money.getAmount(), "Amount should match the provided value");
        assertEquals(usd, money.getCurrency(), "Currency should match the provided value");
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1", "-100", "-0.01", "-999.99" })
    void testMoneyCreation_NegativeAmount_ThrowsException(String negativeAmount) {
        // Arrange
        BigDecimal amount = new BigDecimal(negativeAmount);
        Currency usd = Currency.getInstance("USD");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Money(amount, usd),
                "Creating Money with negative amount should throw IllegalArgumentException");
    }

    @Test
    void testMoneyCreation_NullAmount_ThrowsException() {
        // Arrange
        BigDecimal amount = null;
        Currency usd = Currency.getInstance("USD");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Money(amount, usd),
                "Creating Money with null amount should throw IllegalArgumentException");
    }

    @Test
    void testMoneyCreation_NullCurrency_ThrowsException() {
        // Arrange
        BigDecimal amount = BigDecimal.TEN;
        Currency currency = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Money(amount, currency),
                "Creating Money with null currency should throw IllegalArgumentException");
    }

    @Test
    void testMoneyEquality_SameValues() {
        // Arrange
        Money money1 = new Money(BigDecimal.TEN, Currency.getInstance("USD"));
        Money money2 = new Money(BigDecimal.TEN, Currency.getInstance("USD"));

        // Act & Assert
        assertEquals(money1, money2, "Money objects with same amount and currency should be equal");
        assertEquals(money1.hashCode(), money2.hashCode(), "Equal Money objects should have same hashCode");
    }

    @Test
    void testMoneyEquality_DifferentAmounts() {
        // Arrange
        Money money1 = new Money(BigDecimal.TEN, Currency.getInstance("USD"));
        Money money2 = new Money(BigDecimal.ONE, Currency.getInstance("USD"));

        // Act & Assert
        assertNotEquals(money1, money2, "Money objects with different amounts should not be equal");
    }

    @Test
    void testMoneyEquality_DifferentCurrencies() {
        // Arrange
        Money money1 = new Money(BigDecimal.TEN, Currency.getInstance("USD"));
        Money money2 = new Money(BigDecimal.TEN, Currency.getInstance("EUR"));

        // Act & Assert
        assertNotEquals(money1, money2, "Money objects with different currencies should not be equal");
    }

    @Test
    void testMoneyToString() {
        // Arrange
        Money money = new Money(new BigDecimal("100.50"), Currency.getInstance("USD"));

        // Act
        String result = money.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("100.50"), "toString should contain the amount");
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "0.00", "0.0" })
    void testMoneyCreation_ZeroAmount_Success(String zeroAmount) {
        // Arrange
        BigDecimal amount = new BigDecimal(zeroAmount);
        Currency usd = Currency.getInstance("USD");

        // Act
        Money money = new Money(amount, usd);

        // Assert
        assertNotNull(money, "Money with zero amount should be created successfully");
        assertEquals(0, money.getAmount().compareTo(BigDecimal.ZERO), "Amount should be zero");
    }
}
