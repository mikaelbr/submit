package no.javazone.integrations.sleepingpill.model;

public class Session {

    public String sessionId;
    public String conferenceId;

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", conferenceId='" + conferenceId + '\'' +
                '}';
    }
}
