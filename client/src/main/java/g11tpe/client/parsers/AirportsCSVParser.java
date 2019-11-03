package g11tpe.client.parsers;

import com.hazelcast.core.IMap;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import g11tpe.client.exceptions.InvalidCSVAirportsFileException;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirportsCSVParser {
    private static final int OACI_POS = 1;
    private static final int NAME_POS = 4;

    public static void parseFile(final String airportsFilePath, final  IMap<String, String> airportsImap) throws InvalidCSVAirportsFileException, IOException, IllegalArgumentException  {
        Map<String, String> airportsMap = new HashMap<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(airportsFilePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line = csvReader.readNext(); //salteo la linea de headers
          //Salteo los aeropuertos que no tienen oaci
            while ((line = csvReader.readNext()) != null) {
                if(line[OACI_POS].compareTo("") != 0){
                    airportsMap.put(line[OACI_POS], line[NAME_POS]);
                }
            }
        }
        airportsImap.putAll(airportsMap);

    }
}
