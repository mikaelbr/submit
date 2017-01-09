package no.javazone.integrations.sleepingpill;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.javazone.config.SleepingPillConfiguration;
import no.javazone.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.integrations.sleepingpill.model.create.NewSession;
import no.javazone.integrations.sleepingpill.model.get.Conferences;
import no.javazone.integrations.sleepingpill.model.get.Session;
import no.javazone.integrations.sleepingpill.model.get.Sessions;
import no.javazone.integrations.sleepingpill.model.update.UpdatedSession;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class SleepingPillClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final int TIMEOUT = 10_000;

    private final String baseUri;
    private final CloseableHttpClient client;
    private final ObjectMapper objectmapper;

    @Autowired
    public SleepingPillClient(SleepingPillConfiguration sleepingPillConfiguration) {
        this.baseUri = sleepingPillConfiguration.baseUri;
        client = createHttpClient();
        objectmapper = createObjectmapper();
    }

    public Conferences getConferences() {
        return get("/data/conference", Conferences.class);
    }

    public Sessions getTalksForSpeakerByEmail(String email) {
        return get("/data/submitter/" + email + "/session", Sessions.class);
    }

    public Session getSession(String sessionId) {
        return get("/data/session/" + sessionId, Session.class);
    }

    public CreatedSession createSession(String conferenceId, NewSession session) {
        return post("/data/conference/" + conferenceId + "/session", session, CreatedSession.class);
    }

    public void updateSession(String sessionId, UpdatedSession session) {
        put("/data/session/" + sessionId, session, null);
    }

    private <T> T get(String path, Class<T> responseType) {
        HttpGet httpGet = new HttpGet(baseUri + path);
        return request(httpGet, path, responseType);
    }

    private <T> T post(String path, Object body, Class<T> responseType) {
        HttpPost httpPost = new HttpPost(baseUri + path);
        addEntity(httpPost, body);
        return request(httpPost, path, responseType);
    }

    private <T> T put(String path, Object body, Class<T> responseType) {
        HttpPut httpPut = new HttpPut(baseUri + path);
        addEntity(httpPut, body);
        return request(httpPut, path, responseType);
    }

    private void addEntity(HttpEntityEnclosingRequestBase request, Object body) {
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            entity.setContent(new ByteArrayInputStream(objectmapper.writeValueAsBytes(body)));
        } catch (JsonProcessingException e) {
            LOG.warn("Error serializing object to JSON", e);
            throw new RuntimeException(e);
        }
        request.setEntity(entity);
    }

    private <T> T request(HttpUriRequest request, String path, Class<T> responseType) {
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        try (CloseableHttpResponse response = client.execute(request)) {
            if (responseType != null) {
                return objectmapper.readValue(response.getEntity().getContent(), responseType);
            } else {
                return null;
            }
        } catch (IOException e) {
            LOG.warn("Error when doing http request to " + baseUri + path, e);
            throw new RuntimeException(e);
        }
    }

    private CloseableHttpClient createHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    private static ObjectMapper createObjectmapper() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(
                mapper.getSerializationConfig().getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
        );
        return mapper;
    }
}

