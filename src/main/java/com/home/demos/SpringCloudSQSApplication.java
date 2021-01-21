package com.home.demos;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class})
@RestController
public class SpringCloudSQSApplication {

    Logger logger= LoggerFactory.getLogger(SpringCloudSQSApplication.class);

    @Autowired
    private AmazonSNSClient snsClient;

    @Value("${cloud.aws.end-point.uri}")
    private String endpoint;

    @GetMapping("/addSubscription/{email}")
    public String addSubscription(@PathVariable String email) {
        SubscribeRequest request = new SubscribeRequest(endpoint, "email", email);
        snsClient.subscribe(request);
        return "Subscription request is pending. To confirm the subscription, check your email : " + email;
    }

    @GetMapping("/sendNotification")
    public String publishMessageToTopic(){
        PublishRequest publishRequest=new PublishRequest(endpoint,"Hello from SNS, user","Notification: Network connectivity issue");
        snsClient.publish(publishRequest);
        return "Notification send successfully !!";
    }

    public static void main(String[] args) {
        SpringApplication.run(
                SpringCloudSQSApplication.class, args);
    }
}
