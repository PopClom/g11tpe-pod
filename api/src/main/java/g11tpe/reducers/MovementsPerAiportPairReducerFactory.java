package g11tpe.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashSet;
import java.util.Set;

public class MovementsPerAiportPairReducerFactory implements ReducerFactory<Integer, String, Set<String>> {
    @Override
    public Reducer<String, Set<String>> newReducer(Integer key) {
        return new MovementsPerAirportPairReducer();
    }

    private class MovementsPerAirportPairReducer extends Reducer<String, Set<String>> {
        private volatile Set<String> airports;

        @Override
        public void beginReduce () {
            airports = new HashSet<>();
        }
        @Override
        public void reduce(String value) {
            airports.add(value);
        }
        @Override
        public Set<String> finalizeReduce() {
            return airports;
        }
    }
}
