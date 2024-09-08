package com.example.aws.springbootdynamodbcrud.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public String uploadFile(MultipartFile file, String firstname, String lastname) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        } finally {
            fileObj.delete();
        }
        String s3Url = amazonS3.getUrl(bucketName, fileName).toString();

        String messageBody = String.format("%s;%s;%s", s3Url, firstname, lastname);
        amazonSQS.sendMessage(queueUrl, messageBody);

        return s3Url;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
