package CSCI262A3;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class IDS {
    private static Scanner input;
    public static void main(String[] args) {
        // To store baseline mean and standard deviation each event
        List<BaselineStatistics> baseline = new ArrayList<>();
        List<BaselineStatistics> stats;
        List<DayStatistic> dayStatistics;

        input = new Scanner(System.in);
        String eventTXT = args[0];
        String statTXT = args[1];
        int days = Integer.parseInt(args[2]);
        String runType = "Training";
        boolean isRunning = true;
        boolean isFirst = true;

        while (isRunning){
            stats = new ArrayList<>();
            dayStatistics = new ArrayList<>();

            // Part A - Initial Input
            System.out.println("======================== Initial Input ========================");
            initialInput(stats, eventTXT, statTXT);

            // Display Baseline
            displayBaselineStatistics(stats);
            FileIO fileio = new FileIO(String.format("%s%s", runType, statTXT));
            fileio.openFileOutput();
            fileio.writeBaselineStat(stats);
            fileio.closeFile();



            // Part B - Activity Simulation Engine And Logs
            System.out.println("\n\n============= Activity Simulation Engine And Logs =============");
            // Input number of days
            int[] dStat = new int[days];
            int[] cStat = new int[days];
            // Generate random numbers for Discrete and Continuous statistics
            generateStatistics(days, dStat, cStat);


            // Normalize data
            ActivitySimEngineAndLogs(days, dayStatistics, stats, dStat, cStat);

            // Display Baseline Stat Training
            displayBaselineData(dayStatistics, runType);
            System.out.println();
            System.out.println("==> Event generation completed ... ");

            fileio = new FileIO(String.format("%sLog%s", runType, statTXT));
            fileio.openFileOutput();
            fileio.writeBaselineData(dayStatistics);
            fileio.closeFile();


            // Part C - Analysis Engine
            System.out.println("==> Now begin analyzing ...");
            System.out.println("\n\n============= Analysis Engine =============");

            if (isFirst){
                // Calculate Mean and SD
                CalculateMeanSD(dayStatistics, baseline);

                System.out.println("==> Calculating baseline statistics completed ... ");
            }



            // Calculate daily total
            double[] dailyTotal = new double[days];
            double[][] dailyIndividual = new double[dayStatistics.size()][days];
            // Calculate the daily total of each day
            // and individual data of each event on each day
            //AnalysisEngine(dayStatistics, stats, days, dailyTotal, dailyIndividual);
            AnalysisEngine(dayStatistics, baseline, stats, days, dailyTotal, dailyIndividual);
            displayDailyTotal(dailyTotal);
            displayDailyIndividual(dailyIndividual, dayStatistics, days);

            // Save Daily
            fileio = new FileIO(String.format("%sDailyTotal%s",runType, statTXT));
            fileio.openFileOutput();
            fileio.writeDailyTotal(dailyTotal);
            fileio.closeFile();
            fileio = new FileIO(String.format("%sDailyIndividual%s",runType, statTXT));
            fileio.openFileOutput();
            fileio.writeDailyIndividual(dailyIndividual, dayStatistics, days);
            fileio.closeFile();




            // Part D - Alert Engine
            System.out.println("\n\n============= Alert Engine =============");
            // Calculate threshold
            int threshold = getThreshold(stats);
            double[] dailyCounter = new double[days];
            AlertEngine(stats, days, threshold, dailyIndividual, dayStatistics, dailyCounter);
            fileio = new FileIO(String.format("%sAnomalyCounter%s", runType, statTXT));
            fileio.openFileOutput();
            fileio.writeDailyCounter(threshold, dailyCounter);
            fileio.closeFile();

            System.out.println("\n\n");


            if (!isFirst){
                System.out.print("Would you like to enter another live data file (y/n)? ");

                String opt = input.next();
                if (opt.equalsIgnoreCase("y") || opt.equalsIgnoreCase("yes")){
                    // Input new live data
                    System.out.print("Enter filename for live statistics: ");
                    statTXT = input.next();


                    // Consume the newline character
                    input.nextLine();

                    // Enter number of days
                    System.out.print("Enter number of days for live data: ");
                    days = input.nextInt();
                    runType = "Live";
                }else {
                    isRunning = false;
                    System.out.println("~~ Thank you ~~");
                }

            }else{
                // Input new live data
                System.out.print("Enter filename for live statistics: ");
                statTXT = input.nextLine();
                // Enter number of days
                System.out.print("Enter number of days for live data: ");
                days = input.nextInt();
                runType = "Live";

                isFirst = false;
            }



        }






    }

    private static void initialInput(List<BaselineStatistics> baseline, String eventTXT, String statsTXT){
        // Read event file
        FileIO fileIO = new FileIO(eventTXT);
        fileIO.openFileInput();
        fileIO.readEventFile(baseline);
        fileIO.closeFile();

        // Read stats file
        fileIO = new FileIO(statsTXT);
        fileIO.openFileInput();
        fileIO.readStatsFile(baseline);
        fileIO.closeFile();
    }

    private static void ActivitySimEngineAndLogs(int day, List<DayStatistic> data, List<BaselineStatistics> stats, int[] dStat, int[] cStat){

        // Normalize
        normalizeStatistics(day, dStat, cStat, data, stats);

        System.out.println();
    }

    private static void generateStatistics(int days, int[] dStat, int[] cStat){
        Random rand = new Random();

        // 0 - 1000 for Discrete
        // 0 - 3000 for Continuous
        for (int i = 0; i < days; i++){

            dStat[i] = rand.nextInt(1001);
            cStat[i] = rand.nextInt(3001);
        }


    }

    public static void normalizeStatistics(int day, int[] dStat, int[] cStat, List<DayStatistic> dataStats, List<BaselineStatistics> baseStats){
        int dSum = 0;
        int cSum = 0;
        double dMean, dSD, cMean, cSD;

        // Find sum
        for (int i = 0; i < day; i++){
            dSum += dStat[i];
            cSum += cStat[i];
        }

        // Find mean
        dMean = getMean(dSum, day);
        cMean = getMean(cSum, day);

        // Find SD
        dSD = getStDev(dStat, dMean, day);
        cSD = getStDev(cStat, cMean, day);

        // Populate Baseline Data (for Training)
        // For all events in baseline stat
        for(int i = 0; i < baseStats.size(); i ++){
            if (baseStats.get(i).getEventType().equals("Discrete")){
                dataStats.add(new DiscreteStatistics( baseStats.get(i).getEventName(), day ));
                // Fill data to Discrete Event
                for (int j = 0; j < day; j++){
                    // Normalize
                    double zScore = findZScore(dStat[j], dMean, dSD);
                    int targetX = findTargetXInt(zScore, baseStats.get(i).getMean(), baseStats.get(i).getsDev());

                    ((DiscreteStatistics) dataStats.get(i)).getData()[j] = targetX;
                }

            } else if (baseStats.get(i).getEventType().equals("Continuous")) {
                dataStats.add(new ContinuousStatistics( baseStats.get(i).getEventName(), day ));
                // Fill data
                for (int j = 0; j < day; j++){
                    // Normalize
                    double zScore = findZScore(cStat[j], cMean, cSD);
                    double targetX = findTargetXDecimal(zScore, baseStats.get(i).getMean(), baseStats.get(i).getsDev());

                    ((ContinuousStatistics) dataStats.get(i)).getData()[j] = targetX;
                }

            }
        }


    }

    private static void CalculateMeanSD(List<DayStatistic> dataStats, List<BaselineStatistics> baselineStatistics){
        // Calculate Mean and Standard Deviation of each event
        for (DayStatistic s : dataStats){
            if (s instanceof DiscreteStatistics){
                s.calculateMean();
                s.calculateSD();
            }

            if (s instanceof ContinuousStatistics){
                s.calculateMean();
                s.calculateSD();
            }
        }

        for (DayStatistic dataStat : dataStats) {
            baselineStatistics.add(new BaselineStatistics(dataStat.getEventName(), dataStat.getMean(), dataStat.getsDev()));
        }
    }


    private static void AlertEngine(List<BaselineStatistics> baselineStatistics, int days, int threshold, double[][] dailyIndividual, List<DayStatistic> dayStatistics, double[] dailyCounter ){

        // Display the counter and determine whether an anomaly occurs or not
        System.out.printf("Threshold is %d%n", threshold);
        System.out.printf("When Daily Counter >= %d is an Anomaly%n", threshold);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        displayDailyCounter(threshold, days, dailyIndividual, dayStatistics, dailyCounter);


    }

    private static void displayDailyCounter(int threshold, int days, double[][] dailyIndividual, List<DayStatistic> liveData, double[] dailyCounter){

        // For each day
        for (int d = 0; d < days; d++){
            // For each event
            for (int e = 0; e < liveData.size(); e++){
                dailyCounter[d] += dailyIndividual[e][d];
            }
            System.out.printf("Daily Counter Day %d: %.2f ", d+1, dailyCounter[d]);

            if (dailyCounter[d] >=  threshold){
                System.out.println("(Anomaly Found !!!)");
            }else {
                System.out.println("(Not Anomaly)");
            }
        }

        System.out.println();



    }



    private static double getMean(int sum, int N){
        double mean = (double)sum/N;
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble( df.format(mean) );

    }
    private static double getStDev(int[] data, double mean, int N){
        double sumSq = 0;
        for (int i = 0; i < N; i ++) {
            double diff = data[i] - mean;
            sumSq += diff * diff;
        }

        double SD = Math.sqrt(sumSq / N);
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble( df.format(SD) );
    }

    private static double findZScore(int x, double mean, double stDev){
        return (x-mean)/stDev;
    }
    private static int findTargetXInt(double z, double mean, double stDev){
        return (int) ((z*stDev) + mean);
    }

    private static double findTargetXDecimal(double z, double mean, double stDev){

        double targetX = (z*stDev) + mean;

        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble( df.format(targetX) );

    }

    private static double findDailyTotal(List<DayStatistic> data, int n){
        double sum = 0.0;
        for (DayStatistic d : data){
            if (d instanceof DiscreteStatistics){
                sum += ((DiscreteStatistics) d).getData()[n];
            } else if (d instanceof ContinuousStatistics) {
                sum += ((ContinuousStatistics) d).getData()[n];
            }
        }

        return sum;
    }

    private static void AnalysisEngine(List<DayStatistic> dayStats, List<BaselineStatistics> baselineStats, List<BaselineStatistics> stats, int days, double[] dailyCounter, double[][] dailyIndividual){
        // For each day
        for (int i = 0; i < days; i++){
            dailyCounter[i] = findDailyTotal(dayStats, i);

            // For each event
            for (int j = 0; j < dayStats.size(); j++){
                double individualData = 0.0;
                if (dayStats.get(j) instanceof DiscreteStatistics){
                    individualData = Math.abs( (baselineStats.get(j).getMean() - ((DiscreteStatistics) dayStats.get(j)).getData()[i])/baselineStats.get(j).getsDev()  ) * stats.get(j).getWeight();
                }else if (dayStats.get(j) instanceof ContinuousStatistics){
                    individualData = Math.abs( (baselineStats.get(j).getMean() - ((ContinuousStatistics) dayStats.get(j)).getData()[i])/baselineStats.get(j).getsDev()  ) * stats.get(j).getWeight();
                }

                dailyIndividual[j][i] = individualData;

            }
        }
    }

    private static void displayDailyTotal(double[] dailyCounter){

        System.out.println("\n ~~ Daily Total ~~");

        for (int i = 0; i < dailyCounter.length; i++){
            System.out.printf("Day %d: %.2f%n", i+1, dailyCounter[i]);
        }

        System.out.println();
    }

    private static void displayDailyIndividual(double[][] dailyIndividual, List<DayStatistic> dataStatistics, int day){

        System.out.println("\n ~~ Daily Individual ~~");

        // for each day
        for (int i = 0; i < day ; i ++){
            System.out.println("Day " + (i+1) );

            for (int j = 0; j < dataStatistics.size(); j++){
                System.out.printf("%s: %.2f%n", dataStatistics.get(j).getEventName(), dailyIndividual[j][i]);
            }
            System.out.println();

        }

        System.out.println();

    }


    private static void displayBaselineStatistics(List<BaselineStatistics> baselineStats){
        System.out.printf("%-15s %-8s %-8s %-8s %-8s %-8s %s%n", "Event","Type", "Mean", "StDev", "Min", "Max", "Weight");
        System.out.println("---------------------------------------------------------------");
        for (BaselineStatistics b : baselineStats){
            System.out.println(b);
        }
        System.out.println();
    }

    private static void displayBaselineData(List<DayStatistic> baselineData, String type){
        System.out.printf("Baseline Stat (%s)%n", type);
        System.out.println("------------------------------");
        for(DayStatistic s : baselineData){
            if (s instanceof DiscreteStatistics){
                System.out.println(s);
            }

            if (s instanceof ContinuousStatistics){
                System.out.println(s);
            }
        }

        System.out.println("------------------------------");
    }

    private static int getThreshold(List<BaselineStatistics> baselineStatistics){
        int threshold = 0;

        for (BaselineStatistics b : baselineStatistics){
            threshold += b.getWeight();
        }

        return threshold*2;

    }


}
