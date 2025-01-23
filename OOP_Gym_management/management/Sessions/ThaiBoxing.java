package gym.management.Sessions;

import gym.management.ForumType;
import gym.people.Instructor;
import gym.management.Session;

public class ThaiBoxing extends Session {
    public ThaiBoxing(SessionType sessionType, String dateAndTime, ForumType forumType, Instructor instructor) {
        super(sessionType, dateAndTime, forumType, instructor);
    }

}
