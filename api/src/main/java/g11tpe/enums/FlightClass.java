package g11tpe.enums;

public enum FlightClass {

    NON_REGULAR ("No Regular"),
    REGULAR ("Regular"),
    PRIVATE_FOREIGNER ("Vuelo Privado con Matrícula Nacional"),
    PRIVATE_NATIONAL ("Vuelo Privado con Matrícula Extranjera");

    final String descr;

    FlightClass (String descr) {
        this.descr = descr;
    }

    public String getDescription () {
        return descr;
    }

    public static FlightClass getEnumByString(String code){
        for(FlightClass e : FlightClass.values()){
            if(code.equalsIgnoreCase(e.descr)) return e;
        }
        return null;
    }
}
