package kv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan(basePackages = "kv.mapper")
@EnableScheduling
public class KvApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Australia/Melbourne"));
        SpringApplication.run(KvApplication.class, args);
    }

}
