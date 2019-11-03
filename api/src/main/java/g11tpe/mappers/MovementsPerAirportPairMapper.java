package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementsPerAirportPairMapper implements Mapper<String, Long, Long, String> {
    @Override
    public void map(String key, Long value, Context<Long, String> context) {
        if (value >= 1000)
            context.emit(value - value % 1000, key);
    }
}
