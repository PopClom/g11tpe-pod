package g11tpe.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import g11tpe.*;
import g11tpe.collators.CabotagePerAirlineCollator;
import g11tpe.collators.DestinationsCollator;
import g11tpe.combiners.CabotagePerAirlineCombinerFacctory;
import g11tpe.combiners.DestinationsCombinerFactory;
import g11tpe.enums.FlightClass;
import g11tpe.enums.FlightClassification;
import g11tpe.enums.MoveType;
import g11tpe.mappers.CabotagePerAirlineMapper;
import g11tpe.mappers.DestinationsMapper;
import g11tpe.mappers.MovementCountMapper;
import g11tpe.reducers.CabotagePerAirlineReducerFactory;
import g11tpe.reducers.DestinationsReducerFactory;
import g11tpe.reducers.MovementCountReducerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        final ClientConfig ccfg = new ClientConfig();
        final HazelcastInstance hz = HazelcastClient.newHazelcastClient(ccfg);
        logger.info("g11tpe Client Starting ...");
        query1(hz);
        int n = 3;
        CabotagePerAirline(hz, n);
        Destinations(hz, "EZEI", n);
    }

    private static void query1(HazelcastInstance hz) {
        JobTracker jobTracker = hz.getJobTracker("flight-count");
        final IList<Movement> list = hz.getList("movements");

        populate(list);

        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(list);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Integer>> future = job
                .mapper(new MovementCountMapper())
                .reducer(new MovementCountReducerFactory())
                .submit();

        try {
            Map<String, Integer> result = future.get();
            result.forEach((key, value) -> System.out.println("" + key + ": " + value));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void populate(IList<Movement> list) {
        try {
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Aerolineas Argentinas"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Flybondi"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Aerolineas Argentinas"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.TAKEOFF, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
            list.add(new Movement(FlightClassification.CABOTAGE, MoveType.LANDING, FlightClass.PRIVATE_FOREIGNER, "EZEI", "CORD", "Emirates"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CabotagePerAirline(HazelcastInstance hz, int n) {

        JobTracker jobTracker = hz.getJobTracker("airline-cabotage-count");
        final IList<Movement> list = hz.getList("movements");
        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(list);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Double>> future = job
                .mapper(new CabotagePerAirlineMapper())
                .combiner(new CabotagePerAirlineCombinerFacctory())
                .reducer(new CabotagePerAirlineReducerFactory())
                .submit(new CabotagePerAirlineCollator());

        try {
            Map<String, Double> result = future.get();
            result.forEach((key, value) -> System.out.println("" + key + ": " + value + "%"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void Destinations (HazelcastInstance hz, String origin, int n) {

        JobTracker jobTracker = hz.getJobTracker("destinations-count");
        final IList<Movement> list = hz.getList("movements");
        list.removeIf(element -> !element.getOrigin().equals(origin));
        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(list);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Long>> future = job
                .mapper(new DestinationsMapper())
                .combiner(new DestinationsCombinerFactory())
                .reducer(new DestinationsReducerFactory())
                .submit(new DestinationsCollator());

        try {
            Map<String, Long> result = future.get();
            result.forEach((key, value) -> {
                System.out.println("" + key + ": " + value);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}