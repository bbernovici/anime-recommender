package com.anime.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class Neo4jConnector {

    private static final Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "challpix" ) );

    public static Session getSession() {
        Session session = driver.session();
        return session;
    }

}
