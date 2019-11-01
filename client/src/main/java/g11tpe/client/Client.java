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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        qe.movementsPerAirport(hz);
        int n = 3;
        qe.cabotagePerAirline(n);
        qe.destinations("EZEI", n);
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