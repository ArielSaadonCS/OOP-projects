package gym.people;

import java.util.ArrayList;
import java.util.List;

public class Client implements Member {
    private Person person;

    private List<String> notifications = new ArrayList<String>();
    public Client(Person p) {
        this.person = new Person(p);
    }

    public List<String> getNotifications(){
        return this.notifications;
    }
    @Override
    public void update(String msg) {
        this.notifications.add(msg);
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return person.getName();
    }

    public int getBalance() {
        return person.getBalance();
    }

    public int getAge() {
        return person.getAge();
    }

    public long getId() {
        return person.getId();
    }

    public Gender getGender() {
        return person.getGender();
    }

    public void setBalance(int i) {
        person.setBalance(i);
    }
    public String toString() {
        return ("ID: "+this.person.getId()+" | Name: "+this.person.getName()+" | Gender: "+this.person.getGender()+
                " | Birthday: " +this.person.getBirthDay()+ " | Age: "+this.person.getAge()+" | Balance: "+this.person.getBalance());
    }
}
