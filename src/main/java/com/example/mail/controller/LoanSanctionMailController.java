package com.example.mail.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

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

import com.example.mail.dto.LoanSanctionMailDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/sanction")
@Slf4j
public class LoanSanctionMailController 
{

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@PostMapping()
	public ResponseEntity<String> loanSanctionMail(@RequestBody LoanSanctionMailDTO loanSanctionMailDTO) throws MessagingException
	{
		log.info("Entry");
		System.out.println(loanSanctionMailDTO);
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		
		MimeMessageHelper message=new  MimeMessageHelper(mimeMessage, true);
		
			message.setFrom(from);
			message.setTo(loanSanctionMailDTO.getTo());
			message.setSubject(loanSanctionMailDTO.getSubject());
			
			if (loanSanctionMailDTO.getGenerateSanctionLetter() != null && loanSanctionMailDTO.getAttachmentName() != null) 
			{
				message.addAttachment(loanSanctionMailDTO.getAttachmentName(),
                    new ByteArrayDataSource(loanSanctionMailDTO.getGenerateSanctionLetter(), "application/pdf"));
			}
//			ByteArrayDataSource dataSource = new ByteArrayDataSource(loanSanctionMailDTO.getGenerateSanctionLetter(), "application/pdf");
//			message.addAttachment(loanSanctionMailDTO.getGenerateSanctionLetter(), new Byte);
//			message.addAttachment("LoanSanctionLetter.pdf", new ByteArrayResource(loanSanctionMailDTO.getGenerateSanctionLetter()));
			message.setText(readMailTextFromMailFile(loanSanctionMailDTO), true);
			
		 javaMailSender.send(mimeMessage);
		return new ResponseEntity<String>("!!!...Mail Sent SuccessFully...!!!", HttpStatus.OK);
	}
	
	public static String readMailTextFromMailFile(LoanSanctionMailDTO loanSanctionMailDTO) 
	{
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(loanSanctionMailDTO.getFileName());
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
				  body=body.replace("[Customer Name]", loanSanctionMailDTO.getLoanSanctionDTO().getApplicantName()); 
		return body;
	}
}
