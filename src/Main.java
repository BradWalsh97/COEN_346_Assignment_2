import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        //Start by getting the input file and creating an array consisting of all the light bulbs
        File file = new File(Paths.get("input.txt").toAbsolutePath().toString());
        BufferedReader in = new BufferedReader(new FileReader(file));
        String[] splitInputArray;
        String str;
        int processCount = 0;
        Scheduler scheduler = new Scheduler();


        //start by creating a processs object for each item in the text file
        while ((str = in.readLine()) != null) {
            splitInputArray = str.split("\\s", 2); //split arrival time and
            scheduler.addProcess(
                    new Process(processCount,
                    Integer.parseInt(splitInputArray[0].trim()),
                    Integer.parseInt(splitInputArray[1].trim())));

            processCount++;
        }
        scheduler.start();
        
    }
}
