package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DestinationsCollator implements Collator<Map.Entry<String, Long>, Map<String, Long>> {

    @Override
    public Map<String, Long> collate( Iterable<Map.Entry<String, Long>> values ) {

        Map<String, Long> map = new HashMap<>();
        values.forEach( stringLongEntry -> {
            map.put(stringLongEntry.getKey(), stringLongEntry.getValue());
        });

/*
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue());
*/

    }
}
