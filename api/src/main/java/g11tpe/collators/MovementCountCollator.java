package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MovementCountCollator implements Collator<Map.Entry<String, MutablePair<String, Long>>, Map<String, MutablePair<String, Long>>> {
    @Override
    public Map<String, MutablePair<String, Long>> collate(Iterable<Map.Entry<String, MutablePair<String, Long>>> values ) {

        Map<String, MutablePair<String, Long>> map = new HashMap<>();
        values.forEach( mapEntry -> {
            map.put(mapEntry.getKey(), mapEntry.getValue());
        });

        return map.entrySet().stream().sorted((mapEntry1, mapEntry2) -> {
            if (mapEntry1.getValue().getRight() > mapEntry2.getValue().getRight()) {
                return -1;
            } else if (mapEntry1.getValue().getRight() < mapEntry2.getValue().getRight()) {
                return 1;
            } else {
                return mapEntry1.getKey().compareTo(mapEntry2.getKey());
            }
        }).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
