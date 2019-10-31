package g11tpe.client;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import g11tpe.client.exceptions.InvalidProgramParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static Parameters parameters;

    public static void main(String[] args) {
//        Config config = new Config();
//        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        logger.info("g11tpe Client Starting ...");
        parameters = new Parameters();
        try {
            parameters.validate();
        } catch (InvalidProgramParametersException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

    }
}
