package com.example.mail.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mail.dto.CustomerMailDto;
import com.example.mail.dto.EnquiryMailDTO;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerMailController 
{
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@PostMapping(value = "/mails")
	public ResponseEntity<String> customerMail(@RequestBody CustomerMailDto customerMailDto)
	{
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		
		MimeMessageHelper message=new  MimeMessageHelper(mimeMessage);
		
		try
		{
			message.setFrom(from);
			message.setTo(customerMailDto.getTo());
			message.setSubject(customerMailDto.getSubject());
			message.setText(readMailTextFromMailFile(customerMailDto), true);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		javaMailSender.send(mimeMessage);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	public static String readMailTextFromMailFile(CustomerMailDto customerMailDto) 
	{
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(customerMailDto.getFileName());
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();

			while (str != null) {
				sb.append(str);
				str = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String body = sb.toString();
				  body=body.replace("[Customer Name]", customerMailDto.getCustomerMailParameterDto().getFirstName()+" "+customerMailDto.getCustomerMailParameterDto().getMiddleName()+" "+customerMailDto.getCustomerMailParameterDto().getLastName());
				  body=body.replace("[Customer Email]", customerMailDto.getCustomerMailParameterDto().getEmailId());
				  body=body.replace("[Customer Phone Number]",String.valueOf(customerMailDto.getCustomerMailParameterDto().getContactNo()));
				  body=body.replace("[Customer ID]", String.valueOf(customerMailDto.getCustomerMailParameterDto().getCustomerId()));
				  body=body.replace("[Username]",customerMailDto.getCustomerMailParameterDto().getEmailId());
				  body=body.replace("[Password]",customerMailDto.getCustomerMailParameterDto().getPassword());	 
		return body;
	}
}
