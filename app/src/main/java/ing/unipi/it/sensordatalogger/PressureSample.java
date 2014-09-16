package ing.unipi.it.sensordatalogger;

import java.io.Serializable;

/**
 * Created by carmen on 14/09/14.
 */
public class PressureSample implements Serializable {

    private float pressure;
    private String timestamp;

    public PressureSample(float pressure, String timestamp) {
        this.pressure = pressure;
        this.timestamp = timestamp;
    }

    public String toString() {

        return timestamp+", "+pressure+"\n";
    }
}