package no.javazone.integrations.sleepingpill;

import no.javazone.config.SleepingPillConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

}
