package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementsPerAirportPairMapper implements Mapper<String, Long, Long, String> {
    @Override
    public void map(String key, Long value, Context<Long, String> context) {
        context.emit(value / 1000, key);
    }
}
