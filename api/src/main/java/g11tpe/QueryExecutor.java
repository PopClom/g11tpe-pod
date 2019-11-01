package g11tpe;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import g11tpe.collators.CabotagePerAirlineCollator;
import g11tpe.collators.DestinationsCollator;
import g11tpe.collators.MovementCountCollator;
import g11tpe.combiners.CabotagePerAirlineCombinerFacctory;
import g11tpe.combiners.DestinationsCombinerFactory;
import g11tpe.mappers.CabotagePerAirlineMapper;
import g11tpe.mappers.DestinationsMapper;
import g11tpe.mappers.MovementCountMapper;
import g11tpe.reducers.CabotagePerAirlineReducerFactory;
import g11tpe.reducers.DestinationsReducerFactory;
import g11tpe.reducers.MovementCountReducerFactory;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class QueryExecutor {
    private HazelcastInstance hz;

    public QueryExecutor(HazelcastInstance hz) {
        this.hz = hz;
    }

    public Optional<Map<String, MutablePair<String, Long>>> movementsPerAirport(HazelcastInstance hz) {
        JobTracker jobTracker = hz.getJobTracker("flight-count");
        final IList<Movement> movements = hz.getList("movements");

        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, MutablePair<String, Long>>> future = job
                .mapper(new MovementCountMapper())
                .reducer(new MovementCountReducerFactory())
                .submit(new MovementCountCollator());

        try {
            return Optional.of(future.get());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Map<String, Double>> cabotagePerAirline(int n) {
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
            Map<String, Double> limitedResult = new HashMap<>();
            AtomicReference<Double> acum = new AtomicReference<>(0.0);
            AtomicInteger i = new AtomicInteger();

            result.forEach((key, value) -> {
                if (i.get() <= n) {
                    limitedResult.put(key, value);
                } else {
                    acum.updateAndGet(v -> v + value);
                }
                i.getAndIncrement();
            });

            if (i.get() > n) {
                limitedResult.put("Otros", acum.get());
            }

            return Optional.of(limitedResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Map<String, Long>> destinations (String origin, int n) {
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
            Map<String, Long> limitedResult = new HashMap<>();
            AtomicLong acum = new AtomicLong(0);
            AtomicInteger i = new AtomicInteger();

            result.forEach((key, value) -> {
                if (i.get() <= n) {
                    limitedResult.put(key, value);
                } else {
                    acum.updateAndGet(v -> v + value);
                }
                i.getAndIncrement();
            });

            return Optional.of(limitedResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
