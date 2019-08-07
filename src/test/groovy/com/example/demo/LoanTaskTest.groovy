package com.example.demo

import org.junit.Rule
import org.springframework.boot.test.rule.OutputCapture
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class LoanTaskTest extends Specification {

    @Rule
    public OutputCapture capture = new OutputCapture()

    def "loan task output reflects the loans returned from the mocked loan service" () {

        def service = Mock(LoanService)

        def task = new LoanTask(service, 0)

        when:
        task.reportNewLoans()

        then:
        1 * service.getRecentLoans(_) >> loans

        capture.toString().contains("The service returned ${loans.size()} loans.".toString())

        where:
        loans << [[], [new Loan(id: "123", amount: 5, story: "abc"), new Loan(id: "124", amount: 15, story: "xyz")]]
    }

}
