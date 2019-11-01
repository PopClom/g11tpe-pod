package g11tpe.client.parsers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import g11tpe.*;
import g11tpe.client.exceptions.InvalidCSVMovementsFileException;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MovementsCSVParser {
    private static final int FLIGHT_CLASS_POS = 2;
    private static final int FLIGHT_CLASSIFICATION_POS = 3;
    private static final int MOVEMENT_TYPE_POS = 4;
    private static final int ORIGIN_POS = 5;
    private static final int DESTINATION_POS = 6;
    private static final int AIRILINE_POS = 7;

    public static List<Movement> parseFile(final String airportsFilePath) throws InvalidCSVMovementsFileException, IOException, IllegalArgumentException   {
        List<Movement> movements = new LinkedList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(airportsFilePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line = csvReader.readNext(); //skip headers line
            while ((line = csvReader.readNext()) != null) {

                try {
                    Movement movement = new Movement(
                            FlightClassification.getEnumByString(line[FLIGHT_CLASSIFICATION_POS]),
                            MoveType.getEnumByString(line[MOVEMENT_TYPE_POS]),
                            FlightClass.getEnumByString(line[FLIGHT_CLASS_POS]),
                            line[ORIGIN_POS],
                            line[DESTINATION_POS],
                            line[AIRILINE_POS]);

                    movements.add(movement);
                } catch (illegalMovementException e) {
                    throw new InvalidCSVMovementsFileException(e.getMessage());
                }
            }
            System.out.println(movements.get(0).toString());
            System.out.println(movements.get(1).toString());
            System.out.println(movements.get(2).toString());
            System.out.println(movements.size());
        }

        return null;
    }

}
