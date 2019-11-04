package g11tpe.enums;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public enum FlightClass implements DataSerializable {

    NON_REGULAR ("No Regular"),
    REGULAR ("Regular"),
    PRIVATE_FOREIGNER ("Vuelo Privado con Matrícula Nacional"),
    PRIVATE_NATIONAL ("Vuelo Privado con Matrícula Extranjera");

    final String descr;

    FlightClass (String descr) {
        this.descr = descr;
    }

    public String getDescription () {
        return descr;
    }

    public static FlightClass getEnumByString(String code){
        for(FlightClass e : FlightClass.values()){
            if(code.equalsIgnoreCase(e.descr)) return e;
        }
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {

    }
}
