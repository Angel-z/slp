package com.kanofans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SlpApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(SlpApplication.class, args);
        System.out.println("slp启动成功\n");
    }
}
