package com.wallet.repositories;



//Interface
public interface EmailService {

 // Method
 // To send a simple email
 String sendSimpleMail(com.wallet.entitis.EmailDetails details);

 // Method
 // To send an email with attachment
 String sendMailWithAttachment(com.wallet.entitis.EmailDetails details);
}