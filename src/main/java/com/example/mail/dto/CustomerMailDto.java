package com.example.mail.dto;

import lombok.Data;
@Data
public class CustomerMailDto 
{
	private String to;
	private  String subject;
	private String fileName;
	private CustomerMailParameterDto customerMailParameterDto;
}
