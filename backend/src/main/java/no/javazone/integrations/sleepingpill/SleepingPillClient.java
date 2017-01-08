package no.javazone.integrations.sleepingpill;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.javazone.config.SleepingPillConfiguration;
import no.javazone.integrations.sleepingpill.model.Conferences;
import no.javazone.integrations.sleepingpill.model.Sessions;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private <T> T get(String path, Class<T> responseType) {
        HttpGet httpGet = new HttpGet(baseUri + path);
        httpGet.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            return objectmapper.readValue(response.getEntity().getContent(), responseType);
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
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}

