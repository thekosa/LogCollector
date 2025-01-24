package wat.inz.kolektorlogow.core.modifiers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import wat.inz.kolektorlogow.core.log.CollectorLog;

public class CollectorLogsSortTest {
    private List<CollectorLog> logsList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        logsList.add(new CollectorLog("2025-01-22 15:53:09.515 1219 1811 E VerityUtils:Unable to finalize restore of wat.inz.kolektorlogow"));
        logsList.add(new CollectorLog("2025-01-22 15:53:09.515 1300 1811 E VerityUtils:Unable to finalize restore of wat.inz.kolektorlogow"));
    }

    @Test
    public void sort() {
        CollectorLogsSort collectorLogsSort = new CollectorLogsSort("PID", false);
        assertEquals("1300", collectorLogsSort.sort(logsList).get(0).getPid());
    }
}