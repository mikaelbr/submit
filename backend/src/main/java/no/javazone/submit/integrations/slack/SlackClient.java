package no.javazone.submit.integrations.slack;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import no.javazone.submit.config.SlackConfiguration;
import no.javazone.submit.util.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static no.javazone.submit.util.AuditLogger.Event.SENT_SLACK_MESSAGE;

@Service
public class SlackClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SlackSession slack;

    public SlackClient(SlackConfiguration slackConfiguration) {
        slack = SlackSessionFactory.createWebSocketSlackSession(slackConfiguration.token);
        connectIfNessesary();
    }

    public void postTalkMarkedForInReview(String id, String title, String format, String length, String language, String theAbstract, String submitterName, String submitterEmail, String submitterImage) {
        connectIfNessesary();

        SlackChannel channel = slack.findChannelByName("javazone-submit");

        SlackAttachment attachment = new SlackAttachment(title, "", "_Speaker has changed the talk status to 'ready for review'_", null);
        attachment.setColor("#30a74d");

        attachment.addField("Description of the talk", theAbstract, false);
        attachment.addField("Format", format + " (" + length + "min)", true);
        attachment.addField("Language", language, true);
        if(submitterImage != null) {
            attachment.setAuthorIcon(submitterImage);
        }
        attachment.setTitleLink("http://javazone.no/cakeredux?talkId=" + id);
        attachment.setAuthorName("Speaker: " + submitterName);
        attachment.addMarkdownIn("text");

        SlackPreparedMessage message = new SlackPreparedMessage.Builder()
                .addAttachment(attachment)
                .build();
        slack.sendMessage(channel, message);

        AuditLogger.log(SENT_SLACK_MESSAGE, "session " + id, "type marked-for-review");
    }

    public void postTalkMarkedForNotInReview(String id, String title, String submitterName, String submitterEmail, String submitterImage) {
        connectIfNessesary();

        SlackChannel channel = slack.findChannelByName("javazone-submit");

        SlackAttachment attachment = new SlackAttachment(title, "", "_Speaker has changed the talk status back to 'not in review'_", null);
        attachment.setColor("#b63d9d");
        if(submitterImage != null) {
            attachment.setAuthorIcon(submitterImage);
        }
        attachment.setTitleLink("http://javazone.no/cakeredux?talkId=" + id);
        attachment.setAuthorName("Speaker: " + submitterName);
        attachment.addMarkdownIn("text");

        SlackPreparedMessage message = new SlackPreparedMessage.Builder()
                .addAttachment(attachment)
                .build();
        slack.sendMessage(channel, message);

        AuditLogger.log(SENT_SLACK_MESSAGE, "session " + id, "type marked-for-not-in-review");
    }

    private void connectIfNessesary() {
        if(!slack.isConnected()) {
            try {
                slack.connect();
            } catch (IOException e) {
                LOG.error("Could not connect to slack", e);
            }
        }
    }

}
