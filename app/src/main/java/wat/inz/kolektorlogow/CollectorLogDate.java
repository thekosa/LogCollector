package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollectorLogDate extends Date {
    private final Date date;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public CollectorLogDate(String dateTimeString) throws ParseException {
        this.date = formatter.parse(dateTimeString);
    }

    @NonNull
    @Override
    public String toString() {
        return formatter.format(date);
    }
}
