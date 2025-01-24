package wat.inz.kolektorlogow.core.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;


public class CollectorLogTest {
    private String mockLogLine = "2025-01-22 15:53:09.515 1219 1811 E VerityUtils:Unable to finalize restore of wat.inz.kolektorlogow";
    private CollectorLog collectorLog;

    @Before
    public void setUp() {
        collectorLog = new CollectorLog(mockLogLine);
    }

    @Test
    public void setLog() {
        collectorLog.setLog(mockLogLine);
        assertEquals("E", collectorLog.getPriority());
    }

    @Test
    public void getRow() {
        assertEquals(6, collectorLog.getRow(true, true, true, true, true, true).size());
    }

    @Test
    public void isEmpty() {
        assertFalse(collectorLog.isEmpty());
    }

    @Test
    public void getDateTime() {
        String dateTimePattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}";
        Pattern pattern = Pattern.compile(dateTimePattern);
        assertTrue(pattern.matcher(collectorLog.getDateTime().toString()).matches());
    }

    @Test
    public void getPid() {
        assertEquals("1219", collectorLog.getPid());
    }

    @Test
    public void getTid() {
        assertEquals("1811", collectorLog.getTid());
    }

    @Test
    public void getPriority() {
        assertEquals("E", collectorLog.getPriority());
    }

    @Test
    public void getTag() {
        assertEquals("VerityUtils", collectorLog.getTag());
    }

    @Test
    public void getMessage() {
        assertEquals("Unable to finalize restore of wat.inz.kolektorlogow", collectorLog.getMessage());
    }

    @Test
    public void getColor() {
        assertEquals(Color.RED, collectorLog.getColor());
    }
}