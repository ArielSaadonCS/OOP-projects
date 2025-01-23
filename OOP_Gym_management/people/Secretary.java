package gym.people;

import gym.Exception.ClientNotRegisteredException;
import gym.Exception.DuplicateClientException;
import gym.Exception.InstructorNotQualifiedException;
import gym.Exception.InvalidAgeException;
import gym.management.ForumType;
import gym.management.GymData;
import gym.management.Publisher;
import gym.management.Session;
import gym.management.Sessions.SessionFactory;
import gym.management.Sessions.SessionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Secretary {
    private int salary;
    private Person person;
    private GymData gymData;
    private boolean isActive;
    private Publisher publisher = new Publisher();

    public Secretary(Person person) {
        this.person = new Person(person);
        this.isActive = true;
        this.gymData = GymData.getInstance();
        List<Secretary> secretariesHistory = GymData.getInstance().getSecretariesHistory();
        if (!secretariesHistory.isEmpty()) {
            Secretary lastSecretary = secretariesHistory.getLast();
            lastSecretary.deactivate();
        }
        GymData.getInstance().getSecretariesHistory().add(this);
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException {
        if (p.getAge() < 18) {
            throw new InvalidAgeException("Error: Client must be at least 18 years old to register");
        }
        if(scanClients(p)){
            throw new DuplicateClientException("Error: The client is already registered");
        }
        gymData.getPersons(this).add(p);
        Client newClient = new Client(p);
        gymData.getClients(this).add(newClient);
        addActionToHistory("Registered new client: " + p.getName());
        return newClient;
    }
    public void unregisterClient(Client c) throws ClientNotRegisteredException {
        if(!gymData.getClients(this).contains(c)){
            throw new ClientNotRegisteredException("Error: Registration is required before attempting to unregister");
        }
        for (int i = 0; i < gymData.getClientsHistory(this).size(); i++) {
            if(gymData.getClientsHistory(this).get(i).getId() == c.getId()){
                int index = locateLeftClientIndex(c.getPerson());
                gymData.getClientsHistory(this).remove(i);
            }
        }

        gymData.getClients(this).remove(c);
        gymData.getClientsHistory(this).add(c);

        addActionToHistory("Unregistered client: " + c.getPerson().getName());
    }

    public Session addSession(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) throws InstructorNotQualifiedException {
        if(!instructor.getSessionsTypes().contains(sessionType)){
            throw new InstructorNotQualifiedException("Error: Instructor is not qualified to conduct this session type.");
        }
        String formattedDateTime = reformatDateTime(dateAndTime);
        Session newSession = SessionFactory.createSession(sessionType, dateAndTime, forumType, instructor);
        gymData.getSessions(this).add(newSession);
        gymData.getInstructors(this).get(locateInstructorIndex(instructor.getPerson())).increaseNumOfSessions();
        addActionToHistory("Created new session: " + sessionType + " on " + formattedDateTime + " with instructor: " + instructor.getName());
        return newSession;
    }
    public Instructor hireInstructor(Person p, int instructorSalary, ArrayList<SessionType> arrayList) {
        Instructor newInstructor = new Instructor(p, instructorSalary,arrayList);
        gymData.getInstructors(this).add(newInstructor);
        addActionToHistory("Hired new instructor: " + p.getName() + " with salary per hour: " + newInstructor.getSalaryPerHour());
        return newInstructor;    }

    public void paySalaries() {
        gymData.verifyAccess(this);
        for(Instructor instructor : gymData.getInstructors(this)){
          payToInstructors(instructor);
        }
        this.person.setBalance(this.person.getBalance()+getSalary());
        int newGymBalance = gymData.getGymBalance(this) - this.getSalary();
        gymData.setGymBalance(this,newGymBalance);
        addActionToHistory("Salaries have been paid to all employees");
    }

    public void registerClientToLesson(Client client, Session session) throws ClientNotRegisteredException, DuplicateClientException {
        if(!gymData.getClients(this).contains(client)){
            throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
        }
        if(!gymData.getSessions(this).contains(session)){
            throw new RuntimeException("Failed registration: gym.management.Session is not in the list");
        }
        if (session.getParticipants().contains(client)){
            throw new DuplicateClientException("Error: The client is already registered for this lesson");
        }
        boolean time = checkTime(client, session);
        boolean forum = checkForum(client, session);
        boolean capacity = checkCapacity(session);
        boolean balance = checkBalance(client, session);
        if (time && forum && capacity && balance){
            session.addParticipant(client);
            chargeClient(client, session);

            String formattedDateTime = reformatDateTime(session.getDateAndTime());

            addActionToHistory("Registered client: "+client.getName()+ " to session: "+session.getType()+" on "+formattedDateTime+" for price: "+ session.getType().getPrice());
        }
    }
    private void payToInstructors(Instructor instructor) {
        int numberOfHours = gymData.getInstructors(this).get(locateInstructorIndex(instructor.getPerson())).getNumOfSessions();
        int paymentForGym = instructor.getSalaryPerHour()*numberOfHours;
        int paymentForInstructor =instructor.getSalaryPerHour();
        if(instructorIsClient(instructor)){
            int clientIndex = locateClientIndex(instructor.getPerson());
            int instructorIndex = locateInstructorIndex(instructor.getPerson());
            int balanceOfThePerson = gymData.getClients(this).get(clientIndex).getBalance();
            gymData.getClients(this).get(clientIndex).setBalance(balanceOfThePerson+paymentForInstructor*numberOfHours);
            gymData.getInstructors(this).get(instructorIndex).setBalance(balanceOfThePerson+paymentForInstructor*numberOfHours);
            for (Person person: gymData.getPersons(this)){
                if(instructor.getId() == person.getId()){
                    person.setBalance(balanceOfThePerson+paymentForInstructor*numberOfHours);
                }
            }
        }

        int gymBalance = gymData.getGymBalance(this);
        int newGymBalance = gymBalance - paymentForGym;
        gymData.setGymBalance(this, newGymBalance);
    }

    private void chargeClient(Client client, Session session) {
        int currentClientBalance = client.getBalance() - session.getType().getPrice();
        gymData.getClients(this).get(locateClientIndex(client.getPerson())).setBalance(currentClientBalance);
        for (Person person: gymData.getPersons(this)){
            if(client.getId() == person.getId()){
                person.setBalance(currentClientBalance);
            }
        }
        int currentGymBalance = gymData.getGymBalance(this) +session.getType().getPrice();
        gymData.setGymBalance(this, currentGymBalance);
    }

    public void notify(Session session, String msg) {
        publisher.notify(session, msg);
        String formattedDateTime = reformatDateTime(session.getDateAndTime());
        addActionToHistory("A message was sent to everyone registered for session "+ session.getType()+" on "+ formattedDateTime+ " : "+ msg);
    }

    public void notify(String date, String msg) {
        String formattedDateTime = reformatDateTime(date);
        publisher.notify(date, msg, gymData.getSessions(this));
        addActionToHistory("A message was sent to everyone registered for a session on "+ formattedDateTime.substring(0, 10)+ " : "+ msg);

    }

    public void notify(String msg) {
        publisher.notify(msg, gymData.getClients(this));
        addActionToHistory("A message was sent to all gym clients: "+msg);
    }

    public void addActionToHistory(String action) {
        gymData.getActionHistory(this).add(action);
    }

    public boolean isActive() {
        return isActive;
    }

    public void printActions() {
        for (int i = 0; i < gymData.getActionHistory(this).size(); i++) {
            System.out.println(gymData.getActionHistory(this).get(i));
        }
    }
    public void deactivate() {
        this.isActive = false;
    }

    private boolean checkTime(Client client, Session session){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); // תבנית המחרוזת
        LocalDateTime sessionDateTime = LocalDateTime.parse(session.getDateAndTime(), formatter);
        if((LocalDateTime.now().isAfter(sessionDateTime))){
            addActionToHistory("Failed registration: Session is not in the future");
            return false;
        }
        return true;
    }
    private boolean checkForum(Client client, Session session){
        if(session.getForum() != ForumType.All){
            if(session.getForum() == ForumType.Seniors) {
                if (client.getAge() < 65) {
                    addActionToHistory("Failed registration: Client doesn't meet the age requirements for this session (Seniors)");
                    return false;
                }
            }
                else {
                    switch (session.getForum()){
                        case ForumType.Male -> {
                            if (client.getGender() == Gender.Female){
                                addActionToHistory("Failed registration: Client's gender doesn't match the session's gender requirements");
                                return false;
                            }
                        }
                        case ForumType.Female -> {
                            if (client.getGender() == Gender.Male){
                                addActionToHistory("Failed registration: Client's gender doesn't match the session's gender requirements");
                                return false;
                            }
                        }
                    }
                }

        }
        return true;
    }
    private boolean checkBalance(Client client, Session session) {
        if (client.getBalance() < session.getType().getPrice()){
            addActionToHistory("Failed registration: Client doesn't have enough balance");
            return false;
        }
        return true;
    }
    private boolean checkCapacity(Session session) {
        if (session.getParticipants().size()==session.getType().getCapacity()){
            addActionToHistory("Failed registration: No available spots for session");
            return false;
        }
        return true;
    }
    private boolean scanClients(Person person){
        List<Client> clients = gymData.getClients(this);
        for (int i = 0; i < clients.size() ; i++) {
            if(clients.get(i).getId() == person.getId()){
                return true;
            }
        }
        return false;
    }
    private boolean instructorIsClient(Instructor instructor){
        List<Client> clients = gymData.getClients(this);
        for (int i = 0; i < clients.size() ; i++) {
            if(clients.get(i).getId() == instructor.getId()){
               return true;
            }
        }
        return false;
    }
    private String reformatDateTime(String dateAndTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (!dateAndTime.contains(" ")) {
            dateAndTime += " 00:00"; // Default to midnight
        }
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, inputFormatter);
        return dateTime.format(outputFormatter);
    }
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String toString() {
        return "ID: "+this.person.getId()+" | Name: "+this.person.getName()+" | Gender: "+this.person.getGender()+
                " | Birthday: " +this.person.getBirthDay()+ " | Age: "+this.person.getAge()+" | Balance: "+this.person.getBalance() +" | Role: Secretary | Salary per Month: "+ this.getSalary();
    }
    private int locateInstructorIndex(Person person){
        for (int i = 0; i <gymData.getInstructors(this).size() ; i++) {
            if(person.getId() == gymData.getInstructors(this).get(i).getId()){
                return i;
            }
        }
        return 0;
    }
    private int locateClientIndex(Person person){
        for (int i = 0; i <gymData.getClients(this).size() ; i++) {
            if(person.getId() == gymData.getClients(this).get(i).getId()){
                return i;
            }
        }
        return 0;
    }
    private int locateLeftClientIndex(Person person){
        for (int i = 0; i <gymData.getClientsHistory(this).size() ; i++) {
            if(person.getId() == gymData.getClientsHistory(this).get(i).getId()){
                return i;
            }
        }
        return 0;
    }

}