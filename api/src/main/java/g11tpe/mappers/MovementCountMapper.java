package g11tpe.mappers;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import g11tpe.MoveType;
import g11tpe.Movement;

public class MovementCountMapper implements Mapper<String, Movement, String, Integer> {
    private static final int ONE = 1;
    @Override
    public void map(String key, Movement value, Context<String, Integer> context) {
        if (value.getMovementType().equals(MoveType.TAKEOFF)) {
            context.emit(value.getOrigin(), ONE);
        } else {
            context.emit(value.getDestination(), ONE);
        }
    }
}
