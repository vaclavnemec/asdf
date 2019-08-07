package com.example.demo;

import java.util.List;

public interface LoanService {

    /**
     * @param millis only the loans published between "now" and "now - amount of milliseconds" will be returned
     * @return new loans
     */
    List<Loan> getRecentLoans(int millis);
}
