package ua.university.model;

import java.util.Objects;
import ua.university.util.PersonUtils;

public abstract class Person {

    protected String firstName;
    protected String lastName;
    protected String email;

    protected Person(String firstName, String lastName, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }

    public void setFirstName(String firstName) {
        this.firstName = PersonUtils.requireValidName(firstName, "firstName");
    }

    public void setLastName(String lastName) {
        this.lastName = PersonUtils.requireValidName(lastName, "lastName");
    }

    public void setEmail(String email) {
        this.email = PersonUtils.formatEmail(email);
    }

    public String getFullName() {
        return PersonUtils.formatName(firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return firstName.equals(person.firstName)
                && lastName.equals(person.lastName)
                && email.equals(person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + fieldsString() + "}";
    }

    /**
     * Field list for toString(). Subclasses override and append
     * their own fields via super.fieldsString().
     */
    protected String fieldsString() {
        return "fullName='%s', email='%s'".formatted(getFullName(), email);
    }
}