package g11tpe.mappers;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import g11tpe.enums.MoveType;
import g11tpe.Movement;

public class MovementCountMapper implements Mapper<String, Movement, String, Long> {
    private static final long ONE = 1;

    @Override
    public void map(String key, Movement value, Context<String, Long> context) {
        if (value.getMovementType().equals(MoveType.TAKEOFF)) {
            context.emit(value.getOrigin(), ONE);
        } else {
            context.emit(value.getDestination(), ONE);
        }
    }
}
