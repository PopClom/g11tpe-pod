package g11tpe.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import g11tpe.FlightClass;
import g11tpe.FlightClassification;
import g11tpe.MoveType;
import g11tpe.Movement;
import g11tpe.mappers.MovementCountMapper;
import g11tpe.reducers.MovementCountReducerFactory;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        final ClientConfig ccfg = new ClientConfig();
        final HazelcastInstance hz = HazelcastClient.newHazelcastClient(ccfg);
        logger.info("g11tpe Client Starting ...");
        query1(hz);
    }

    private static void query1(HazelcastInstance hz) {
        JobTracker jobTracker = hz.getJobTracker("flight-count");
        final IList<Movement> movements = hz.getList("movements");
        final IMap<String, String> airports = hz.getMap("airports");
        airports.put("EZEI", "EZEIZA");
        airports.put("CORD", "CORDOBA");

        populate(movements);

        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, MutablePair<String, Integer>>> future = job
                .mapper(new MovementCountMapper())
                .reducer(new MovementCountReducerFactory())
                .submit();

        try {
            Map<String, MutablePair<String, Integer>> result = future.get();
            result.forEach((key, value) -> System.out.println("" + key + ": " + value));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void populate(IList<Movement> list) {
        try {
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Aerolineas Argentinas"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}