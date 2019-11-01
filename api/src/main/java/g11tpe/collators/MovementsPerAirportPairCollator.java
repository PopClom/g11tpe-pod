package g11tpe.collators;

import com.hazelcast.mapreduce.Collator;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;
import java.util.stream.Collectors;

public class MovementsPerAirportPairCollator implements Collator<Map.Entry<Integer, List<String>>, List<MutablePair<Integer, MutablePair<String, String>>>> {
    @Override
    public List<MutablePair<Integer, MutablePair<String, String>>> collate(Iterable<Map.Entry<Integer, List<String>>> values ) {
        List<MutablePair<Integer, MutablePair<String, String>>> list = new LinkedList<>();

        values.forEach( mapEntry -> {
            List<String> airports = mapEntry.getValue();
            int size = airports.size();
            for (int i = 0 ; i < size ; i++) {
                for (int j = i + 1 ; j < size ; j++) {
                    String airportI = airports.get(i);
                    String airportJ = airports.get(j);
                    if (airportI.compareTo(airportJ) < 0)
                        list.add(new MutablePair<>(mapEntry.getKey(), new MutablePair<>(airportI, airportJ)));
                    else
                        list.add(new MutablePair<>(mapEntry.getKey(), new MutablePair<>(airportJ, airportI)));
                }
            }
        });

        return list.stream().sorted((elem1, elem2) -> {
            int cmp = elem1.getLeft().compareTo(elem2.getLeft());
            if (cmp != 0) {
                return -cmp;
            } else if (elem1.getRight().getLeft().compareTo(elem2.getRight().getLeft()) != 0) {
                return elem1.getRight().getLeft().compareTo(elem2.getRight().getLeft());
            } else {
                return elem1.getRight().getRight().compareTo(elem2.getRight().getRight());
            }
        }).collect(Collectors.toList());
    }
}
