package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementsPerAirportPairMapper implements Mapper<String, Integer, Integer, String> {
    @Override
    public void map(String key, Integer value, Context<Integer, String> context) {
        context.emit(value / 1000, key);
    }
}
