package g11tpe;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class QueriesTest {

    private static final String Airport1 = "ABCD";
    private static final String Airport2 = "EFGH";
    private static final String Airport3 = "JKLM";
    private static final String Airport4 = "NOPQ";
    private static final String Airport5 = "RSTU";

    private static final int FLIGHTS_PER_AIRPORT = 5;

    private static final int MAX_NODES = 4;

    /* QUERY 1 */
    private static final long EXPECTED_MOVES_PER_AIRPORT = 0;

    /* QUERY 2 */
    private static final long EXPECTED_CABOTAGE_FLIGHTS = 0;

    /* QUERY 4 */
    private static final long EXPECTED_FLIGHTS_ABCD = 0;
    private static final long EXPECTED_FLIGHTS_EFGH = 0;
    private static final long EXPECTED_FLIGHTS_JKLM = 0;
    private static final long EXPECTED_FLIGHTS_NOPQ = 0;

    public QueriesTest() {

        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);

    }

}
