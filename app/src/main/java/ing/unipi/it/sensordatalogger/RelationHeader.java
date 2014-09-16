package ing.unipi.it.sensordatalogger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by carmen on 11/09/14.
 */
public class RelationHeader {String name;
    List<Attribute> attributes;

    public RelationHeader(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes  = new LinkedList<Attribute>();
        this.attributes = attributes;
    }

    public String toString() {
        String attributesList ="";

        for(int i=0; i < attributes.size(); i++) {
            attributesList += attributes.get(i).toString();

        }
        return "@RELATION \t"+name +"\n"+attributesList+"@DATA\n";
    }
}