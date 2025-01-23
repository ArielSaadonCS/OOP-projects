package gym.management.Sessions;

import gym.management.ForumType;
import gym.people.Instructor;
import gym.management.Session;

public class Pilates extends Session {
    private final int sessionPrice = 60;
    private final int capacity = 30;
    public Pilates(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) {
        super(sessionType, dateAndTime, forumType, instructor);
    }

    public int getSessionPrice() {
        return sessionPrice;
    }

    public int getCapacity() {
        return capacity;
    }
}
