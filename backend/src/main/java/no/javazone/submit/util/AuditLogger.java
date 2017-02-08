package no.javazone.submit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static no.javazone.submit.util.AuditLogger.EventClass.BAD_DATA;
import static no.javazone.submit.util.AuditLogger.EventClass.EXPECTED;
import static no.javazone.submit.util.AuditLogger.EventClass.SECURITY;

public class AuditLogger {

    public enum Event {
        CREATE_TOKEN(EXPECTED),
        REMOVE_TOKEN_FROM_BROWSER_LOCAL_STORAGE(EXPECTED),
        GET_ALL_TALKS(EXPECTED),
        GET_SINGLE_TALK(EXPECTED),
        CREATE_DRAFT(EXPECTED),
        UPDATE_TALK(EXPECTED),
        UPLOAD_SPEAKER_PICTURE(EXPECTED),
        GET_SPEAKER_PICTURE(EXPECTED),
        USER_AUTHENTICATION_OK(EXPECTED),
        SENT_EMAIL(EXPECTED),
        SENT_SLACK_MESSAGE(EXPECTED),

        ILLEGAL_TALK_ACCESS(SECURITY),
        EDIT_UNEDITABLE_TALK(SECURITY),
        ILLEGAL_SPEAKER_FOR_PICTURE_UPLOAD(SECURITY),
        GET_SPEAKER_IMAGE_MISSING_IMAGE(SECURITY),
        GET_SPEAKER_IMAGE_MISSING_SPEAKER(SECURITY),
        WRONG_ACCESS_TOKEN_IN_RESOURCE(SECURITY),
        USER_AUTHENTICATION_WRONG_TOKEN(SECURITY),

        INVALID_TALK_FIELD(BAD_DATA);

        private final EventClass eventClass;

        Event(EventClass eventClass) {
            this.eventClass = eventClass;
        }
    }

    public enum EventClass {
        EXPECTED,
        BAD_DATA,
        SECURITY
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogger.class);

    public static void log(Event event, String ... logappends) {
        LOG.info("AUDIT EVENT - [" + event.eventClass + "] - [" + event.name() + "] - " + Stream.of(logappends).map(s -> "[" + s + "]").collect(joining(" - ")));
    }

}
