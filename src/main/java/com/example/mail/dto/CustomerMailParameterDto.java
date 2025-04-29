package com.example.mail.dto;

import lombok.Data;

@Data
public class CustomerMailParameterDto
{
	private Integer customerId;

	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String emailId;

	private Long contactNo;
	
	private String password;
}
