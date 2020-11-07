<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Contributing](#contributing)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)



<!-- ABOUT THE PROJECT -->
## About The Project

In modern cloud architecture, applications are decoupled into smaller, independent building blocks that are easier to develop, deploy and maintain. Message queues provide communication and coordination for these distributed applications. 

The basic architecture of a message queue is simple; there are client applications called producers that create messages and deliver them to the message queue. Another application, called a consumer, connects to the queue and gets the messages to be processed. Messages placed onto the queue are stored until the consumer retrieves them.

It provides endpoints that allow software components to connect to the queue in order to send and receive messages. The messages are usually small, and can be things like requests, replies, error messages, or just plain information.

An example of a message could be something that tells one system to start processing a task, it could contain information about a finished task or just be a plain message.

This message queue can be deployed as a service in cloud foundry (Platform as a Service) on any cloud infrastructure of your choice.

### Built With
* [Java (JAX-RS) using Jersey](https://eclipse-ee4j.github.io/jersey/)
* [Open Service Broker API](https://www.openservicebrokerapi.org/)
* [PostgreSQL](https://www.postgresql.org/)



<!-- GETTING STARTED -->
## Getting Started
To get a local copy up and running follow these simple example steps.

### Prerequisites

You will need a PostgreSQL service instance for this application to work. 

Create a service instance of PostgreSQL in the org/space where you are deploying this app.

Bind the service instance with the app.

You can do this in 2 ways -
1. Pre-deployment - Add the name of the service instance in the manifest.yml file   
```yml
services:
- postgres-instance
```
2. Post-deployment - Bind the PostgreSQL service instance to the app
```sh
cf bind-service APP_NAME SERVICE_INSTANCE
```

### Installation

1. Clone the repo
```sh
git clone https://github.com/en-jai-neer/queue-as-a-service.git
```
2. Install Maven dependencies
```sh
mvn clean install
```
3. Deploy the app in your org/space of cloud foundry
```sh
cf push APP_NAME [-d DOMAIN]
```

<!-- USAGE EXAMPLES -->
## Usage

1. To use the app as a service, you will need to register the app as a service broker and then enable the access of this service to your org
```sh
cf create-service-broker SERVICE_BROKER USERNAME PASSWORD URL
```
```sh
cf enable-service-access SERVICE [-o ORG]
```

2. Now you can create as many instances of this service as you want!
```sh
cf create-service SERVICE PLAN SERVICE_INSTANCE
```

3. Now, will need a key to access this service instance
```sh
cf create-service-key SERVICE_INSTANCE SERVICE_KEY
```

On successful execution this command will return you username, password and the queue_id to access the service instance

You can now access, add and retrieve messages/data from the queue!

Use Postman to access the apis hassle-free
1. Add a message
```
PUT request
url: 'https://<service_name>.<domain_name>/v1/queueservice/add/<queue_id>'
Add basic authentication header and use the username and password received after creating a key
data: [the message you want add to the queue]
```
 
2. View the message at the beginning of the queue
```
GET request
url: 'https://<service_name>.<domain_name>/v1/queueservice/peek/<queue_id>'
Add basic authentication header and use the username and password received after creating a key
```

3. Delete the message at the beginning of the queue
```
DELETE request
url: 'https://<service_name>.<domain_name>/v1/queueservice/remove/<queue_id>'
Add basic authentication header and use the username and password received after creating a key
```

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- CONTACT -->
## Contact

Jai Jain - jaisandeepjain@gmail.com

Project Link: [https://github.com/en-jai-neer/queue-as-a-service](https://github.com/en-jai-neer/queue-as-a-service)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [RESTful Service in Jersey](https://crunchify.com/how-to-build-restful-service-with-java-using-jax-rs-and-jersey/)
* [Authentication](https://howtodoinjava.com/jersey/jersey-rest-security/)
* [Cloud Foundry Docs](https://docs.cloudfoundry.org/)
* [Maven Tutorial](https://www.baeldung.com/maven)
