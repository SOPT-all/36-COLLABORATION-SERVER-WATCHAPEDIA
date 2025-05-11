package or.sopt.soptwatcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SoptWatchaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoptWatchaApplication.class, args);
    }

}
