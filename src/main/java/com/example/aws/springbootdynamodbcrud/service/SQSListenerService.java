package com.example.aws.springbootdynamodbcrud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.example.aws.springbootdynamodbcrud.entity.Person;
import com.example.aws.springbootdynamodbcrud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SQSListenerService {

    private static final Logger logger = LoggerFactory.getLogger(SQSListenerService.class);

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private PersonRepository personRepository;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Scheduled(fixedDelay = 10000)
    public void pollSQS() {
        logger.info("Polling SQS queue: {}", queueUrl);

        List<Message> messages = amazonSQS.receiveMessage(queueUrl).getMessages();
        if (messages.isEmpty()) {
            logger.info("No messages found in the queue.");
        } else {
            for (Message message : messages) {
                logger.info("Received message: {}", message.getBody());

                try {

                    String[] messageParts = message.getBody().split(";");
                    String s3Url = messageParts[0];
                    String firstname = messageParts[1];
                    String lastname = messageParts[2];

                    List<Person> existingPersons = personRepository.findByPhotoUrl(s3Url);
                    if (existingPersons.isEmpty()) {
                        Person person = new Person();
                        person.setPhotoUrl(s3Url);
                        person.setFirstname(firstname);
                        person.setLastname(lastname);

                        personRepository.save(person);

                        logger.info("Saved person with photo URL: {} and name: {} {}", s3Url, firstname, lastname);
                    } else {
                        logger.info("Person with photo URL: {} already exists.", s3Url);
                    }

                    amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
                    logger.info("Deleted message with receipt handle: {}", message.getReceiptHandle());

                } catch (Exception e) {
                    logger.error("Error processing message: {}", message.getBody(), e);
                }
            }
        }
    }
}
