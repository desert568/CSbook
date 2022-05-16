package com.kinghis.emri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.kinghis.emri.dao"})
@SpringBootApplication
public class WtxEmriApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtxEmriApplication.class, args);
    }

}
