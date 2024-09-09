package CSCI262A3;

public class BaselineStatistics {
    private String eventName;
    private String eventType;
    private double mean;
    private double sDev;
    private int min;
    private int max;
    private int weight;

    public BaselineStatistics(){
        this.eventName = "";
        this.mean = 0.0;
        this.sDev = 0.0;
        this.min = 0;
        this.max = 0;
        this.weight = 0;
    }

    public BaselineStatistics(String eventName, int min, int weight){
        this.eventName = eventName;
        this.eventType = "Discrete";
        this.min = min;
        this.weight = weight;
    }
    public BaselineStatistics(String eventName, int min, int max, int weight){
        this.eventName = eventName;
        this.eventType = "Continuous";
        this.min = min;
        this.max = max;
        this.weight = weight;
    }

    public BaselineStatistics(String eventName, double mean, double sDev){
        this.eventName = eventName;
        this.mean = mean;
        this.sDev = sDev;
    }



    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getMean() {
        return mean;
    }
    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getsDev() {
        return sDev;
    }
    public void setsDev(double sDev) {
        this.sDev = sDev;
    }

    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString(){
        return String.format("%-15s %-8s %-8.2f %-8.2f %-8d %-8d %d", getEventName(),(getEventType().equals("Discrete"))? "D":"C", getMean(), getsDev(), getMin(), getMax(), getWeight());

    }
}