import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy/M/d");
        ZoneId zoneId = ZoneId.of("America/New_York");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss a", Locale.US);

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatterDate);
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(formatter) + " " + zoneId.getDisplayName(TextStyle.SHORT, Locale.US);
    }
}
