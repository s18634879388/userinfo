package com.realmadrid;

import org.json.JSONException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * 程序的入口
 *
 * @author shenjizhe
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages={"com.realmadrid"})
public class Application implements CommandLineRunner {

    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws JSONException, NoSuchAlgorithmException {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("\n用户中心启动！");
    }
}
