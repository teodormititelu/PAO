package PAO.Entities.Customers;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import PAO.Entities.Orders.*;
import PAO.Entities.Products.Product;
import PAO.Entities.Address;

public class Customer {

    private String firstName;
    private String lastName;
    private final LocalDate birthDate;
    private String phoneNumber;
    private String password;
    private Address address;

    private static int Id = 0;
    private final int customerId;

    public Customer(String firstName, String lastName, LocalDate birthDate, String phoneNumber, String password, Address address) {
        Id += 1;
        this.customerId = Id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return  "First Name = " + firstName +
                "\nLast Name = " + lastName +
                "\nPhone Number = " + phoneNumber +
                "\nBirth Date = " + birthDate +
                address.toString();
    }

    public String toCSV(){
        // fname, lname, date, phone, password, addressId
        return this.firstName + "," + this.lastName + "," + this.birthDate + "," + this.phoneNumber + "," +
                this.password + "," + this.address.getAddressId();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getCustomerId() == customer.getCustomerId() && Objects.equals(getFirstName(), customer.getFirstName()) && Objects.equals(getLastName(), customer.getLastName()) && Objects.equals(getBirthDate(), customer.getBirthDate()) && Objects.equals(getPhoneNumber(), customer.getPhoneNumber()) && Objects.equals(password, customer.password) && Objects.equals(getAddress(), customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getBirthDate(), getPhoneNumber(), password, getAddress(), getCustomerId());
    }
}
