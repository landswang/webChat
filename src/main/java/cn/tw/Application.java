package cn.tw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by torreswangzh@gmail.com on 2017/4/20.
 */
@SpringBootApplication
@EnableWebSocket
@MapperScan(basePackages = "cn.tw.mongo.dao")
public class Application {

    public static void main(String[] args) {
        // SpringApplication.run(Application.class, args);
        SpringApplication.run(Application.class, args);
    }
}
