package gym.people;

import gym.management.Sessions.SessionType;

import java.util.ArrayList;

public class Instructor {

    private int salaryPerHour;
    private Person person;
    private int numOfSessions;
    private ArrayList<SessionType> sessionsTypes;

    public Instructor(Person p, int instructorSalary, ArrayList<SessionType> arrayList) {
        this.person = new Person(p);
        this.salaryPerHour = instructorSalary;
        this.sessionsTypes = arrayList;
    }

    public int getNumOfSessions() {
        return numOfSessions;
    }

    public void increaseNumOfSessions() {
        this.numOfSessions++;
    }


    public int getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(int salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }

    public ArrayList<SessionType> getSessionsTypes() {
        return sessionsTypes;
    }

    @Override
    public String toString() {
        String certifiedClasses = "";
        for (SessionType sessionsType : this.sessionsTypes) {
            if (this.sessionsTypes.get(this.sessionsTypes.size() - 1).equals(sessionsType)) {
                certifiedClasses += sessionsType;
            } else {
                certifiedClasses += sessionsType + ", ";
            }

        }
        return "ID: " + this.person.getId() + " | Name: " + this.person.getName() + " | Gender: " + this.person.getGender() +
                " | Birthday: " + this.person.getBirthDay() + " | Age: " + this.person.getAge() + " | Balance: " + this.person.getBalance() +
                " | Role: Instructor | Salary per Hour: " + this.salaryPerHour + " | Certified Classes: " + certifiedClasses;

    }

    public String getName() {
        return this.person.getName();
    }

    public int getBalance() {
        return person.getBalance();
    }

    public void setBalance(int i) {
        person.setBalance(i);
    }

    public long getId() {
        return person.getId();
    }

    public Person getPerson() {
        return this.person;
    }

}
