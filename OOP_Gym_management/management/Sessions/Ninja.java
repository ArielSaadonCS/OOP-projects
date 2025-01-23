package gym.management.Sessions;

import gym.management.ForumType;
import gym.people.Instructor;
import gym.management.Session;

public class Ninja extends Session {
    public Ninja(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) {
        super(sessionType, dateAndTime, forumType, instructor);
    }

    public int getSessionPrice() {
        int sessionPrice = 150;
        return sessionPrice;
    }

    public int getCapacity() {
        int capacity = 5;
        return capacity;
    }
}
