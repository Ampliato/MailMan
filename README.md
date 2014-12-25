# MailMan
---
An email library that makes sense.

This library was created out of frustation with existing popular libraries. They are too complex, not really testable and make it hard to separate responsabilities.

## Sending emails

Sending emails is simple. All you need is an `EmailSender`, write an `Email`, say who is sending it and set its recipients.

```java
EmailSender emailSender = new SmtpEmailSender("smtp.gmail.com");
emailSender.setAuthentication("username", "password");

Email email = new Email();
email.setMessage("Hello, World!");
email.setFrom("me@world.com", "Me");
email.addRecipient(RecipientType.TO, "You", "you@world.com");

emailSender.send(email);
```

There are two important things to notice here. First, the `EmailSender` creation is completely isolate from the `Email` creation. That is important because it means that when you writing emails, you don't need to know how they'll be sent, you just need someone to give you an `EmailSender` and you are set. The other thing is that `Email` is a POJO, so it is easy to have fun with it. For instance, you can serialize it, persist it, read it from an structured file format, anything.
