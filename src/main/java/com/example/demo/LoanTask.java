package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class LoanTask {

    private static final Logger log = LoggerFactory.getLogger(LoanTask.class);

    private int loanTaskFixedRate;
    private LoanService loanService;

    public LoanTask(LoanService loanService, @Value("${loanTaskFixedRate}") int loanTaskFixedRate) {
        this.loanService = loanService;
        this.loanTaskFixedRate = loanTaskFixedRate;
    }

    @Scheduled(fixedRateString = "${loanTaskFixedRate}")
    public void reportNewLoans() {

        List<Loan> loans = loanService.getRecentLoans(loanTaskFixedRate);

        log.info("The service returned {} loans.", loans.size());

        loans.stream().map(Loan::getAmount).max(Comparator.naturalOrder()).ifPresent(
                max -> log.info("The highest amount is {}", max)
        );

        if (loans.size() != 0) {
            log.info(
                    "Sum of the loan amounts is {}", loans.stream().map(Loan::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }

        loans.forEach(loan -> log.info(loan.toString()));
    }
}