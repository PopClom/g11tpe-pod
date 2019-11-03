package g11tpe.client.writers;

import com.opencsv.CSVWriter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;



public class QueryResultsToCsv {
    private static Logger logger = LoggerFactory.getLogger(QueryResultsToCsv.class);
    private String outPath;

    public QueryResultsToCsv (String outPath) {
        this.outPath = outPath;
    }

    public void query1(Map<String, MutablePair<String, Long>> movesPerAirport) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outPath + "/query1.csv"), ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            String[] headers = {"OACI","Denominación","Movimientos"};
            writer.writeNext(headers);

            movesPerAirport.forEach((key, value) -> {
                String[] outputline = new String[3];
                outputline[0] = key;
                outputline[1] = value.getLeft();
                outputline[2] = String.valueOf(value.getRight());
                writer.writeNext(outputline);
            });

        } catch (Exception e) {
            logger.error("Query results couldn't be written to file");
            System.out.println(e.getMessage());
        }
    }

    public void query2(Map<String, Double> cabotagePerAirline) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outPath + "/query2.csv"), ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            String[] headers = {"Aerolínea","Porcentaje"};
            writer.writeNext(headers);

            cabotagePerAirline.forEach((key, value) -> {
                String[] outputline = new String[2];
                outputline[0] = key;
                outputline[1] = Double.valueOf(new DecimalFormat("#.##").format(value)) + "%";
                writer.writeNext(outputline);
            });

        } catch (Exception e) {
            logger.error("Query results couldn't be written to file");
            System.out.println(e.getMessage());
        }
    }

    public void query3(List<MutablePair<Long, MutablePair<String, String>>> movementsPerAirportPair) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outPath + "/query3.csv"), ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            String[] headers = {"Grupo","Aeropuerto A","Aeropuerto B"};
            writer.writeNext(headers);

            movementsPerAirportPair.forEach((elem) -> {
                String[] outputline = new String[3];
                outputline[0] = String.valueOf(elem.getLeft());
                outputline[1] = elem.getRight().getLeft();
                outputline[2] = elem.getRight().getRight();
                writer.writeNext(outputline);
            });

        } catch (Exception e) {
            logger.error("Query results couldn't be written to file");
            System.out.println(e.getMessage());
        }
    }


    public void query4(Map<String, Long> destinations) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outPath + "/query4.csv"), ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            String[] headers = {"OACI","Despegues"};
            writer.writeNext(headers);

            destinations.forEach((key, value) -> {
                String[] outputline = new String[3];
                outputline[0] = key;
                outputline[1] = String.valueOf(value);
                writer.writeNext(outputline);
            });

        } catch (Exception e) {
            logger.error("Query results couldn't be written to file");
            System.out.println(e.getMessage());
        }
    }
}
