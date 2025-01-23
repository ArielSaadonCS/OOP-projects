package gym.management.Sessions;

import gym.management.ForumType;
import gym.people.Instructor;
import gym.management.Session;

public class MachinePilates extends Session {
    public MachinePilates(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) {
        super(sessionType, dateAndTime, forumType, instructor);
    }

    public int getSessionPrice() {
        int sessionPrice = 80;
        return sessionPrice;
    }

    public int getCapacity() {
        int capacity = 10;
        return capacity;
    }
}
