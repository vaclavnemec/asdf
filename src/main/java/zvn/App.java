package zvn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@CommonsLog
public class App {

    private static final int INTERVAL = 5 * 60 * 1000;
    private static final String URI = "https://api.zonky.cz/loans/marketplace";

    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
        App app = new App();

        Instant last = Instant.now().minusMillis(INTERVAL);

        while (true) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

                List<Loan> loans = app.get(httpClient, last);

                loans.forEach(loan -> log.info("Loan: " + loan));

                last = loans.stream().map(loan -> loan.getDatePublished().toInstant())
                        .max(Comparator.naturalOrder())
                        .orElse(last);

                log.info("The last published date is: " + last);

                Thread.sleep(INTERVAL);
            }
        }
    }

    List<Loan> get(CloseableHttpClient httpClient, Instant datePublished) throws URISyntaxException {

        URIBuilder builder = new URIBuilder(URI);

        builder.setParameter("datePublished__gt", datePublished.toString());

        URI build = builder.build();

        log.info("URI: " + build);

        HttpGet httpGet = new HttpGet(build);
        httpGet.addHeader("X-Size", "100000");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();

            try (InputStream content = entity.getContent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType javaType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Loan.class);
                return objectMapper.readValue(content, javaType);
            }

        } catch (IOException e) {
            // i don't want to kill the whole program, so just logging is fine, then I return an empty list
            log.error(e);
            return Collections.emptyList();
        }
    }
}
