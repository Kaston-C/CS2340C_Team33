import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FilterStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterAfterDateTime implements FilterStrategy {
    @Override
    public <T> List<T> filter(List<T> items, String param) {
        LocalDateTime dateTime = LocalDateTime.parse(param);
        List<T> filteredList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if (items.get(0) instanceof Dining) {
            for (T dining : items) {
                if (((Dining) dining).getReservationDateTime().isAfter(dateTime)) {
                    filteredList.add(dining);
                }
            }
        } else if (items.get(0) instanceof Accommodation) {
            for (T accommodation : items) {
                String checkInDateStr = ((Accommodation) accommodation).getCheckInDate();
                LocalDateTime checkInDate = LocalDateTime.parse(checkInDateStr + "T00:00:00", formatter);
                if (checkInDate.isAfter(dateTime)) {
                    filteredList.add(accommodation);
                }
            }
        }
        return filteredList;
    }
}
