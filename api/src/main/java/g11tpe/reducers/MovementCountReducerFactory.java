package g11tpe.reducers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
import org.apache.commons.lang3.tuple.MutablePair;

public class MovementCountReducerFactory implements ReducerFactory<String, Long, MutablePair<String, Long>>, HazelcastInstanceAware {
    private transient HazelcastInstance hz;

    @Override
    public void setHazelcastInstance(HazelcastInstance hz) {
        this.hz = hz;
    }

    @Override
    public Reducer<Long, MutablePair<String, Long>> newReducer(String key) {
        return new MovementCountReducer(key);
    }

    private class MovementCountReducer extends Reducer<Long, MutablePair<String, Long>> {
        private volatile long sum;
        private volatile String key;

        private MovementCountReducer(String key) {
            this.key = key;
        }

        @Override
        public void beginReduce () {
            sum = 0;
        }
        @Override
        public void reduce(Long value) {
            sum += value;
        }
        @Override
        public MutablePair<String, Long> finalizeReduce() {
            final IMap<String, String> airports = hz.getMap("airports");
            return new MutablePair<>(airports.get(this.key), sum);
        }
    }
}