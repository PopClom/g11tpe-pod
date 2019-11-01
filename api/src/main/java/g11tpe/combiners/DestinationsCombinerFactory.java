package g11tpe.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class DestinationsCombinerFactory implements CombinerFactory<String, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return new DestinationsCombiner();
    }

    private class DestinationsCombiner extends Combiner<Long,Long> {

        private long sum = 0;
        @Override
        public void combine( Long value ) {
            sum++;
        }
        @Override
        public Long finalizeChunk() {
            return sum;
        }
        @Override
        public void reset() {
            sum = 0;
        }

    }
}