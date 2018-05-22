package com.anime.rest;

import ai.grakn.concept.Concept;
import ai.grakn.graql.GetQuery;
import ai.grakn.graql.QueryBuilder;
import ai.grakn.graql.admin.Answer;
import com.anime.grakn.GraknConnector;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by bbernovici on 22.05.2018.
 */
import java.util.Map;

import static ai.grakn.graql.Graql.var;


@RestController
public class RestResource {

    @RequestMapping(value="/user/create/{name}",
        method = RequestMethod.POST)
    public ResponseEntity<?> createUserInNeo4j(@PathVariable String name) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //localhost:8080/anime/2273
    @RequestMapping(value="/anime/{id}",
        method = RequestMethod.GET)
    public ResponseEntity<?> getAnimeTitleByIdFromGrakn(@PathVariable String id) {
        QueryBuilder qb = GraknConnector.getSessionToRead().graql();
        GetQuery query = qb.match(var("x").isa("anime").has("anime_id", id).has("title", var("x_name"))).get();
        for(Answer ans : query) {
            System.out.println(ans.get("x_name").asAttribute().getValue());
            System.out.println(ans.get("x").getId().getValue());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //localhost:8080/animes/similar-to/2273
    @RequestMapping(value="/animes/similar-to/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getSimilarAnimesByIdFromGrakn(@PathVariable String id) {
        QueryBuilder qb = GraknConnector.getSessionToWrite().graql();
        GetQuery query = qb.match(var("x").isa("anime").has("anime_id", id), var().rel("x").rel("y").isa("similar_anime"), var("y").has("title", var("y_title"))).limit(2).get();
        System.out.println(query.toString());
        System.out.println(query.vars());

        for(Answer ans : query) {
            System.out.println(ans.get("y_title").asAttribute().getValue());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //localhost:8080/animes/similar-to?title=mobile suit gundam unicorn
    @RequestMapping(value="/animes/similar-to",
            method = RequestMethod.GET)
    public ResponseEntity<?> getSimilarAnimesByTitleFromGrakn(@RequestParam String title) {
        QueryBuilder qb = GraknConnector.getSessionToWrite().graql();
        GetQuery query = qb.match(var("x").isa("anime").has("title", title), var().rel("x").rel("y").isa("similar_anime"), var("y").has("title", var("y_title"))).limit(5).get();
        System.out.println(query.toString());
        System.out.println(query.vars());

        for(Answer ans : query) {
            System.out.println(ans.get("y_title").asAttribute().getValue());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
