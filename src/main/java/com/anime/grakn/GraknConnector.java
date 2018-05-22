package com.anime.grakn;

import ai.grakn.*;
import ai.grakn.factory.EmbeddedGraknSession;
import ai.grakn.remote.RemoteGrakn;
import ai.grakn.util.SimpleURI;

/**
 * Created by bbernovici on 21.05.2018.
 */
public class GraknConnector {

    private static GraknSession openSession() {
        return RemoteGrakn.session(new SimpleURI("localhost", 48555), Keyspace.of("anime"));
    }

    public static GraknTx getSessionToRead() {
        return openSession().open(GraknTxType.READ);

}
    public static GraknTx getSessionToWrite() {
        return openSession().open(GraknTxType.WRITE);
    }
}
