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

import com.example.mail.dto.EnquiryMailDTO;

@RestController
@RequestMapping("/api/mail")
public class EnquiryMailController {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String from;

	@PostMapping(value = "/mails")
	public ResponseEntity<String> registrationMail(@RequestBody EnquiryMailDTO enquiryMailDTO) 
	{
//		MailParameterDTO mailParameterDTO = enquiryMailDTO.getMailParameterDTO();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		try 
		{
		
			message.setFrom(from);
			message.setTo(enquiryMailDTO.getTo());
			message.setSubject(enquiryMailDTO.getSubject());
		
			message.setText(readMailTextFromMailFile(enquiryMailDTO), true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		javaMailSender.send(mimeMessage);

		return new ResponseEntity<String>("Mail Sent", HttpStatus.OK);
	}

	public static String readMailTextFromMailFile(EnquiryMailDTO enquiryMailDTO) {
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(enquiryMailDTO.getFileName());
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
				  body=body.replace("[Customer Name]", enquiryMailDTO.getMailParameterDTO().getFirstName()+" "+enquiryMailDTO.getMailParameterDTO().getMiddleName()+" "+enquiryMailDTO.getMailParameterDTO().getLastName());
				  body=body.replace("[Customer Email]", enquiryMailDTO.getMailParameterDTO().getEmailId());
				  body=body.replace("[Customer Phone Number]",String.valueOf(enquiryMailDTO.getMailParameterDTO().getContactNo()));
				  body=body.replace("[Enquiry ID]", String.valueOf(enquiryMailDTO.getMailParameterDTO().getEnquiryId()));
				  body=body.replace("[Support Email]", "www.unified.com");
				  body=body.replace("[Support Phone Number]", "9988776655");	 

		return body;
	}
}
