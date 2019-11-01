package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DestinationsCollator implements Collator<Map.Entry<String, Long>, Map<String, Long>> {
    private volatile int n;

    public DestinationsCollator(int n) {
        this.n = n;
    }

    @Override
    public Map<String, Long> collate( Iterable<Map.Entry<String, Long>> values ) {

        Map<String, Long> map = new HashMap<>();
        values.forEach( stringLongEntry -> {
            map.put(stringLongEntry.getKey(), stringLongEntry.getValue());
        });

        return map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(n).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}