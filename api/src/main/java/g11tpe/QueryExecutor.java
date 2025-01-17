package g11tpe;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import g11tpe.collators.CabotagePerAirlineCollator;
import g11tpe.collators.DestinationsCollator;
import g11tpe.collators.MovementCountCollator;
import g11tpe.collators.MovementsPerAirportPairCollator;
import g11tpe.combiners.CabotagePerAirlineCombinerFacctory;
import g11tpe.combiners.DestinationsCombinerFactory;
import g11tpe.mappers.CabotagePerAirlineMapper;
import g11tpe.mappers.DestinationsMapper;
import g11tpe.mappers.MovementCountMapper;
import g11tpe.mappers.MovementsPerAirportPairMapper;
import g11tpe.reducers.*;
import g11tpe.util.CollectionNames;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryExecutor {
    private HazelcastInstance hz;

    public QueryExecutor(HazelcastInstance hz) {
        this.hz = hz;
    }


    public Optional<Map<String, MutablePair<String, Long>>> movementsPerAirport(HazelcastInstance hz) {
        JobTracker jobTracker = hz.getJobTracker("movement-count");
        final IList<Movement> movements = hz.getList(CollectionNames.MOVEMENTS_LIST.getName());

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

    public Optional<List<MutablePair<Long, MutablePair<String, String>>>> movementsPerAirportPair() {
        JobTracker jobTracker = hz.getJobTracker("movement-pair-count");
        final IList<Movement> movements = hz.getList(CollectionNames.MOVEMENTS_LIST.getName());

        final KeyValueSource<String, Movement> source1 = KeyValueSource.fromList(movements);

        Job<String, Movement> job1 = jobTracker.newJob(source1);
        ICompletableFuture<Map<String, Long>> future1 = job1
                .mapper(new MovementCountMapper())
                .reducer(new MovementSimpleCountReducerFactory())
                .submit();

        final IMap<String, Long> movementsAux = hz.getMap(CollectionNames.MOVEMENTS_AUX_LIST.getName());
        try {
            Map<String, Long> result1 = future1.get();
            movementsAux.putAll(result1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        final KeyValueSource<String, Long> source2 = KeyValueSource.fromMap(movementsAux);

        Job<String, Long> job2 = jobTracker.newJob(source2);
        ICompletableFuture<List<MutablePair<Long, MutablePair<String, String>>>> future2 = job2
                .mapper(new MovementsPerAirportPairMapper())
                .reducer(new MovementsPerAiportPairReducerFactory())
                .submit(new MovementsPerAirportPairCollator());

        try {
            return Optional.of(future2.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Map<String, Double>> cabotagePerAirline(int n) {
        JobTracker jobTracker = hz.getJobTracker("airline-cabotage-count");
        final IList<Movement> list = hz.getList(CollectionNames.MOVEMENTS_LIST.getName());
        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(list);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Double>> future = job
                .mapper(new CabotagePerAirlineMapper())
                .combiner(new CabotagePerAirlineCombinerFacctory())
                .reducer(new CabotagePerAirlineReducerFactory())
                .submit(new CabotagePerAirlineCollator(n));

        try {
            Map<String, Double> result = future.get();
            return Optional.of(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Map<String, Long>> destinations (String origin, int n) {
        JobTracker jobTracker = hz.getJobTracker("destinations-count");
        final IList<Movement> list = hz.getList(CollectionNames.MOVEMENTS_LIST.getName());
        final KeyValueSource<String, Movement> source = KeyValueSource.fromList(list);

        Job<String, Movement> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Long>> future = job
                .mapper(new DestinationsMapper(origin))
                .combiner(new DestinationsCombinerFactory())
                .reducer(new DestinationsReducerFactory())
                .submit(new DestinationsCollator(n));

        try {
            return Optional.of(future.get());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
