package org.natlex.geo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 10:11 PM
 */
@EnableAsync
@SpringBootApplication
public class GeoClassifyServiceRunner {
    public static void main(String[] args) {
        SpringApplication.run(GeoClassifyServiceRunner.class , args);
    }
}