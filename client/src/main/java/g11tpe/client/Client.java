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
import g11tpe.FlightClass;
import g11tpe.FlightClassification;
import g11tpe.Movement;
import g11tpe.client.exceptions.InvalidCSVAirportsFileException;
import g11tpe.client.exceptions.InvalidCSVMovementsFileException;
import g11tpe.client.exceptions.InvalidProgramParametersException;
import g11tpe.client.parsers.AirportsCSVParser;
import g11tpe.client.parsers.MovementsCSVParser;
import g11tpe.util.CollectionNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static Parameters parameters;
    private static HazelcastInstance hzClient;
    private final static String HZ_NAME = "grupo-11";
    private final static String HZ_PASSWORD = "chau";
    private final static String AIRPORTS_INFILE_NAME = "aeropuertos.csv";
    private final static String MOVEMENTS_INFILE_NAME = "movimientos.csv";

    private static IMap<String, Map<String, String>> airportsIMap ;
    private static IList<Movement> movementsIList;

    public static void main(String[] args) {
        logger.info("g11tpe Client Starting ...");
        parameters = new Parameters();
        try {
            parameters.validate();
        } catch (InvalidProgramParametersException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        hzClient = getHazelCastClient();
        initializeHzCollections();
        parseInFiles();


        switch (parameters.getQueryN()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }

    private static void initializeHzCollections() {
        airportsIMap = hzClient.getMap(CollectionNames.AIRPORTS_MAP.getName());
        movementsIList = hzClient.getList(CollectionNames.MOVEMENTS_LIST.getName());
    }


    private static HazelcastInstance getHazelCastClient() {
        ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName(HZ_NAME).setPassword(HZ_PASSWORD);

        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        for (String address : parameters.getAddresses()) {
            networkConfig.addAddress(address);
        }

        return HazelcastClient.newHazelcastClient(config);
    }

    private static void parseInFiles() {
        try {
            AirportsCSVParser.parseFile(parameters.getInPath() + '/' + AIRPORTS_INFILE_NAME, airportsIMap);
            MovementsCSVParser.parseFile(parameters.getInPath() + '/' + MOVEMENTS_INFILE_NAME, movementsIList);
        } catch (InvalidCSVAirportsFileException | InvalidCSVMovementsFileException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
