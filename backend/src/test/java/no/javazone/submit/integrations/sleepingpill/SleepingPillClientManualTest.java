package no.javazone.submit.integrations.sleepingpill;

import no.javazone.submit.config.SleepingPillConfiguration;
import no.javazone.submit.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.submit.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.submit.integrations.sleepingpill.model.create.NewSession;
import no.javazone.submit.integrations.sleepingpill.model.create.NewSpeaker;
import no.javazone.submit.integrations.sleepingpill.model.get.Session;
import no.javazone.submit.integrations.sleepingpill.model.picture.CreatedPicture;
import no.javazone.submit.integrations.sleepingpill.model.update.UpdatedSession;
import no.javazone.submit.integrations.sleepingpill.model.update.UpdatedSpeaker;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.singletonList;

@Ignore
public class SleepingPillClientManualTest {

    private SleepingPillClient client;

    @Before
    public void setup() {
        SleepingPillConfiguration configuration = new SleepingPillConfiguration();
        configuration.baseUri = "https://sleepingpill.javazone.no";
        configuration.username = "USERNAME";
        configuration.password = "PASSWORD";
        client = new SleepingPillClient(configuration);
    }

    @Test
    public void fetch_conferences() {
        client.getConferences().conferences.forEach(System.out::println);
    }

    @Test
    public void fetch_talks_by_conference_id() {
        String id = client.getConferences().getIdFromSlug("javazone_2017");
        client.getTalksByConferenceId(id).sessions.forEach(System.out::println);
    }

    @Test
    public void fetch_talks_by_email() {
        client.getTalksForSpeakerByEmail("espen.halvorsen@bekk.no").sessions.forEach(System.out::println);
    }

    @Test
    public void get_session() {
        Session session = client.getSession("5f38466d0b5e43c0a26e1cde537151bf");
        System.out.println(session);
    }

    @Test
    public void create_session() {
        String conferenceId = client.getConferences().getIdFromSlug("javazone_2016");

        List<NewSpeaker> speakers = singletonList(
                new NewSpeaker(
                        "Espen Herseth Halvorsen",
                        "espenhh@example.com",
                        "Espens flotte bio",
                        "1234",
                        "@Espenhh"
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
                "beginner",
                "",
                "",
                speakers);
        CreatedSession createdSession = client.createSession(conferenceId, session);

        System.out.println("Created session with ID: " + createdSession.id);
    }

    @Test
    public void update_session() {
        String sessionId = "35c404712dd54ae0a81f9765ec14dace";

        List<UpdatedSpeaker> speakers = singletonList(
                new UpdatedSpeaker(
                        "3006c2cf0fc34c4293d5474eb8242aec",
                        "Espen Updated Halvorsen",
                        "espenhh@example.com",
                        "Espens flotte bio",
                        "1234",
                        "@JavaZone",
                        "picture-id"
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
                "advanced",
                "",
                "",
                speakers
        );
        client.updateSession(sessionId, session);

        System.out.println("Updated session");
    }

    @Test
    public void add_picture_to_talk() throws IOException {
        InputStream image = getClass().getResourceAsStream("/testimage.jpg");
        byte[] sentInBytes = IOUtils.toByteArray(image);
        CreatedPicture picture = client.uploadPicture(sentInBytes, "image/jpeg");
        byte[] downloadedPicture = client.getPicture(picture.id);

        System.out.println("Uploaded picture with byte length " + sentInBytes.length + ", got id " + picture.id);
        System.out.println("Fetched picture back with byte length " + downloadedPicture.length);
    }

    @Test
    public void get_picture_by_id() throws IOException {
        byte[] clientPicture = client.getPicture("7f9e8ed40dc947899b4a43d28a578a11");
        FileOutputStream fos = new FileOutputStream("/Users/eh/Desktop/submitbilde/direct.jpeg");
        fos.write(clientPicture);
        fos.close();
    }

}
