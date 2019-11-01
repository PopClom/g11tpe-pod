package g11tpe;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Airport implements DataSerializable {
    private String oaci;
    private String name;

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(oaci);
        objectDataOutput.writeUTF(name);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        oaci = objectDataInput.readUTF();
        name = objectDataInput.readUTF();
    }

    public String getOaci() {
        return oaci;
    }

    public void setOaci(String oaci) {
        this.oaci = oaci;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
