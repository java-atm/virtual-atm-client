package com.atm.customer_console;

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
        ByteArrayInputStream inputPasswordsSecondCorrect = new ByteArrayInputStream((
                        correctPasswordIsSecond[0] + System.lineSeparator() +
                        correctPasswordIsSecond[1] + System.lineSeparator()).getBytes());
        System.setIn(inputPasswordsSecondCorrect);
        CustomerConsole.console = new Scanner(System.in);
        assertDoesNotThrow(CustomerConsole::askPIN);
    }


    @Test
    void askPINWhenPasswordWrongThreeTimes() {
        String[] wrongPasswords = new String[]{"pD", "~`@#$^^^^", "123.45"};
        ByteArrayInputStream inputWrongPasswords = new ByteArrayInputStream((
                        wrongPasswords[0] + System.lineSeparator() +
                        wrongPasswords[1] + System.lineSeparator() +
                        wrongPasswords[2] + System.lineSeparator()).getBytes());
        System.setIn(inputWrongPasswords);
        CustomerConsole.console = new Scanner(System.in);
        assertThrows(IncorrectPinException.class, CustomerConsole::askPIN);
    }

    @Test
    void askPINWhenPasswordIsCorrectAtOnce() {
        String[] correctPasswords = new String[]{"1111", "12345", "0000"};
        ByteArrayInputStream inputPasswords = new ByteArrayInputStream((
                        correctPasswords[0] + System.lineSeparator() +
                        correctPasswords[1] + System.lineSeparator() +
                        correctPasswords[2] + System.lineSeparator()).getBytes());
        System.setIn(inputPasswords);
        CustomerConsole.console = new Scanner(System.in);
        for (String s : correctPasswords) {
            assertDoesNotThrow(CustomerConsole::askPIN);
        }
    }

    @Test
    void acceptBanknoteIfAllValid() {
        double[] banknotes = new double[]{10, 20, 50, 100, 200, 500, 1000, 5000, 10000, 20000, 50000, 100000};
        ByteArrayInputStream inputBanknotes = new ByteArrayInputStream((
                    banknotes[0] + System.lineSeparator() +
                    banknotes[1] + System.lineSeparator() +
                    banknotes[2] + System.lineSeparator() +
                    banknotes[3] + System.lineSeparator() +
                    banknotes[4] + System.lineSeparator() +
                    banknotes[5] + System.lineSeparator() +
                    banknotes[6] + System.lineSeparator() +
                    banknotes[7] + System.lineSeparator() +
                    banknotes[8] + System.lineSeparator() +
                    banknotes[9] + System.lineSeparator() +
                    banknotes[10] + System.lineSeparator() +
                    banknotes[11] + System.lineSeparator()).getBytes());
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
        for (double banknote : banknotes) {
            assertEquals(banknote ,CustomerConsole.acceptBanknote());
        }
    }

    @Test
    void acceptBanknoteIfSeveralAreValid() {
        double[] severalValidBanknotes = new double[]{5000, 100000};
        double[] banknotes = new double[]{0, -100, 0.898765, 'k', severalValidBanknotes[0], 10004, 50200, 101000, 20700, 500030, severalValidBanknotes[1]};
        ByteArrayInputStream inputBanknotes = new ByteArrayInputStream((
                        banknotes[0] + System.lineSeparator() +
                        banknotes[1] + System.lineSeparator() +
                        banknotes[2] + System.lineSeparator() +
                        banknotes[3] + System.lineSeparator() +
                        banknotes[4] + System.lineSeparator() +
                        banknotes[5] + System.lineSeparator() +
                        banknotes[6] + System.lineSeparator() +
                        banknotes[7] + System.lineSeparator() +
                        banknotes[8] + System.lineSeparator() +
                        banknotes[9] + System.lineSeparator() +
                        banknotes[10] + System.lineSeparator()).getBytes());
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidBanknotes[0], CustomerConsole.acceptBanknote());
        assertEquals(severalValidBanknotes[1], CustomerConsole.acceptBanknote());
    }

    @Test
    void askAmountForWithdrawWhenAmountIsValid() {
        double[] rightAmounts = new double[]{210, 100, 93840, 1230};
        ByteArrayInputStream inputRightAmount = new ByteArrayInputStream((
                rightAmounts[0] + System.lineSeparator() +
                        rightAmounts[1] + System.lineSeparator() +
                        rightAmounts[2] + System.lineSeparator() +
                        rightAmounts[3] + System.lineSeparator()).getBytes());
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
        ByteArrayInputStream inputAmount = new ByteArrayInputStream((
                amounts[0] + System.lineSeparator() +
                        amounts[1] + System.lineSeparator() +
                        amounts[2] + System.lineSeparator() +
                        amounts[3] + System.lineSeparator() +
                        amounts[4] + System.lineSeparator() +
                        amounts[5] + System.lineSeparator() +
                        amounts[6] + System.lineSeparator() +
                        amounts[7] + System.lineSeparator() +
                        amounts[8] + System.lineSeparator()).getBytes());
        System.setIn(inputAmount);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidAmounts[0], CustomerConsole.askAmountForWithdraw());
        assertEquals(severalValidAmounts[1], CustomerConsole.askAmountForWithdraw());
    }

    @Test
    void askAmountForTransferWhenAmountsAreInvalid() {
        BigDecimal[] severalValidAmounts = new BigDecimal[]{new BigDecimal("1000.003"), new BigDecimal("0.234")};
        Object[] amounts = new Object[]{0, -100, "0.898.765", 'k', severalValidAmounts[0], "01.01.01", '\'', "-200", severalValidAmounts[1]};
        ByteArrayInputStream inputAmount = new ByteArrayInputStream((
                        amounts[0] + System.lineSeparator() +
                        amounts[1] + System.lineSeparator() +
                        amounts[2] + System.lineSeparator() +
                        amounts[3] + System.lineSeparator() +
                        amounts[4] + System.lineSeparator() +
                        amounts[5] + System.lineSeparator() +
                        amounts[6] + System.lineSeparator() +
                        amounts[7] + System.lineSeparator() +
                        amounts[8] + System.lineSeparator()).getBytes());
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
        ByteArrayInputStream inputAmount = new ByteArrayInputStream((
                        amounts[0] + System.lineSeparator() +
                        amounts[1] + System.lineSeparator() +
                        amounts[2] + System.lineSeparator() +
                        amounts[3] + System.lineSeparator() +
                        amounts[4] + System.lineSeparator() +
                        amounts[5] + System.lineSeparator() +
                        amounts[6] + System.lineSeparator() +
                        amounts[7] + System.lineSeparator()).getBytes());
        System.setIn(inputAmount);
        CustomerConsole.console = new Scanner(System.in);
        for (BigDecimal amount : amounts) {
            assertEquals(amount, CustomerConsole.askAmountForTransfer());
        }
    }

    @Test
    void askAccountNumberIfOnlyValidAccountNumber() {
        Object[] validAccountNumbers = new Object[]{"0000000000000000", "0101010101010101"};
        ByteArrayInputStream inputAccountNumber = new ByteArrayInputStream((
                        validAccountNumbers[0] + System.lineSeparator() +
                        validAccountNumbers[1] + System.lineSeparator()).getBytes());
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
        ByteArrayInputStream inputAccountNumbers = new ByteArrayInputStream((
                        accountNumbersForTest[0] + System.lineSeparator() +
                        accountNumbersForTest[1] + System.lineSeparator() +
                        accountNumbersForTest[2] + System.lineSeparator() +
                        accountNumbersForTest[3] + System.lineSeparator() +
                        accountNumbersForTest[4] + System.lineSeparator() +
                        accountNumbersForTest[5] + System.lineSeparator() +
                        accountNumbersForTest[6] + System.lineSeparator() +
                        accountNumbersForTest[7] + System.lineSeparator() +
                        accountNumbersForTest[8] + System.lineSeparator()).getBytes());
        System.setIn(inputAccountNumbers);
        CustomerConsole.console = new Scanner(System.in);
        assertEquals(severalValidAccountNumbers[0], CustomerConsole.askAccountNumber());
        assertEquals(severalValidAccountNumbers[1], CustomerConsole.askAccountNumber());
    }
}