package gym.people;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Person {
    private int balance;
    private static long _ID = 1111;
    private long id;
    private Gender gender;
    private String name;
    private final String birthDay;
    private ArrayList<String> notifications = new ArrayList<>();

    public Person(String name, int balance, Gender gender, String birthDay) {
        this.name = name;
        this.gender = gender;
        this.birthDay = birthDay;
        this.balance = balance;
        this.id = _ID++;
    }
    protected Person(Person other) {
        this.name = other.name;
        this.gender = other.gender;
        this.birthDay = other.birthDay;
        this.balance = other.balance;
        this.id = other.id; // Inherit the same ID
    }
    public int getAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(birthDay, formatter); // parse the string into a LocalDate
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }

    public String getBirthDay() {
        return birthDay;
    }
    public Person getPerson(){
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return ("ID: "+this.getId()+" | Name: "+this.getName()+" | Gender: "+this.getGender()+
                " | Birthday: " +this.getBirthDay()+ " | Age: "+this.getAge()+" | Balance: "+this.getBalance());
    }
}
