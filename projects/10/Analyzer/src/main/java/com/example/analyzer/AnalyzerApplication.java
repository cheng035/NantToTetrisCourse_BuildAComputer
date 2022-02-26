package com.example.analyzer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalyzerApplication implements CommandLineRunner {

    @Resource
    FileService fileService;

    @Resource
    Tokenizer tokenizer;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AnalyzerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            var commands = fileService.readFile();

            commands.forEach(
                (c,v)->{
                    System.out.println("start");
                    tokenizer.init(v);
                    tokenizer.getTokenList().forEach(
                        System.out::println
                    );
                }
            );



        }
    }

}
