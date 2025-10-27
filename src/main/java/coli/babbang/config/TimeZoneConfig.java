package coli.babbang.config;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("Server timezone set to: " + TimeZone.getDefault().getID());
    }
}
