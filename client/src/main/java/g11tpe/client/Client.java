package g11tpe.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import g11tpe.Movement;
import g11tpe.client.exceptions.InvalidProgramParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static Parameters parameters;

    public static void main(String[] args) {
        logger.info("g11tpe Client Starting ...");
        parameters = new Parameters();
        try {
            parameters.validate();
        } catch (InvalidProgramParametersException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("grupo-11").setPassword("chau");

        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        for (String address : parameters.getAddresses()) {
            networkConfig.addAddress(address);
        }

        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        //probando que funcione
        IMap<Long, String> map = hzClient.getMap("data");
        System.out.println(map.values());

        IList<Movement> movementsIList;







    }
}
