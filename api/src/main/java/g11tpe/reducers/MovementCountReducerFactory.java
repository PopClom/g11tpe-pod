package g11tpe.reducers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
import org.apache.commons.lang3.tuple.MutablePair;

public class MovementCountReducerFactory implements ReducerFactory<String, Integer, MutablePair<String, Integer>>, HazelcastInstanceAware {
    private transient HazelcastInstance hz;

    @Override
    public Reducer<Integer, MutablePair<String, Integer>> newReducer(String key) {
        return new Reducer<Integer, MutablePair<String, Integer>>() {
            private volatile int sum;
            @Override
            public void beginReduce () {
                sum = 0;
            }
            @Override
            public void reduce(Integer value) {
                sum += value;
            }
            @Override
            public MutablePair<String, Integer> finalizeReduce() {
                final IMap<String, String> airports = hz.getMap("airports");
                return new MutablePair<>(airports.get(key), sum);
            }
        };
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hz) {
        this.hz = hz;
    }

}