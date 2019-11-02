package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import g11tpe.Movement;
import g11tpe.enums.MoveType;

public class DestinationsMapper implements Mapper<String, Movement, String, Long> {

    private static final long ONE = 1;
    private volatile String origin;

    public DestinationsMapper(String origin) {
        this.origin = origin;
    }

    @Override
    public void map(String s, Movement movement, Context<String, Long> context) {
        if (movement.getMovementType().equals(MoveType.TAKEOFF) && movement.getOrigin().equals(this.origin)) {
            context.emit(movement.getDestination(), ONE);
        }
    }
}
