package gym.management;

import gym.people.Client;
import gym.people.Instructor;
import gym.management.Sessions.SessionType;

import java.util.ArrayList;
import java.util.List;

public abstract class Session {
    private SessionType type;
    private String dateAndTime;
    private ForumType forum;
    private Instructor instructor;
    private List<Client> participants;


    public Session(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor){
        this.type = sessionType;
        this.dateAndTime = dateAndTime;
        this.forum = forumType;
        this.instructor = instructor;
        participants = new ArrayList<>();
    }
    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public java.lang.String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(java.lang.String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public ForumType getForum() {
        return forum;
    }

    public void setForum(ForumType forum) {
        this.forum = forum;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    public void addParticipant(Client client){
      participants.add(client);
    }
    public List<Client> getParticipants(){
        return this.participants;
    }
    public String toString(){
        return ("Session Type: "+this.getType()+" | "+ "Date: "+this.dateAndTime+" | "+"Forum: "+this.getForum()+" | "+"Instructor: "+ this.getInstructor().getName() +" | "+"Participants: "+ getParticipants().size()+"/"+this.getType().getCapacity());
    }
}
