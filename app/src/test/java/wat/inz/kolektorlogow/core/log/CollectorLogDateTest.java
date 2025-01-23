package wat.inz.kolektorlogow.core.log;

import static org.junit.Assert.*;

import org.junit.Test;

import java.text.ParseException;

public class CollectorLogDateTest {

    @Test
    public void testToString() throws ParseException {
        CollectorLogDate date = new CollectorLogDate("2023-06-01 12:34:56.789");
        assertEquals("2023-06-01 12:34:56.789", date.toString());
    }
}