package CSCI262A3;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FileIO {
    private static Scanner input;
    private static Formatter output;
    private String filename;

    public FileIO(String filename){
        this.filename = filename;
    }

    public void setFilename(String filename) {this.filename = filename;}

    public String getFilename(){return this.filename;}

    public void openFileInput(){
        try {
            input = new Scanner(Paths.get(getFilename()));
        }catch (IOException io) {
            System.out.println("Error while opening the file");
            // Exit the program
            System.exit(1);
        }
    }
    public void openFileOutput(){
        try{
            // Open file
            output = new Formatter(getFilename());
        }catch(SecurityException se){
            System.out.println("Write permission is denied");
            // Exit the program
            System.exit(1);
        }catch (FileNotFoundException fe){
            System.out.println("Error in opening the file");
            // Exit the program
            System.exit(1);
        }


    }




    public void readEventFile(List<BaselineStatistics> baselineStatistics) {
        try{

            int l = input.nextInt();
            input.nextLine();


            for(int i = 0; i < l; i++){

                String[] split = input.nextLine().split(":");
                if (split[1].equals("D")){
                    // Discrete Event
                    baselineStatistics.add(new BaselineStatistics(split[0], Integer.parseInt(split[2]), Integer.parseInt(split[4])));
                }else if(split[1].equals("C")){
                    // Continuous Event
                    baselineStatistics.add(new BaselineStatistics(split[0], Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
                }

            }




        }catch (NoSuchElementException ns){
            System.out.println( getFilename() + " was not created");
            // Exit the program
            System.exit(1);
        }catch (IllegalStateException ise){
            System.out.println("Error while reading the file");
            // Exit the program
            System.exit(1);
        }

    }

    public void readStatsFile(List<BaselineStatistics> baselineStatistics) {
        try{

            int l = input.nextInt();
            input.nextLine();

            for(int i = 0; i < l; i++){
                String[] split = input.nextLine().split(":");
                if(split[0].equals(baselineStatistics.get(i).getEventName())){
                    baselineStatistics.get(i).setMean(Double.parseDouble(split[1]));
                    baselineStatistics.get(i).setsDev(Double.parseDouble(split[2]));
                }
            }


        }catch (NoSuchElementException ns){
            System.out.println( getFilename() + " was not created");
            // Exit the program
            System.exit(1);
        }catch (IllegalStateException ise){
            System.out.println("Error while reading the file");
            // Exit the program
            System.exit(1);
        }

    }









    public void writeBaselineStat(List<BaselineStatistics> baselineStats) {

        try {
            output.format(String.format("%-15s %-8s %-8s %-8s %-8s %s%n", "Event", "Mean", "StDev", "Min", "Max", "Weight"));
            output.format("---------------------------------------------------------------%n");
            for (BaselineStatistics b : baselineStats){
                output.format(b.toString());
                output.format("\n");
                output.flush();
            }
            System.out.println("==> Done writing to file: " + getFilename());
        }catch (FormatterClosedException fc){
            System.out.println("Error in writing to file");
            // Exit the program
            System.exit(1);
        }


    }

    public void writeBaselineData(List<DayStatistic> baselineDataTraining){
        try {
            for(DayStatistic s : baselineDataTraining){
                if (s instanceof DiscreteStatistics){
                    output.format(s.toString());
                }

                if (s instanceof ContinuousStatistics){
                    output.format(s.toString());
                }
                output.format("\n");
                output.flush();
            }


            System.out.println("==> Done writing to file: " + getFilename());

        }catch (FormatterClosedException fc){
            System.out.println("Error in writing to file");
            // Exit the program
            System.exit(1);
        }



    }

    public void writeDailyTotal(double[] dailyCounter){

        try {

            for (int i = 0; i < dailyCounter.length; i++){
                output.format(String.format("Day %d: %.2f%n", i+1, dailyCounter[i]));
                output.flush();
            }

            output.format("\n");


            System.out.println("==> Done writing to file: " + getFilename());

        }catch (FormatterClosedException fc){
            System.out.println("Error in writing to file");
            // Exit the program
            System.exit(1);
        }


    }

    public void writeDailyIndividual(double[][] dailyIndividual, List<DayStatistic> dataStatistics, int day){
        try {
            // for each day
            for (int i = 0; i < day ; i ++){
                output.format(String.format("Day %d%n", (i+1)) );

                for (int j = 0; j < dataStatistics.size(); j++){
                    output.format(String.format("%s: %.2f%n", dataStatistics.get(j).getEventName(), dailyIndividual[j][i]));
                }

                output.format("\n");
                output.flush();

            }



            System.out.println("==> Done writing to file: " + getFilename());

        }catch (FormatterClosedException fc){
            System.out.println("Error in writing to file");
            // Exit the program
            System.exit(1);
        }



    }

    public void writeDailyCounter(int threshold, double[] dailyTotal){
        try {

            for (int i = 0; i < dailyTotal.length; i++){
                output.format(String.format("Daily Counter Day %d: %.2f ", (i+1), dailyTotal[i] ));
                if (dailyTotal[i] >= threshold){
                    output.format("(Anomaly Found !!!)%n");
                }else {
                    output.format("(Not Anomaly)%n");
                }
                output.flush();
            }


            System.out.println("==> Done writing to file: " + getFilename());

        }catch (FormatterClosedException fc){
            System.out.println("Error in writing to file");
            // Exit the program
            System.exit(1);
        }
    }






    public void closeFile(){
        if (input != null){
            // Close file
            input.close();
        }

        if (output != null){
            output.close();
        }
    }





}
