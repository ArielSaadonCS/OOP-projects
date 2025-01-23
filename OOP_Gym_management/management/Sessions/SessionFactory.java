package gym.management.Sessions;

import gym.management.ForumType;
import gym.people.Instructor;
import gym.management.Session;

public class SessionFactory {
    public static Session createSession(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) {

        switch (sessionType) {
            case Pilates:
                return new Pilates(sessionType, dateAndTime, forumType, instructor);
            case MachinePilates:
                return new MachinePilates(sessionType, dateAndTime, forumType, instructor);
            case ThaiBoxing:
                return new ThaiBoxing(sessionType, dateAndTime, forumType, instructor);
            case Ninja:
                return new Ninja(sessionType, dateAndTime, forumType, instructor);
            default:
                throw new IllegalArgumentException("Unknown session type: " + sessionType);
        }
    }
}

