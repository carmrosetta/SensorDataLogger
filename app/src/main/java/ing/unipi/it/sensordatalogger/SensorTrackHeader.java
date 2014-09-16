package ing.unipi.it.sensordatalogger;

/**
 * Created by carmen on 11/09/14.
 */
public class SensorTrackHeader {
    String sensorType;
    String startDate;
    String startTime;
    String device;
    String androidVersion;
    float range;
    String unit;
    String androidSamplingFrequency;


    public SensorTrackHeader(String sensorType, String startDate, String startTime, String device, String androidVersion, float range, String unit, String samplingFrequency) {
        this.sensorType = sensorType;
        this.startDate = startDate;
        this.startTime = startTime;
        this.device = device;
        this.androidVersion = androidVersion;
        this.range = range;
        this.unit = unit;
        this.androidSamplingFrequency = samplingFrequency;
    }


    @Override
    public String toString() {
        return "% "+sensorType+" Track \n%"+
                "\n% Start date [YY-MM-DD]: "+startDate+
                "\n% Start time [hh-mm-ss]: "+startTime+"\n%"+
                "\n% Device: "+device+
                "\n% Android Version: "+androidVersion+
                "\n% Range ["+unit+"]: " + range +
                "\n% Android Sampling Rate: "+ androidSamplingFrequency +"\n%";

    }
}
