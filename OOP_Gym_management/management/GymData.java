package gym.management;

import gym.people.Client;
import gym.people.Instructor;
import gym.people.Person;
import gym.people.Secretary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GymData {
    private static GymData instance = null;
    private List<Client> clients = new ArrayList<>();
    private List<Client> leftClients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actionHistory = new ArrayList<>();
    private List<Secretary> secretariesHistory = new ArrayList<>();
    private List<Integer> clientsBalance = new ArrayList<>();
    private List<Integer> workersBalance = new ArrayList<>();
    private Set<Person> persons = new HashSet<>();
    private int gymBalance;




    private GymData() {
    }
    public static GymData getInstance() {
        if (instance == null) {
            instance = new GymData();
        }
        return instance;
    }

    public  List<Integer> getClientsBalance(Secretary secretary) {
        verifyAccess(secretary);
        return clientsBalance;
    }

    public List<Integer> getWorkersBalance(Secretary secretary) {
        verifyAccess(secretary);
        return workersBalance;
    }

    public void verifyAccess(Secretary secretary) {
        if (secretary == null || !secretary.isActive()) {
            throw new NullPointerException();
        }
    }
    public List<Client> getClients(Secretary secretary) {
        verifyAccess(secretary);
        return clients;
    }

    public List<Instructor> getInstructors(Secretary secretary) {
        verifyAccess(secretary);
        return instructors;
    }

    public List<Session> getSessions(Secretary secretary) {
        verifyAccess(secretary);
        return sessions;
    }

    public List<String> getActionHistory(Secretary secretary) {
        verifyAccess(secretary);
        return actionHistory;
    }

    @Override
    public String toString() {
        String clientsData = "Clients Data:"+"\n";
        for (Client client : this.clients) {
            clientsData += client + "\n";
        }
        String instructorsData = "Employees Data:"+"\n";
        for (Instructor instructor : this.instructors) {
            instructorsData += instructor + "\n";
        }
        String sessionsData = "Sessions Data:";
        for (Session session : this.sessions) {
            sessionsData += "\n" +session;
        }

        return clientsData+"\n"+instructorsData + secretariesHistory.getLast().toString()+"\n"+"\n"+sessionsData;
    }

    public List<Secretary> getSecretariesHistory() {
        return secretariesHistory;
    }

    public int getGymBalance(Secretary secretary) {
        verifyAccess(secretary);
        return gymBalance;
    }
    public void setGymBalance(Secretary secretary, int newBalance){
        this.gymBalance = newBalance;
    }

    public List<Client> getClientsHistory(Secretary secretary) {
        verifyAccess(secretary);
        return leftClients;
    }

    public Set<Person> getPersons(Secretary secretary) {
        verifyAccess(secretary);
        return persons;
    }
}
