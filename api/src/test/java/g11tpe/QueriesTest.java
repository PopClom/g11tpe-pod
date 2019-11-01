package g11tpe;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import g11tpe.enums.FlightClass;
import g11tpe.enums.FlightClassification;
import g11tpe.enums.MoveType;
import g11tpe.exceptions.illegalMovementException;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class QueriesTest {

    private static final Config config = new Config();
    private static final HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    private static final QueryExecutor qe = new QueryExecutor(hz);

    private static final String Airport1 = "ABCD";
    private static final String Airport2 = "EFGH";
    private static final String Airport3 = "IJKL";
    private static final String Airport4 = "MNOP";
    private static final String Airport5 = "QRST";
    private static final String OTROS = "Otros";
    private static final String Airline1 = "AirlineA";
    private static final String Airline2 = "AirlineB";
    private static final String Airline3 = "AirlineC";

    private static final int MAX_NODES = 4;

    private static final double LAMBDA = 0.01;

    /* QUERY 1 */
    private static final long EXPECTED_MOVES_AIRPORT_1 = 11;
    private static final long EXPECTED_MOVES_AIRPORT_2 = 7;
    private static final long EXPECTED_MOVES_AIRPORT_3 = 4;
    private static final long EXPECTED_MOVES_AIRPORT_4 = 10;
    private static final long EXPECTED_MOVES_AIRPORT_5 = 14;

    /* QUERY 2 */
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_1 = 5;
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_2 = 2;
    private static final long EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_3 = 1;
    private static final long EXPECTED_CABOTAGE_FLIGHTS = 8;
    private static final int MAX_AIRLINES = 3;

    /* QUERY 4 ; origin = MNOP */
    private static final long EXPECTED_FLIGHTS_1 = 4;
    private static final long EXPECTED_FLIGHTS_2 = 0;
    private static final long EXPECTED_FLIGHTS_3 = 1;
    private static final long EXPECTED_FLIGHTS_5 = 5;
    private static final int MAX_AIRPORTS = 5;

    @BeforeClass
    public static void setUpClass() throws illegalMovementException {

        final IList<Movement> listOfMovements = hz.getList("movements");

        /* QUERY 1 */
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport3, Airline1));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport3, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.PRIVATE_NATIONAL, Airport4, Airport1, Airline2));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.PRIVATE_NATIONAL, Airport4, Airport1, Airline2));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, Airport1, Airport3, Airline2));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, Airport1, Airport3, Airline2));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport5, Airport3, Airline3));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport5, Airport3, Airline3));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, Airport5, Airport2, Airline3));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, Airport5, Airport2, Airline3));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.TAKEOFF, FlightClass.REGULAR, Airport2, Airport5, Airline3));
        listOfMovements.add(new Movement(FlightClassification.INTERNATIONAL, MoveType.LANDING, FlightClass.REGULAR, Airport2, Airport5, Airline3));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.NON_REGULAR, Airport2, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.NON_REGULAR, Airport2, Airport1, Airline1));

        /* QUERY 2 */
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport1, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport2, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport2, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport5, Airport2, Airline1));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport5, Airport2, Airline1));

        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport5, Airport2, Airline2));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport5, Airport2, Airline2));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport1, Airport2, Airline2));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport1, Airport2, Airline2));

        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.REGULAR, Airport3, Airport5, Airline3));
        listOfMovements.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.REGULAR, Airport3, Airport5, Airline3));

        /* QUERY 4 */
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport1, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.TAKEOFF, FlightClass.REGULAR, Airport4, Airport5, Airline1));
        listOfMovements.add(new Movement(FlightClassification.N_A, MoveType.LANDING, FlightClass.REGULAR, Airport4, Airport5, Airline1));
    }

    /* QUERY 1 */
    @Test
    public final void movesPerAirport() {

        Optional<Map<String, MutablePair<String, Long>>> result = qe.movementsPerAirport(hz);
        if (!result.isPresent()) {
            Assert.fail();
        }
        result.get().forEach((key, value) -> {
            switch (key) {
                case Airport1:
                    Assert.assertEquals(EXPECTED_MOVES_AIRPORT_1, (long) value.right);
                    break;
                case Airport2:
                    Assert.assertEquals(EXPECTED_MOVES_AIRPORT_2, (long) value.right);
                    break;
                case Airport3:
                    Assert.assertEquals(EXPECTED_MOVES_AIRPORT_3, (long) value.right);
                    break;
                case Airport4:
                    Assert.assertEquals(EXPECTED_MOVES_AIRPORT_4, (long) value.right);
                    break;
                case Airport5:
                    Assert.assertEquals(EXPECTED_MOVES_AIRPORT_5, (long) value.right);
                    break;
                default:
                    Assert.fail();
            }
        });
    }

    /* QUERY 2 */
    @Test
    public final void cabotageFlightsPerAirline() {

        for (int i = 0; i < MAX_AIRLINES; i++) {
            Optional<Map<String, Double>> result = qe.cabotagePerAirline(i);
            if (!result.isPresent()) {
                Assert.fail();
            }
            result.get().forEach( (key, value) -> {
                switch (key) {
                    case Airline1:
                        Assert.assertEquals(((double) EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_1 / (double) EXPECTED_CABOTAGE_FLIGHTS) * 10000.0 / 100.0, value, LAMBDA);
                        break;
                    case Airline2:
                        Assert.assertEquals(((double) EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_2 / (double) EXPECTED_CABOTAGE_FLIGHTS) * 10000.0 / 100.0, value, LAMBDA);
                        break;
                    case Airline3:
                        Assert.assertEquals(((double) EXPECTED_CABOTAGE_FLIGHTS_AIRLINE_3 / (double) EXPECTED_CABOTAGE_FLIGHTS) * 10000.0 / 100.0, value, LAMBDA);
                        break;
                    default:
                        if (!key.equals(OTROS)) {
                            Assert.fail();
                        }
                        break;
                }
            });
        }

    }

    /* QUERY 3 */
    @Test
    public final void query3() {

        /* query executor 3 */
    }

    /* QUERY 4 */
    @Test
    public final void Destinations() {

        Optional<Map<String, Long>> result = qe.destinations(Airport4, MAX_AIRPORTS);
        if (!result.isPresent()) {
            Assert.fail();
        }
        result.get().forEach((key, value) -> {
            switch (key) {
                case Airport1:
                    Assert.assertEquals(EXPECTED_FLIGHTS_1, (long) value);
                    break;
                case Airport2:
                    Assert.assertEquals(EXPECTED_FLIGHTS_2, (long) value);
                    break;
                case Airport3:
                    Assert.assertEquals(EXPECTED_FLIGHTS_3, (long) value);
                    break;
                case Airport5:
                    Assert.assertEquals(EXPECTED_FLIGHTS_5, (long) value);
                    break;
                default:
                    Assert.fail();
            }
        });
    }

}

