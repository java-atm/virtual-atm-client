package com.atm.customer_console;

import com.utils.exceptions.atm_exceptions.IncorrectPinException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
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
    void continueOperation() {
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
        double banknotesSum = 0;
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
        for (double banknote : banknotes) {
            assertEquals(banknote ,CustomerConsole.acceptBanknote());
        }
    }

    @Test
    void acceptBanknoteIfSeveralAreInvalid() {
        double[] banknotes = new double[]{101, 201, 503, 1001, 2010, 5001, 10004, 50200, 101000, 20700, 500030, 1001000};
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
        double banknotesSum = 0;
        System.setIn(inputBanknotes);
        CustomerConsole.console = new Scanner(System.in);
//        for (double banknote : banknotes) {
//
//        }
    }

    @Test
    void askAmount() {
    }

    @Test
    void askAmountForTransfer() {
    }

    @Test
    void askAccountNumber() {
    }

    @Test
    void chooseAction() {
    }

    @Test
    void displayAccounts() {
    }

    @Test
    void chooseAccountIndex() {
    }

    @Test
    void displayMessage() {
    }
}