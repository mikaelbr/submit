package no.javazone.integrations.sleepingpill;

import no.javazone.config.SleepingPillConfiguration;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.integrations.sleepingpill.model.create.NewSession;
import no.javazone.integrations.sleepingpill.model.create.NewSpeaker;
import no.javazone.integrations.sleepingpill.model.get.Session;
import no.javazone.integrations.sleepingpill.model.update.UpdatedSession;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;

@Ignore
public class SleepingPillClientManualTest {

    private SleepingPillClient client;

    @Before
    public void setup() {
        SleepingPillConfiguration configuration = new SleepingPillConfiguration();
        configuration.baseUri = "https://sleepingpill.javazone.no";
        client = new SleepingPillClient(configuration);
    }

    @Test
    public void fetch_conferences() {
        client.getConferences().conferences.forEach(System.out::println);
    }

    @Test
    public void fetch_talks_by_email() {
        client.getTalksForSpeakerByEmail("espen.halvorsen@bekk.no").sessions.forEach(System.out::println);
    }

    @Test
    public void get_session() {
        Session session = client.getSession("b9d4c352911841a097314e254bf5d643");
        System.out.println(session);
    }

    @Test
    public void create_session() {
        String conferenceId = client.getConferences().getIdFromSlug("javazone_2016");

        List<NewSpeaker> speakers = asList(
                new NewSpeaker(
                        "Espen Herseth Halvorsen",
                        "espenhh@example.com",
                        "Espens flotte bio"
                )
        );
        NewSession session = new NewSession(
                SessionStatus.DRAFT,
                "espenhh@example.com",
                "Min flotte talk",
                "Har et flott abtract",
                "Har en fin intended audience",
                "presentation",
                "no",
                "Har en fin outline",
                speakers
        );
        CreatedSession createdSession = client.createSession(conferenceId, session);

        System.out.println("Created session with ID: " + createdSession.id);
    }

    @Test
    public void update_session() {
        String sessionId = "b9d4c352911841a097314e254bf5d643";

        UpdatedSession session = new UpdatedSession(
                SessionStatus.SUBMITTED,
                "New title",
                "New abstract",
                "New intended audience",
                "presentation",
                "en",
                "New outline"
        );
        client.updateSession(sessionId, session);

        System.out.println("Updated session");
    }

}
