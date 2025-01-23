package gym.management;

import gym.people.Person;
import gym.people.Secretary;

import java.util.List;

public class Gym{
    private static Gym instance;
    private GymData gymData;
    private Secretary currentSecretary;
    private String name;

    private Gym() {

    }
    public static Gym getInstance() {

        if (instance == null) {

            instance = new Gym();

        }
        return instance;
    }
    public Secretary getSecretary(){
        return currentSecretary;
    }
    public void setSecretary(Person person, int salary) {
        if(gymData != null){
            for (int i = 0; i < gymData.getClientsHistory(currentSecretary).size(); i++) {
                if( gymData.getClientsHistory(currentSecretary).get(i).getId() == person.getId()){

                 person =  gymData.getClientsHistory(currentSecretary).get(i).getPerson();
                }
         }
        }

        this.currentSecretary = new Secretary(person);
//        this.currentSecretary.setBalance(balance);
        this.currentSecretary.setSalary(salary);
        currentSecretary.addActionToHistory("A new secretary has started working at the gym: "+person.getName());


    }
    //    public void setSecretary(gym.customers.Person p, int salary) {
//
//        this.currentSecretary = new gym.customers.Secretary(p,salary);
//
//    }
    public String getName() {

        return name;

    }
    public void setName(String name) {

        this.name = name;
    }


    @Override
    public String toString() {
        this.gymData = GymData.getInstance();
        return "Gym Name: " + this.name+"\n"+
                "Gym Secretary: " + this.currentSecretary+"\n"+
                "Gym Balance: "+ gymData.getGymBalance(currentSecretary)+"\n"+"\n"+
                gymData.toString();
    }


    public List<Secretary> getSecretariesHistory() {
        return gymData.getSecretariesHistory();
    }
}
