package g11tpe.client;

import g11tpe.client.exceptions.InvalidProgramParametersException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Parameters {
    private static final int MIN_N = 1;
    private static final int MAX_N = 4;
    //Parámetros presentes en todas las queries
    private List<String> addresses;
    private String inPath;
    private String outPath;

    //TODO cambiar esto cuando haga los scripts
    private int queryN;

    //parametros adicionales
    private int n;
    private String oaci;

    public void validate() throws InvalidProgramParametersException {
        Properties properties = System.getProperties();
        boolean valid, queryNOK;

        valid = validateAddress(properties);
        valid &= validateInPath(properties);
        valid &= validateOutPath(properties);
        queryNOK = validateQueryN(properties);
        valid &= queryNOK;

        if (queryNOK) {
            switch (queryN) {
                case 2:
                    valid &= validateN(properties);
                    break;
                case 4:
                    valid &= validateN(properties);
                    valid &= validateOaci(properties);
                    break;
            }
        }

        if (!valid) {
            printParametersHelp ();
            throw new InvalidProgramParametersException("Invalid program parameters.");
        }
    }

    private boolean validateOaci(Properties properties) {
        if (!properties.containsKey("oaci")) {
            System.out.println("Oaci parameter missing.");
            return false;
        }
        oaci = properties.getProperty("oaci");
        return true;
    }

    private boolean validateN(Properties properties) {
        if (!properties.containsKey("n")) {
            System.out.println("n parameter missing.");
            return false;
        }  else {
            try {
                n = Integer.parseInt(properties.getProperty("n"));
            } catch (Exception e) {
                System.out.println("Invalid n.");
                return false;
            }
            if (n < 0){
                System.out.println("Invalid n.");
                return false;
            }
        }
        return true;
    }

    private boolean validateQueryN(Properties properties) {
        if (!properties.containsKey("queryN")) {
            System.out.println("Number of query parameter missing.");
            return false;
        }
        try {
            queryN = Integer.parseInt(properties.getProperty("queryN"));
        } catch (Exception e) {
            System.out.println("Invalid query number.");
            return false;
        }
        if (queryN < MIN_N || queryN > MAX_N){
            System.out.println("Invalid query number.");
            return false;
        }
        return true;
    }

    private boolean validateOutPath(Properties properties) {
        if (!properties.containsKey("outPath")) {
            System.out.println("Out path parameter missing.");
            return false;
        }
        outPath = properties.getProperty("outPath");
        return true;
    }

    private boolean validateInPath(Properties properties) {
        if (!properties.containsKey("inPath")) {
            System.out.println("In path parameter missing.");
            return false;
        }
        inPath = properties.getProperty("inPath");
        return true;
    }

    private boolean validateAddress(Properties properties) {
        if(!properties.containsKey("addresses")) {
            System.out.println("Server address parameter missing.");
            return false;
        }
        addresses =  Arrays.asList(properties.getProperty("addresses").split(";"));
        return true;
    }

    private void printParametersHelp () {
        //TODO escribir el help
        System.out.println("HELP:");
    }

    public int getQueryN() {
        return queryN;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public String getInPath() {
        return inPath;
    }

    public String getOutPath() {
        return outPath;
    }

}
