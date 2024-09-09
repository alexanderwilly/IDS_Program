My files are consists of
- IDS.java (Main Driver)
- FileIO.java
- BaselineStatistics.java
- DayStatistic.java
==> Consists of DayStatistics class, Continuous Statistics class, and DiscreteStatistics class.
* This program is using Java JDK 1.8

Text files:
- Events.txt
- Stats.txt (for training)
- StatsLive.txt (for live)

How to compile:
1. Open command prompt / Powershell and find the directory where java files are located
2. Compile all .java file with the following command:
==> javac -d . *.java
3. Run the IDS Java program with entering the Events text file, Stats text file, and number of days.
==> java IDS Events.txt Stats.txt 30
