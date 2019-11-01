package g11tpe.enums;

public enum FlightClassification {

    CABOTAGE ("Cabotaje"),
    INTERNATIONAL ("Internacional"),
    N_A ("N/A");

    final String descr;

    FlightClassification (String descr) {
        this.descr = descr;
    }

    public String getDescription () {
        return descr;
    }

    public static FlightClassification getEnumByString(String code){
        for(FlightClassification e : FlightClassification.values()){
            if(code.equalsIgnoreCase(e.descr)) return e;
        }
        return null;
    }
}
