package g11tpe.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import g11tpe.enums.FlightClassification;
import g11tpe.enums.MoveType;
import g11tpe.Movement;

public class CabotagePerAirlineMapper implements Mapper<String, Movement, String, Long> {
    private static final long ONE = 1;

    @Override
    public void map(String key, Movement value, Context<String, Long> context) {
        /* mapping only landing movements to avoid repetitions */
        if (value.getMovementType().equals(MoveType.LANDING) && value.getFlightClassification().equals(FlightClassification.CABOTAGE)) {
            context.emit(value.getAirline(), ONE);
        }
    }
}
