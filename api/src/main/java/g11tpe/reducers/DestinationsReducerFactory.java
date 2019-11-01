package g11tpe.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class DestinationsReducerFactory implements ReducerFactory<String, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(String s) {
        return new DestinationsReducer();
    }

    private class DestinationsReducer extends Reducer<Long, Long> {

        private volatile long sum;

        @Override
        public void beginReduce () {
            sum = 0;
        }
        @Override
        public void reduce(Long value) {
            sum += value;
        }
        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
