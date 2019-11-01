package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import g11tpe.enums.MoveType;
import g11tpe.Movement;

public class DestinationsMapper implements Mapper<String, Movement, String, Long> {

    private static final long ONE = 1;

    @Override
    public void map(String s, Movement movement, Context<String, Long> context) {
        if (movement.getMovementType().equals(MoveType.LANDING)) {
            context.emit(movement.getDestination(), ONE);
        }
    }
}
