package no.javazone.integrations.sleepingpill;

import no.javazone.config.SleepingPillConfiguration;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.integrations.sleepingpill.model.create.NewSession;
import no.javazone.integrations.sleepingpill.model.create.NewSpeaker;
import no.javazone.integrations.sleepingpill.model.get.Session;
import no.javazone.integrations.sleepingpill.model.update.UpdatedSession;
import no.javazone.integrations.sleepingpill.model.update.UpdatedSpeaker;
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
        Session session = client.getSession("7018c57f36ff46aebd55961a63a80604");
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
                "Equipment",
                "60",
                speakers
        );
        CreatedSession createdSession = client.createSession(conferenceId, session);

        System.out.println("Created session with ID: " + createdSession.id);
    }

    @Test
    public void update_session() {
        String sessionId = "cd2ae79db7f3459b9b59def7730aff79";

        List<UpdatedSpeaker> speakers = asList(
                new UpdatedSpeaker(
                        "3006c2cf0fc34c4293d5474eb8242aec",
                        "Espen Updated Halvorsen",
                        "espenhh@example.com",
                        "Espens flotte bio"
                )
        );
        UpdatedSession session = new UpdatedSession(
                SessionStatus.SUBMITTED,
                "New title",
                "New abstract",
                "New intended audience",
                "presentation",
                "en",
                "New outline",
                "New Equipment",
                "60",
                speakers
        );
        client.updateSession(sessionId, session);

        System.out.println("Updated session");
    }

}
