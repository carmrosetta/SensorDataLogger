package ing.unipi.it.sensordatalogger;

import java.io.Serializable;

/**
 * Created by carmen on 14/09/14.
 */

public class AccelerationSample implements Serializable {
    private float componentX, componentY, componentZ;
    private String timestamp;

    public AccelerationSample(float componentX, float componentY, float componentZ, String timestamp) {
        this.componentX = componentX;
        this.componentY = componentY;
        this.componentZ = componentZ;
        this.timestamp = timestamp;

    }

    public float getComponentX() {
        return componentX;
    }

    public float getComponentY() {
        return componentY;
    }

    public float getComponentZ() {
        return componentZ;
    }

    public String toString() {
        // 0.0000,-0.897,0.027,0.041
        return timestamp+", "+componentX+", "+componentY+", "+componentZ+"\n";
    }

}