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

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static Parameters parameters;
    private static List<String> airportsList;
    private static List<Movement> movementsList;
    private final static String AIRPORTS_INFILE_NAME = "aeropuertos.csv";
    private final static String MOVEMENTS_INFILE_NAME = "movimientos.csv";

    public static void main(String[] args) {
        logger.info("g11tpe Client Starting ...");
        parameters = new Parameters();
        try {
            parameters.validate();
        } catch (InvalidProgramParametersException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        HazelcastInstance hzClient = getHazelCastClient();
//
//        //probando que funcione
//        IMap<Long, String> map = hzClient.getMap("data");
//        System.out.println(map.values());

        parseInFiles();

    //    IMap<Integer, Movement> movementsIList = hzClient.getMap(CollectionNames.MOVEMENTS_LIST.getName());


    }

    private static HazelcastInstance getHazelCastClient() {
        ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("grupo-11").setPassword("chau");

        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        for (String address : parameters.getAddresses()) {
            networkConfig.addAddress(address);
        }

        return HazelcastClient.newHazelcastClient(config);
    }

    private static void parseInFiles() {
        try {
            airportsList = AirportsCSVParser.parseFile(parameters.getInPath() + '/' + AIRPORTS_INFILE_NAME);
            movementsList = MovementsCSVParser.parseFile(parameters.getInPath() + '/' + MOVEMENTS_INFILE_NAME);
        } catch (InvalidCSVAirportsFileException | InvalidCSVMovementsFileException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
