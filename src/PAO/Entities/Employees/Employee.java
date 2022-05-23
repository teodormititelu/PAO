package PAO.Entities.Employees;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {

    protected String firstName;
    protected String lastName;
    protected LocalDate birthDate;

    private static int Id = 0;
    private final int employeeId;

    public Employee(String firstName, String lastName, LocalDate birthDate ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        Id += 1;
        this.employeeId = Id;
    }



    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public static int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", employeeId=" + employeeId +
                '}';
    }

    public String toCSV(){
        return this.firstName + "," + this.lastName + "," + birthDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getEmployeeId() == employee.getEmployeeId() && Objects.equals(getFirstName(), employee.getFirstName()) && Objects.equals(getLastName(), employee.getLastName()) && Objects.equals(getBirthDate(), employee.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getBirthDate(), getEmployeeId());
    }
}
