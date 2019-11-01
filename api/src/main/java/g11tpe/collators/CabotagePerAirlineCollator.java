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

        Map<String, Double> map = new HashMap<>();
        AtomicLong acum = new AtomicLong();
        AtomicLong others = new AtomicLong();
        AtomicInteger i = new AtomicInteger();
        values.forEach( stringLongEntry -> acum.addAndGet(stringLongEntry.getValue()));
        values.forEach( stringLongEntry -> {
            map.put(stringLongEntry.getKey(), (((double) stringLongEntry.getValue() / (double) acum.get()) * 10000.0) / 100.0);
            if (i.get() > n) {
                others.addAndGet(stringLongEntry.getValue());
            }
        });

        Map<String, Double> result = map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(n).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        result.put("Otros", (((double) others.get() / (double) acum.get()) * 10000.0) / 100.0);
        return result;

    }
}
