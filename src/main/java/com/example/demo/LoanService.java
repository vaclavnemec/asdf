package com.example.demo;

import java.util.List;

public interface LoanService {
    List<Loan> getRecentLoans(int millis);
}
