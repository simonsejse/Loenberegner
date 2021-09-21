package dk.simonsejse.loenberegning;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.stream.Stream;

public class DateRange implements Iterable<LocalDateTime> {

    //TODO: Delete this class when you're done but its good to keep because you remember the Iterable<>

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private ChronoUnit chronoUnit;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate, ChronoUnit chronoUnit) {
        //check that range is valid (null, start < end)
        this.startDate = startDate;
        this.endDate = endDate;
        this.chronoUnit = chronoUnit;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Iterator<LocalDateTime> iterator() {
        return stream().iterator();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Stream<LocalDateTime> stream() {
        return Stream.iterate(startDate, d -> d.plus(1, chronoUnit))
                .limit(chronoUnit.between(startDate, endDate) + 1);
    }
}
