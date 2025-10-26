package coli.babbang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PingController {

    @Value("${github.token}")
    private String masterToken;

    @Value("${webhook.url}")
    private String webhookUrl;


    @GetMapping("/")
    public String pong() {
        log.info(masterToken + "github Token");
        log.info(webhookUrl + "webhook");
        return "pong";
    }
}
