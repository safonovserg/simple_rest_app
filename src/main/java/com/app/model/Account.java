package com.app.model;

import java.math.BigDecimal;

/**
 * Created by Sergey on 06.09.2017.
 */
public class Account {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private BigDecimal amount;

    public Account(String phoneNumber, String firstName, String lastName, BigDecimal amount) {
        this.setPhoneNumber(phoneNumber);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAmount(amount);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.length() == 0 || !isNumber(phoneNumber)) {
            throw new IllegalArgumentException(this.getPhoneNumber() + " has invalid phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    private boolean isNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]*$");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName == null || firstName.length() == 0){
            throw new IllegalArgumentException(this.getPhoneNumber() + " has invalid first name");
        }

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        if(lastName == null || lastName.length() == 0){
            throw new IllegalArgumentException(this.getPhoneNumber() + " has invalid last name");
        }
        this.lastName = lastName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException(this.getPhoneNumber() + " has invalid currency");
        }
        this.amount = amount;
    }

    public void transfer(Account ac2, BigDecimal amount) {
        if(this.getAmount().subtract(amount).compareTo(BigDecimal.ZERO) == -1){
            throw new IllegalArgumentException(this.getPhoneNumber() + " not enough funds");
        }
        this.setAmount(this.getAmount().subtract(amount));
        ac2.setAmount(ac2.getAmount().add(amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!phoneNumber.equals(account.phoneNumber)) return false;
        if (!firstName.equals(account.firstName)) return false;
        if (!lastName.equals(account.lastName)) return false;
        return amount.equals(account.amount);
    }

    @Override
    public int hashCode() {
        int result = phoneNumber.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
