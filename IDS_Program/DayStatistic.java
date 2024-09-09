package CSCI262A3;

import java.text.DecimalFormat;

public abstract class DayStatistic {
    protected String eventName;
    protected double mean;
    protected double sDev;
    protected int days;

    public DayStatistic(){
        this.eventName = "";
        this.mean = 0.0;
        this.sDev = 0.0;
        this.days = 0;
    }

    public DayStatistic(String eventName, int days){
        this.eventName = eventName;
        this.days = days;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public abstract void calculateMean();
    public abstract void calculateSD();

    @Override
    public abstract String toString();
}

class DiscreteStatistics extends DayStatistic{
    private int[] data;

    public DiscreteStatistics(){
        super();
        this.data = new int[0];
    }

    public DiscreteStatistics(String eventName, int days){
        super(eventName, days);
        this.data = new int[super.getDays()];
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public void calculateMean(){
        int sum = 0;
        for (int i = 0; i < super.getDays(); i++){
            sum += data[i];
        }
        double mean = (double)sum/super.getDays();

        DecimalFormat df = new DecimalFormat("#.##");

        super.setMean(Double.parseDouble( df.format(mean) ));
    }

    public void calculateSD(){

        double sumSq = 0;
        for (int i = 0; i < super.getDays(); i ++) {
            double diff = data[i] - mean;
            sumSq += diff * diff;
        }

        double SD = Math.sqrt(sumSq / getDays());
        DecimalFormat df = new DecimalFormat("#.##");

        super.setsDev(Double.parseDouble( df.format(SD) ));

    }

    @Override
    public String toString(){
        String s = super.getEventName() + "\n";
        if(super.getMean() != 0){
            s += String.format("Mean: %.2f%n", super.getMean());
        }
        if (super.getsDev() != 0){
            s += String.format("SD: %.2f%n", super.getsDev());
        }
        for (int i = 0; i < data.length ; i++){
            s += String.format("%-10s | %d%n", "Day "+(i+1), data[i]);
        }

        return s;
    }
}

class ContinuousStatistics extends DayStatistic{
    private double[] data;

    public ContinuousStatistics(){
        super();
        this.data = new double[0];
    }

    public ContinuousStatistics(String eventName, int days){
        super(eventName, days);
        this.data = new double[super.getDays()];
    }


    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public void calculateMean(){
        double sum = 0;
        for (int i = 0; i < super.getDays(); i++){
            sum += data[i];
        }

        double mean = sum/super.getDays();

        DecimalFormat df = new DecimalFormat("#.##");

        super.setMean(Double.parseDouble( df.format(mean) ));
    }

    public void calculateSD(){

        double sumSq = 0;
        for (int i = 0; i < super.getDays(); i ++) {
            double diff = data[i] - mean;
            sumSq += diff * diff;
        }

        double SD = Math.sqrt(sumSq / getDays());
        DecimalFormat df = new DecimalFormat("#.##");

        super.setsDev(Double.parseDouble( df.format(SD) ));

    }

    @Override
    public String toString(){
        String s = super.getEventName() + "\n";
        if(super.getMean() != 0){
            s += String.format("Mean: %.2f%n", super.getMean());
        }
        if (super.getsDev() != 0){
            s += String.format("SD: %.2f%n", super.getsDev());
        }

        for (int i = 0; i < data.length ; i++){
            s += String.format("%-10s | %.2f%n", "Day "+(i+1), data[i]);
        }

        return s;
    }

}
