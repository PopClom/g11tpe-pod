package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CabotagePerAirlineCollator implements Collator<Map.Entry<String, Long>, Map<String, Double>> {

    @Override
    public Map<String, Double> collate( Iterable<Map.Entry<String, Long>> values ) {

        Map<String, Double> map = new HashMap<>();
        AtomicLong acum = new AtomicLong();
        values.forEach( stringLongEntry -> acum.addAndGet(stringLongEntry.getValue()));
        values.forEach( stringLongEntry -> {
            map.put(stringLongEntry.getKey(), (((double) stringLongEntry.getValue() / (double) acum.get()) * 10000.0) / 100.0);
        });

        map.entrySet().stream().sorted(Map.Entry.comparingByValue());

        return map;
    }
}
