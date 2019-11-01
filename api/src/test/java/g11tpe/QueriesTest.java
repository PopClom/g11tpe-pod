package g11tpe;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueriesTest {

    private static final String Airport1 = "ABCD";
    private static final String Airport2 = "EFGH";
    private static final String Airport3 = "IJKL";
    private static final String Airport4 = "MNOP";
    private static final String Airport5 = "QRST";

    private static final int FLIGHTS_PER_AIRPORT = 5;

    private static final int MAX_NODES = 4;

    /* QUERY 1 */
    private static final long EXPECTED_MOVES_AIRPORT_1 = 11;
    private static final long EXPECTED_MOVES_AIRPORT_2 = 7;
    private static final long EXPECTED_MOVES_AIRPORT_3 = 4;
    private static final long EXPECTED_MOVES_AIRPORT_4 = 10;
    private static final long EXPECTED_MOVES_AIRPORT_5 = 14;

    /* QUERY 2 ; max n = 3*/
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_A = 5;
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_B = 2;
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_C = 1;

    /* QUERY 4 ; origin = MNOP */
    private static final long EXPECTED_FLIGHTS_ABCD = 4;
    private static final long EXPECTED_FLIGHTS_EFGH = 0;
    private static final long EXPECTED_FLIGHTS_IJKL = 1;
    private static final long EXPECTED_FLIGHTS_QRST = 5;

    @Before
    public final void before() throws illegalMovementException {

        Config config = new Config();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        List<Movement> listOfMovements = new ArrayList<>();

        /* QUERY 1 */
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "IJKL", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "IJKL", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.PRIVATE_NATIONAL, "MNOP", "ABCD", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.PRIVATE_NATIONAL, "MNOP", "ABCD", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, "ABCD", "IJKL", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, "ABCD", "IJKL", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "QRST", "IJKL", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "QRST", "IJKL", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "QRST", "EFGH", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "QRST", "EFGH", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, "EFGH", "QRST", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, "EFGH", "QRST", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.NON_REGULAR, "EFGH", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.NON_REGULAR, "EFGH", "ABCD", "AirlineA"));

        /* QUERY 2 */
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "ABCD", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "EFGH", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "EFGH", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "QRST", "EFGH", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "QRST", "EFGH", "AirlineA"));

        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "QRST", "EFGH", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "QRST", "EFGH", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "ABCD", "EFGH", "AirlineB"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "ABCD", "EFGH", "AirlineB"));

        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, "IJKL", "QRST", "AirlineC"));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, "IJKL", "QRST", "AirlineC"));

        /* QUERY 4 */
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "ABCD", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, "MNOP", "QRST", "AirlineA"));
    }

    /* QUERY 1 */
    @Test
    public final void movesPerAirport() {

        /* query executor 1 */
    }

    /* QUERY 2 */
    @Test
    public final void cabotageFlightsPerAirline() {

        /* query executor 2 */
    }

    /* QUERY 3 */
    @Test
    public final void query3() {

        /* query executor 3 */
    }

    /* QUERY 4 */
    @Test
    public final void Destinations() {

        /* query executor 4 */
    }

}

