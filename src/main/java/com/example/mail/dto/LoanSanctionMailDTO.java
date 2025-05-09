package com.example.mail.dto;

import lombok.Data;

@Data
public class LoanSanctionMailDTO
{
	private String to;
	private  String subject;
	private String fileName;
	private byte[] generateSanctionLetter;
	private String attachmentName;
	private LoanSanctionDTO LoanSanctionDTO;
	
}
