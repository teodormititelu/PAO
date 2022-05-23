package PAO.Entities.Services;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Audit {

    private static Audit singletonInstance;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private List<String> actionHistory = new ArrayList<>();

    public static Audit getInstance(){
        if (singletonInstance == null)
            singletonInstance = new Audit();
        return singletonInstance;
    }

    private static List<String> getCSVStrings(String fileName){
        List<String> lines = new ArrayList<>();
        try(var in = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = in.readLine()) != null ) {
                lines.add( line );
            }

        }catch (IOException e) {
            System.out.println("Operation history is empty!");
        }
        return lines;
    }

    public void parseActionHistory() {
        var lines = Audit.getCSVStrings("Data/Audit.csv");
        actionHistory.addAll(lines);
    }

    public void addAction(String action){
        actionHistory.add( action + "," + formatter.format(LocalDateTime.now()));

        try(var writer = new FileWriter("Data/Audit.csv", true)) {
            writer.write(action + "," + formatter.format(LocalDateTime.now()));
            writer.write("\n");

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    public void writeAudit()
    {
        Stream<String> actionHistoryStream = actionHistory.stream();
        try{
            var writer = new FileWriter("Data/Audit.csv");
            Consumer<String> consumer = action -> {
                try {
                    writer.write(action);
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println( e );
                }
            };
            actionHistoryStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audit audit = (Audit) o;
        return Objects.equals(actionHistory, audit.actionHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionHistory);
    }
}
