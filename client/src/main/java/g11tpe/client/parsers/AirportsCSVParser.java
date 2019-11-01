package g11tpe.client.parsers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import g11tpe.client.exceptions.InvalidCSVAirportsFileException;


import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AirportsCSVParser {
    private static final int OACI_POS = 1;

    public static List<String> parseFile(final String airportsFilePath) throws InvalidCSVAirportsFileException, IOException, IllegalArgumentException  {
        List<String> airports = new LinkedList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(airportsFilePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line = csvReader.readNext(); //skip headers line
            while ((line = csvReader.readNext()) != null) {
                if(line[OACI_POS].compareTo("") != 0){
                    airports.add(line[OACI_POS]);
                }
            }
        }
        System.out.print(airports.size());
        System.out.println(airports);
        System.out.println(airportsFilePath);

        return airports;
    }
}
