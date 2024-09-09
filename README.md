# AWS Full Stack Project

## Project Overview

This project is a full-stack web application that uses AWS services to provide a comprehensive solution for managing and displaying photos. The application is built using Spring Boot for the backend, AWS services for storage and messaging, and a simple HTML frontend. 

### Features

- **Frontend**: A web interface to upload and view photos.
- **Backend**: A Spring Boot application that handles CRUD operations for photos.
- **AWS S3**: Stores photos uploaded by users.
- **AWS DynamoDB**: Stores metadata of photos including S3 URLs and user information.
- **AWS SQS**: Handles asynchronous processing of photo URLs.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Usage](#usage)


1.## Prerequisites

- **Java 11**: Required to build and run the backend application.
- **Maven**: Required to build the Spring Boot application.
- **AWS CLI**: Required for interacting with AWS services.
- **Git**: Required for version control and cloning the repository.
- **An AWS account**: For using AWS services such as S3, DynamoDB, and SQS.

2.## Installation

 **Clone the Repository**

   Clone the project repository to your local machine using Git:

   ```bash
   git clone https://github.com/vignakodali/AWS-full-stack.git
    **Navigate to the Project Directory**
          cd AWS-full-stack
    **Install Dependencies and check java and maven are there or not**
          java -version
          mvn -version
   Configuration
3.  **AWS Configuration**

   S3 Bucket: Create an S3 bucket to store the photos.
   DynamoDB Table: Create a DynamoDB table to store photo metadata.
   SQS Queue: Create an SQS queue for processing photo URLs.
   Update the application.properties file with your AWS configurations:

     **properties**
       aws.accessKeyId=YOUR_AWS_ACCESS_KEY
       aws.secretAccessKey=YOUR_AWS_SECRET_KEY
       aws.s3.bucketName=YOUR_S3_BUCKET_NAME
       aws.dynamodb.tableName=YOUR_DYNAMODB_TABLE_NAME
       aws.sqs.queueUrl=YOUR_SQS_QUEUE_URL
     **Database Configuration**

       Ensure your DynamoDB table schema matches the requirements of the application.

  4.** Usage **
   **Build the Application**

    Use Maven to build the Spring Boot application:

     mvn clean install
   **Run the Application**

     Start the Spring Boot application:

     java -jar target/your-app.jar
     The application should now be running on http://localhost:8080.

  **Access the Photo Gallery**

    Open your web browser and navigate to http://localhost:8080/photoGallery.html to view and upload photos.



 
