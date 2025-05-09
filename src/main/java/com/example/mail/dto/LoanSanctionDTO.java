package com.example.mail.dto;

import lombok.Data;

@Data
public class LoanSanctionDTO 
{
	private String applicantName;
	
	private Long contactNo;
	
	private Integer customerId;
	
	private Integer appllicantId;
	
	private String emailId;
	
	private Integer cibilScore;
	
	private Integer loanTenureInMonth;
	
	private Long requestedLoanAmount;
	
	private Double NetMonthlyIncome;
	
	private String IFSC_Code;
	
	private Long accountNumber;
}
