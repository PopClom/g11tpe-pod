package g11tpe.enums;

public enum MoveType {

    TAKEOFF ("Despegue"),
    LANDING ("Aterrizaje");

    final String descr;

    MoveType (String descr) {
        this.descr = descr;
    }

    public String getDescription () {
        return descr;
    }

    public static MoveType getEnumByString(String code){
        for(MoveType e : MoveType.values()){
            if(code.equalsIgnoreCase(e.descr)) return e;
        }
        return null;
    }
}
