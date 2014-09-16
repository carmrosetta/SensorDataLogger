package ing.unipi.it.sensordatalogger;

/**
 * Created by carmen on 11/09/14.
 */
public class Attribute  {
    String name, unit, type;

    public Attribute(String name, String unit, String type) {
        this.name = name;
        this.unit = unit;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return "@ATTRIBUTE \t"+this.name+" ["+this.unit+"] "+this.type+"\n";
    }
}