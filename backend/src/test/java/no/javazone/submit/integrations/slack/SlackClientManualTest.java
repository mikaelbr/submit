package no.javazone.submit.integrations.slack;

import no.javazone.submit.config.SlackConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SlackClientManualTest {

    private SlackClient client;

    @Before
    public void before() {
        SlackConfiguration slackConfiguration = new SlackConfiguration();
        slackConfiguration.setToken("SECRET");
        client = new SlackClient(slackConfiguration);
    }

    @Test
    public void mark_for_review() {
        client.postTalkMarkedForInReview(
                "1273hj123213132ukjnk",
                "My awesome new talk",
                "lightning-talk",
                "45",
                "en",
                "This is a long and windy abstract\n\nWell, it contains newlines as well!\n\nLook at how long it is! :)",
                "Espen Herseth Halvorsen",
                "espenhh@gmail.com",
                "https://submit.javazone.no/api/submissions/e8df80124dbd4195ae4b4779493a795b/speakers/1766e9ab3e7a45d6a60b853b76e2b13d/picture"
        );
    }

    @Test
    public void mark_for_not_in_review() {
        client.postTalkMarkedForNotInReview(
                "1273hj123213132ukjnk",
                "My awesome new talk",
                "Espen Herseth Halvorsen",
                "espenhh@gmail.com",
                "https://submit.javazone.no/api/submissions/e8df80124dbd4195ae4b4779493a795b/speakers/1766e9ab3e7a45d6a60b853b76e2b13d/picture"
        );
    }

}
