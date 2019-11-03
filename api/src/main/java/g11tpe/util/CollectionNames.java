package g11tpe.util;

public enum CollectionNames {
    MOVEMENTS_LIST ( "movements-g11"),
    MOVEMENTS_AUX_LIST ( "movements-aux-g11"),
    AIRPORTS_MAP ( "airports-g11"),
    ;

    final String name;

    CollectionNames(String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }
}
