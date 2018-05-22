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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


}
