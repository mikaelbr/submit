package no.javazone.submit.integrations.sleepingpill;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.javazone.submit.config.SleepingPillConfiguration;
import no.javazone.submit.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.submit.integrations.sleepingpill.model.create.NewSession;
import no.javazone.submit.integrations.sleepingpill.model.get.Conferences;
import no.javazone.submit.integrations.sleepingpill.model.get.Session;
import no.javazone.submit.integrations.sleepingpill.model.get.Sessions;
import no.javazone.submit.integrations.sleepingpill.model.picture.CreatedPicture;
import no.javazone.submit.integrations.sleepingpill.model.update.UpdatedSession;
import no.javazone.submit.util.StreamUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class SleepingPillClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final int TIMEOUT = 10_000;

    private final String baseUri;
    private final SleepingPillConfiguration sleepingPillConfiguration;
    private final CloseableHttpClient client;
    private final ObjectMapper objectmapper;
    private final HttpContext context;

    @Autowired
    public SleepingPillClient(SleepingPillConfiguration sleepingPillConfiguration) {
        this.baseUri = sleepingPillConfiguration.baseUri;
        this.sleepingPillConfiguration = sleepingPillConfiguration;
        client = createHttpClient();
        objectmapper = createObjectmapper();
        context = createPreemptiveAuthContext();
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

    public CreatedPicture uploadPicture(byte[] image, String mediaType) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(image));

        String path = baseUri + "/data/picture";
        HttpPost httpPost = new HttpPost(path);
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", mediaType);
        logRequest("POST", path, null);
        return request(httpPost, path, CreatedPicture.class);
    }

    public byte[] getPicture(String id) {
        long before = System.currentTimeMillis();
        String path = baseUri + "/data/picture/" + id;
        HttpGet httpGet = new HttpGet(path);
        logRequest("GET", path, null);
        try (CloseableHttpResponse response = client.execute(httpGet, context)) {
            if (response.getStatusLine().getStatusCode() >= 400) {
                LOG.warn("Could not fetch picture with id " + id + ". Got status code " + response.getStatusLine().getStatusCode());
                logResponse("GET", path, null, response.getStatusLine().getStatusCode(), before);
                throw new RuntimeException("Couldn't fetch picture");
            } else {
                logResponse("GET", path, null, response.getStatusLine().getStatusCode(), before);
                return IOUtils.toByteArray(response.getEntity().getContent());
            }
        } catch (IOException e) {
            LOG.warn("Error when doing http request to " + baseUri + path, e);
            throw new RuntimeException(e);
        }
    }

    private <T> T get(String path, Class<T> responseType) {
        HttpGet httpGet = new HttpGet(baseUri + path);
        logRequest("GET", baseUri + path, null);
        return jsonRequest(httpGet, path, responseType);
    }

    private <T> T post(String path, Object body, Class<T> responseType) {
        HttpPost httpPost = new HttpPost(baseUri + path);
        addEntity(httpPost, body);
        logRequest("POST", baseUri + path, body);
        return jsonRequest(httpPost, path, responseType);
    }

    private <T> T put(String path, Object body, Class<T> responseType) {
        HttpPut httpPut = new HttpPut(baseUri + path);
        addEntity(httpPut, body);
        logRequest("POST", baseUri + path, body);
        return jsonRequest(httpPut, path, responseType);
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

    private <T> T jsonRequest(HttpUriRequest request, String path, Class<T> responseType) {
        request.setHeader("Content-Type", "application/json");
        return request(request, path, responseType);
    }

    private <T> T request(HttpUriRequest request, String path, Class<T> responseType) {
        long before = System.currentTimeMillis();
        request.setHeader("Accept", "application/json");
        try (CloseableHttpResponse response = client.execute(request, context)) {
            if (response.getStatusLine().getStatusCode() >= 400) {
                LOG.warn("Got HTTP error (" + response.getStatusLine().getStatusCode() + ") when doing http request to " + baseUri + path + "\nRESPONSE:\n"
                        + StreamUtil.convertStreamToString(response.getEntity().getContent()));
                logResponse(request.getMethod(), path, null, response.getStatusLine().getStatusCode(), before);
                throw new ServerErrorException("Error with sleeping pill...", Response.Status.INTERNAL_SERVER_ERROR);
            } else if (responseType != null) {
                byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
                logResponse(request.getMethod(), path, bytes, response.getStatusLine().getStatusCode(), before);
                return objectmapper.readValue(new ByteArrayInputStream(bytes), responseType);
            } else {
                logResponse(request.getMethod(), path, null, response.getStatusLine().getStatusCode(), before);
                return null;
            }
        } catch (IOException e) {
            LOG.warn("Error when doing http request to " + baseUri + path, e);
            throw new RuntimeException(e);
        }
    }

    private CloseableHttpClient createHttpClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(sleepingPillConfiguration.username, sleepingPillConfiguration.password));

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }

    private HttpContext createPreemptiveAuthContext() {
        AuthCache authCache = new BasicAuthCache();
        authCache.put(HttpHost.create(sleepingPillConfiguration.baseUri), new BasicScheme());

        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);
        return localContext;
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

    private void logRequest(String method, String url, Object body) {
        try {
            LOG.info(
                    String.format("SLEEPINGPILL REQUEST: [Method: %s] [URL: %s] [BODY: %s]",
                            method,
                            url,
                            body != null ? objectmapper.writeValueAsString(body) : "[NO BODY OR BODY NOT LOGGED]"
                    )
            );
        } catch (JsonProcessingException e) {
            LOG.error("Error when logging request", e);
        }
    }

    private void logResponse(String method, String url, byte[] bytes, int statusCode, long beforeMillis) {
        try {
            LOG.info(
                    String.format("SLEEPINGPILL RESPONSE: [Status: %s] [Duration: %s ms] [Method: %s] [URL: %s] [BODY: %s]",
                            statusCode,
                            System.currentTimeMillis() - beforeMillis,
                            method,
                            url,
                            bytes != null ? new String(bytes, "UTF-8") : "[NO BODY OR BODY NOT LOGGED]"
                    )
            );
        } catch (UnsupportedEncodingException e) {
            LOG.error("Error when logging response", e);
        }
    }
}

