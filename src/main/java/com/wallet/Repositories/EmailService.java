package com.wallet.Repositories;



//Interface
public interface EmailService {

 // Method
 // To send a simple email
 String sendSimpleMail(com.wallet.Entitis.EmailDetails details);

 // Method
 // To send an email with attachment
 String sendMailWithAttachment(com.wallet.Entitis.EmailDetails details);
}