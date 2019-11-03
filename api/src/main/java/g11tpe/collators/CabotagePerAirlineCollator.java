package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CabotagePerAirlineCollator implements Collator<Map.Entry<String, Long>, Map<String, Double>> {

    private volatile int n;

    public CabotagePerAirlineCollator(int n) {
        this.n=n;
    }

    @Override
    public Map<String, Double> collate( Iterable<Map.Entry<String, Long>> values ) {

        Map<String, Long> map = new HashMap<>();
        AtomicLong acum = new AtomicLong();
        AtomicLong others = new AtomicLong();
        values.forEach( stringLongEntry -> acum.addAndGet(stringLongEntry.getValue()));
        values.forEach( stringLongEntry -> {
                if (!stringLongEntry.getKey().equals("Otros"))
                    map.put(stringLongEntry.getKey(), stringLongEntry.getValue());
        });

        Map<String, Long> aux = map.entrySet().stream().sorted((mapEntry1, mapEntry2) -> {
            int cmp = mapEntry1.getValue().compareTo(mapEntry2.getValue());
            if (cmp != 0)
                return -cmp;
            else
                return mapEntry1.getKey().compareTo(mapEntry2.getKey());
        }).limit(n).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<String, Double> result = new LinkedHashMap<>();
        aux.forEach((key, value) -> {
            result.put(key, (((double) value / (double) acum.get()) * 10000.0) / 100);
            others.addAndGet(value);
        });

        result.put("Otros", 100.0 - (((double) others.get() / (double) acum.get()) * 10000.0) / 100.0);

        return result;
    }
}
