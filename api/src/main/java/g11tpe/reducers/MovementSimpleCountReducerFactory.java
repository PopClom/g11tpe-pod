package g11tpe.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementSimpleCountReducerFactory implements ReducerFactory<String, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(String key) {
        return new MovementCountReducer();
    }

    private class MovementCountReducer extends Reducer<Integer, Integer> {
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
        public Integer finalizeReduce() {
            return sum;
        }
    }
}