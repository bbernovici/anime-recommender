package com.anime;

/**
 * Created by bbernovici on 21.05.2018.
 */


import com.anime.grakn.Migrator;
import com.anime.grakn.SchemaCreator;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
        SchemaCreator.createSchema();
        SchemaCreator.defineRules();
        Migrator.migrateAnimeCSV();
    }
}
