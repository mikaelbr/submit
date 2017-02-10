package no.javazone.submit.integrations.slack;

import no.javazone.submit.config.SlackConfiguration;
import no.javazone.submit.config.SleepingPillConfiguration;
import no.javazone.submit.integrations.sleepingpill.SleepingPillClient;
import no.javazone.submit.integrations.sleepingpill.model.get.Sessions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SlackClientManualTest {

    private SlackClient slack;
    private SleepingPillClient sleepingpill;

    @Before
    public void before() {
        SlackConfiguration slackConfiguration = new SlackConfiguration();
        slackConfiguration.setToken("SECRET");
        slack = new SlackClient(slackConfiguration);

        SleepingPillConfiguration configuration = new SleepingPillConfiguration();
        configuration.baseUri = "https://sleepingpill.javazone.no";
        configuration.username = "SECRET";
        configuration.password = "SECRET";
        sleepingpill = new SleepingPillClient(configuration);
    }

    @Test
    public void mark_for_review() {
        slack.postTalkMarkedForInReview(
                "1273hj123213132ukjnk",
                "My awesome new talk",
                "lightning-talk",
                "45",
                "en",
                "This is a long and windy abstract\n\nWell, it contains newlines as well!\n\nLook at how long it is! :)",
                "Espen Herseth Halvorsen",
                "https://submit.javazone.no/api/submissions/e8df80124dbd4195ae4b4779493a795b/speakers/1766e9ab3e7a45d6a60b853b76e2b13d/picture"
        );
    }

    @Test
    public void mark_for_not_in_review() {
        slack.postTalkMarkedForNotInReview(
                "1273hj123213132ukjnk",
                "My awesome new talk",
                "Espen Herseth Halvorsen",
                "espenhh@gmail.com",
                "https://submit.javazone.no/api/submissions/e8df80124dbd4195ae4b4779493a795b/speakers/1766e9ab3e7a45d6a60b853b76e2b13d/picture"
        );
    }

    @Test
    public void post_statistics() {
        String conferenceId = sleepingpill.getConferences().getIdFromSlug("javazone_2017");
        Sessions talks = sleepingpill.getTalksByConferenceId(conferenceId);
        slack.postStatistics(talks);
    }

}
