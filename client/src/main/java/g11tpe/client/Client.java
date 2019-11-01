package g11tpe.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import g11tpe.*;
import g11tpe.enums.FlightClass;
import g11tpe.enums.FlightClassification;
import g11tpe.enums.MoveType;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        final ClientConfig ccfg = new ClientConfig();
        final HazelcastInstance hz = HazelcastClient.newHazelcastClient(ccfg);
        logger.info("g11tpe Client Starting ...");

        final IList<Movement> movements = hz.getList("movements");
        final IMap<String, String> airports = hz.getMap("airports");

        populate(movements, airports);

        QueryExecutor qe = new QueryExecutor(hz);

        /* QUERY 1 */
        Optional<Map<String, MutablePair<String, Long>>> movesPerAirport = qe.movementsPerAirport(hz);
        if (!movesPerAirport.isPresent()) {
            /* tirar un error */
        }
        else {
            movesPerAirport.get().forEach((key, value) -> System.out.println("" + key + ";" + value));
        }

        int n = 3;

        /* QUERY 2 */
        Optional<Map<String, Double>> cabotagePerAirline = qe.cabotagePerAirline(n);
        if (!cabotagePerAirline.isPresent()) {
            /* tirar un error */
        } else {
            cabotagePerAirline.get().forEach( (key, value) -> System.out.println("" + key + ";" + value + "%"));
        }

        /* QUERY 4 */
        Optional<Map<String, Long>> destinations = qe.destinations("EZEI", n);
        if (!destinations.isPresent()) {
            /* tirar un error */
        } else {
            destinations.get().forEach( (key, value) -> System.out.println("" + key + ";" + value));
        }

    }

    private static void populate(IList<Movement> list, IMap<String, String> map) {
        try {
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Aerolineas Argentinas"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Aerolineas Argentinas"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            map.put("EZEI", "EZEIZA");
            map.put("CORD", "CORDOBA");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}