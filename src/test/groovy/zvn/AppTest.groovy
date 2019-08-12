package zvn

import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient
import spock.lang.Specification

import java.time.Instant

class AppTest extends Specification {
    def "testing unmarshalling and the format of the URI"() {
        setup:
        def app = new App()

        def client = Mock(CloseableHttpClient)
        def response = Mock(CloseableHttpResponse)
        def entity = Mock(HttpEntity)
        def instant = Instant.now()

        when:
        def result = app.get(client, instant)

        then:
        1 * client.execute({ it.getURI().toString() ==~
                /.*loans\/marketplace\?datePublished__gt=\d{4}-\d\d-\d\dT\d\d%3A\d\d%3A\d\d.\d{6}Z.*/ }) >> response
        1 * response.getEntity() >> entity
        1 * entity.getContent() >> new ByteArrayInputStream(getClass()
                .getResource('/loans.json').text.getBytes( 'UTF-8' ) )

        result.size() == 2
    }

    def "in case of error an empty collection is returned"() {
        setup:
        def app = new App()

        def client = Mock(CloseableHttpClient)

        when:
        def result = app.get(client, Instant.now())

        then:
        1 * client.execute(_) >> { throw new IOException("Oops!") }

        result.size() == 0
    }
}
