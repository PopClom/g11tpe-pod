package g11tpe;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Movement implements DataSerializable {

    private FlightClassification classification;
    private MoveType movementType;
    private FlightClass flightClass;
    private String origin;
    private String destination;
    private String airline;

    public Movement (FlightClassification classification,
                     MoveType movementType,
                     FlightClass flightClass,
                     String origin,
                     String destination,
                     String airline) throws illegalMovementException {

        if (origin == null || destination == null) {
            throw new illegalMovementException("ICAO of origin airport and ICAO destination airport cannot be null");
        }
        this.airline = airline;
        this.classification = classification;
        this.destination = destination;
        this.origin = origin;
        this.flightClass = flightClass;
        this.movementType = movementType;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(origin);
        out.writeUTF(destination);
        out.writeUTF(airline);
        out.writeInt(classification.ordinal());
        out.writeInt(movementType.ordinal());
        out.writeInt(flightClass.ordinal());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.origin = in.readUTF();
        this.destination = in.readUTF();
        this.airline = in.readUTF();
        this.classification = FlightClassification.values()[in.readInt()];
        this.movementType = MoveType.values()[in.readInt()];
        this.flightClass = FlightClass.values()[in.readInt()];
    }

    @Override
    public String toString() {
        return "Movement{" +
                "classification=" + classification +
                ", movementType=" + movementType +
                ", flightClass=" + flightClass +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", airline='" + airline + '\'' +
                '}';
    }
}
