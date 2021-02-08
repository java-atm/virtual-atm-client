package com.atm.customer_console;

import com.utils.enums.Banknote;
import com.utils.exceptions.atm_exceptions.IncorrectPinException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CustomerConsoleTest {

    @Test
    void askPINWhenPasswordIsCorrectInSecondTime() {
        String[] correctPasswordIsSecond = new String[]{"qauee1111", "111111"};
        StringBuilder s = new StringBuilder();
        for (String i: correctPasswordIsSecond) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputPasswordsSecondCorrect = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputPasswordsSecondCorrect);
        CustomerConsole.console = new Scanner(System.in);
        assertDoesNotThrow(CustomerConsole::askPIN);
    }


    @Test
    void askPINWhenPasswordWrongThreeTimes() {
        String[] wrongPasswords = new String[]{"pD", "~`@#$^^^^", "123.45"};
        StringBuilder s = new StringBuilder();
        for (String i: wrongPasswords) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputWrongPasswords = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputWrongPasswords);
        CustomerConsole.console = new Scanner(System.in);
        assertThrows(IncorrectPinException.class, CustomerConsole::askPIN);
    }

    @Test
    void askPINWhenPasswordIsCorrectAtOnce() {
        String[] correctPasswords = new String[]{"1111", "12345", "0000"};
        StringBuilder s = new StringBuilder();
        for (String i: correctPasswords) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputPasswords = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputPasswords);
        CustomerConsole.console = new Scanner(System.in);
        for (String pass : correctPasswords) {
            assertDoesNotThrow(CustomerConsole::askPIN);
        }
    }

    @Test
    void acceptBanknoteIfAllValid() {
        StringBuilder s = new StringBuilder();
        for (Banknote i: Banknote.values()) {
            s.append(i.getBanknote());
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputBanknotes = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
        for (Banknote banknote : Banknote.values()) {
            assertEquals(banknote.getBanknote() ,CustomerConsole.acceptBanknote());
        }
    }

    @Test
    void acceptBanknoteIfSeveralAreValid() {
        double[] severalValidBanknotes = new double[]{5000, 100000};
        double[] banknotes = new double[]{0, -100, 0.898765, 'k', severalValidBanknotes[0], 10004, 50200, 101000, 20700, 500030, severalValidBanknotes[1]};
        StringBuilder s = new StringBuilder();
        for (double i: banknotes) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputBanknotes = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidBanknotes[0], CustomerConsole.acceptBanknote());
        assertEquals(severalValidBanknotes[1], CustomerConsole.acceptBanknote());
    }

    @Test
    void askAmountForWithdrawWhenAmountIsValid() {
        double[] rightAmounts = new double[]{210, 100, 93840, 1230};
        StringBuilder s = new StringBuilder();
        for (double i: rightAmounts) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputRightAmount = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputRightAmount);
        CustomerConsole.console = new Scanner(System.in);
        for (double rightAmount : rightAmounts) {
            assertEquals(rightAmount, CustomerConsole.askAmountForWithdraw());
        }
    }

    @Test
    void askAmountForWithdrawWhileCustomerDoesNotInputRightAmount() {
        double[] severalValidAmounts = new double[]{123600, 100000};
        double[] amounts = new double[]{0, -100, 0.898765, 'k', severalValidAmounts[0], 1001, '\'', 1021, severalValidAmounts[1]};
        StringBuilder s = new StringBuilder();
        for (double i: amounts) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputAmount = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputAmount);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidAmounts[0], CustomerConsole.askAmountForWithdraw());
        assertEquals(severalValidAmounts[1], CustomerConsole.askAmountForWithdraw());
    }

    @Test
    void askAmountForTransferWhenAmountsAreInvalid() {
        BigDecimal[] severalValidAmounts = new BigDecimal[]{new BigDecimal("1000.003"), new BigDecimal("0.234")};
        Object[] amounts = new Object[]{0, -100, "0.898.765", 'k', severalValidAmounts[0], "01.01.01", '\'', "-200", severalValidAmounts[1]};
        StringBuilder s = new StringBuilder();
        for (Object i: amounts) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputAmount = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputAmount);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidAmounts[0], CustomerConsole.askAmountForTransfer());
        assertEquals(severalValidAmounts[1], CustomerConsole.askAmountForTransfer());
    }

    @Test
    void askAmountForTransferIfOnlyValidAmounts() {
        BigDecimal[] amounts = new BigDecimal[]{
                new BigDecimal("1"), new BigDecimal("100"),
                new BigDecimal("0.898765"), new BigDecimal("1010"),
                new BigDecimal("1000.321"), new BigDecimal("0011"),
                new BigDecimal("0.001"), new BigDecimal("0.200")};
        StringBuilder s = new StringBuilder();
        for (BigDecimal i: amounts) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputAmount = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputAmount);
        CustomerConsole.console = new Scanner(System.in);
        for (BigDecimal amount : amounts) {
            assertEquals(amount, CustomerConsole.askAmountForTransfer());
        }
    }

    @Test
    void askAccountNumberIfOnlyValidAccountNumber() {
        Object[] validAccountNumbers = new Object[]{"0000000000000000", "0101010101010101"};
        StringBuilder s = new StringBuilder();
        for (Object i: validAccountNumbers) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputAccountNumber = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputAccountNumber);
        CustomerConsole.console = new Scanner(System.in);
        for (Object accountNumber : validAccountNumbers) {
            assertEquals(accountNumber, CustomerConsole.askAccountNumber());
        }
    }

    @Test
    void askAccountNumberWhileNotBeEnteredValidOne() {
        Object[] severalValidAccountNumbers = new Object[]{"0000000000000000", "0101010101010101"};
        Object[] accountNumbersForTest = new Object[]{111111111111111L, 100, "0.898.765", 'k', severalValidAccountNumbers[0], "01.01.01", "'pp'", "-200", severalValidAccountNumbers[1]};
        StringBuilder s = new StringBuilder();
        for (Object i: accountNumbersForTest) {
            s.append(i);
            s.append(System.lineSeparator());
        }
        ByteArrayInputStream inputAccountNumbers = new ByteArrayInputStream(s.toString().getBytes());
        System.setIn(inputAccountNumbers);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidAccountNumbers[0], CustomerConsole.askAccountNumber());
        assertEquals(severalValidAccountNumbers[1], CustomerConsole.askAccountNumber());
    }
}