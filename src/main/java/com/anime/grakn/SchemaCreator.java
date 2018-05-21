package com.anime.grakn;

import ai.grakn.GraknTx;
import ai.grakn.concept.AttributeType;
import ai.grakn.concept.EntityType;
import ai.grakn.concept.Role;
import ai.grakn.concept.Rule;
import ai.grakn.graql.Pattern;
import ai.grakn.graql.QueryBuilder;

import static ai.grakn.graql.Graql.and;
import static ai.grakn.graql.Graql.label;
import static ai.grakn.graql.Graql.var;

/**
 * Created by bbernovici on 21.05.2018.
 */
public class SchemaCreator {

    public static void createSchema() {
        QueryBuilder qb = Connector.getSessionToWrite().graql();

        qb.define(
                label("anime")
                        .sub("entity")
                        .has("anime_id")
                        .has("title")
                        .has("rating")
                        .has("type")
                        .has("episodes")
                        .has("members")
                        .plays("watched_anime")
                        .plays("rated_anime")
                        .plays("unrated_anime")
                        .plays("tagged")
                        .plays("similar_to")
                        .plays("dissimilar_to"),

                label("genre")
                        .sub("entity")
                        .plays("tagger")
                        .has("name"),

                label("user")
                        .sub("entity")
                        .plays("anime_rater")
                        .plays("anime_watcher")
                        .has("user_id")
                        .has("user_name")
                        .has("first_name")
                        .has("last_name"),

                label("tagging")
                        .sub("relationship")
                        .relates("tagged").relates("tagger"),

                label("anime_watched")
                        .sub("relationship")
                        .relates("watched_anime").relates("anime_watcher"),

                label("anime_rated")
                        .sub("relationship")
                        .relates("rated_anime").relates("anime_rater")
                        .has("user_rating"),

                label("anime_not_rated")
                        .sub("relationship")
                        .relates("unrated_anime").relates("anime_watcher"),

                label("similar_anime")
                        .sub("relationship")
                        .relates("tagged").relates("watched_anime").relates("rated_anime").relates("similar_to"),

                label("dissimilar_anime")
                        .sub("relationship")
                        .relates("tagged").relates("watched_anime").relates("unrated_anime").relates("dissimilar_to"),

                label("similar_to")
                        .sub("role"),
                label("dissimilar_to")
                        .sub("role"),

                label("identifier")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.STRING),
                label("anime_id")
                        .sub("identifier"),
                label("user_id")
                        .sub("identifier"),
                label("name")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.STRING),
                label("user_name")
                        .sub("name"),
                label("first_name")
                        .sub("name"),
                label("last_name")
                        .sub("name"),
                label("title")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.STRING),
                label("type")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.STRING),
                label("episodes")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.LONG),
                label("members")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.LONG),
                label("user_rating")
                        .sub("rating"),
                label("rating")
                        .sub("attribute")
                        .datatype(AttributeType.DataType.DOUBLE)).execute();

    }

    public static void defineRules() {

//        Pattern animeIsSimilarRuleWhen1 = var().rel("tagged", "anime_a").rel("tagger", "genre").isa("tagging");
//        Pattern animeIsSimilarRuleWhen2 = var().rel("tagged", "anime_b").rel("tagger", "genre").isa("tagging");
//        Pattern animeIsSimilarRuleThen = var().rel("tagged", "anime_a")
        Pattern rule1when = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_a, tagger: $genre) isa tagging;(tagged: $anime_b, tagger: $genre) isa tagging;"));
//        Pattern rule1when2 = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_b, tagger: $genre) isa tagging;"));
        Pattern rule1then = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_a, similar_to: $anime_b) isa similar_anime;"));

        Pattern rule2when = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_c, tagger: $genre_a) isa tagging;(tagged: $anime_d, tagger: $genre_b) isa tagging;$genre_a != $genre_b;"));
//        Pattern rule2when2 = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_d, tagger: $genre_b) isa tagging;"));
//        Pattern rule2when3 = and(Connector.getSessionToWrite().graql().parser().parsePatterns("$genre_a != $genre_b;"));
        Pattern rule2then = and(Connector.getSessionToWrite().graql().parser().parsePatterns("(tagged: $anime_c, dissimilar_to: $anime_d) isa disimilar_anime;"));

        Rule rule1 = Connector.getSessionToWrite().putRule("anime_is_similar", rule1when, rule1then);
        Rule rule2 = Connector.getSessionToWrite().putRule("anime_is_not_similar", rule2when, rule2then);

    }
}
