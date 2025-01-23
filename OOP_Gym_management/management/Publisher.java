package gym.management;

import gym.people.Client;

import java.util.List;

public class Publisher implements Subject{ //for OBSERVER design pattern

    public void notify(Session session, String msg) {
        for (int i = 0; i < session.getParticipants().size(); i++) {
            session.getParticipants().get(i).update(msg);
        }
    }

    public void notify(String date, String msg, List<Session> sessions) {
        for (Session session : sessions) {
            String sessionDate = session.getDateAndTime().substring(0, 10);
            if (sessionDate.equals(date)) {
                for (int j = 0; j < session.getParticipants().size(); j++) {
                    session.getParticipants().get(j).update(msg);
                }
            }
        }
    }

    public void notify(String msg, List<Client> clients) {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).update(msg);
        }
    }
}
