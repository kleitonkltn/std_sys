package krtechs.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class STDApplication {
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

    public static void main(String[] args) {
        SpringApplication.run(STDApplication.class, args);
    }

}
