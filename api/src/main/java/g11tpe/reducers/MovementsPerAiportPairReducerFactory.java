package g11tpe.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.LinkedList;
import java.util.List;

public class MovementsPerAiportPairReducerFactory implements ReducerFactory<Long, String, List<String>> {
    @Override
    public Reducer<String, List<String>> newReducer(Long key) {
        return new MovementsPerAirportPairReducer();
    }

    private class MovementsPerAirportPairReducer extends Reducer<String, List<String>> {
        private volatile List<String> airports;

        @Override
        public void beginReduce () {
            airports = new LinkedList<>();
        }
        @Override
        public void reduce(String value) {
            airports.add(value);
        }
        @Override
        public List<String> finalizeReduce() {
            return airports;
        }
    }
}
