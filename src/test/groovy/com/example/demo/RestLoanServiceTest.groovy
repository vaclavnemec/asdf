package com.example.demo

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class RestLoanServiceTest extends Specification {

    def "the rest loan service correctly returns loan objects based on a mocked rest template" () {

        def template = Mock(RestTemplate)

        def builder = Mock(RestTemplateBuilder)
        builder.build() >> template

        RestLoanService service = new RestLoanService("http://dummyhost", builder)

        ResponseEntity<List<Loan>> entity = new ResponseEntity<>(loans, HttpStatus.OK)

        when:
        def result = service.getRecentLoans(123)

        then:
        1 * template.exchange(*_) >> entity

        result.size() == loans.size()

        where:
        loans << [[], [new Loan(id: "123", amount: 5, story: "abc"), new Loan(id: "124", amount: 15, story: "xyz")]]
    }

    def "validation of a parameter millis" () {
        def builder = Mock(RestTemplateBuilder)
        when:
        new RestLoanService(null, builder).getRecentLoans(millis)
        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Parameter millis must be a positive number"
        where:
        millis << [0,-10]
    }

}
