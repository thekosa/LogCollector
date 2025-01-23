package wat.inz.kolektorlogow.core.collection;

import static org.junit.Assert.assertEquals;

import android.webkit.RoboCookieManager;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import wat.inz.kolektorlogow.core.log.CollectorLog;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsFilter;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsSort;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33, shadows = {RoboCookieManager.class})
public class CollectorLogsTest {
    private CollectorLogs collectorLogs = new CollectorLogs();
    private BufferedReader mockbufferedReader;
    private FirebaseFirestore mockdbConnection = FirebaseFirestore.getInstance();
    private CollectorLogsFilter mockcollectorLogsFilter = Mockito.mock(CollectorLogsFilter.class);
    private CollectorLogsSort mockcollectorLogsSort = Mockito.mock(CollectorLogsSort.class);
    private CollectorLog mockcollectorLog = Mockito.mock(CollectorLog.class);


    @Before
    public void setUp() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("2025-01-22 15:53:09.515 1219 1811 E VerityUtils:Unable to finalize restore of wat.inz.kolektorlogow").append("\n");
        sb.append("--------- beginning").append("\n");
        sb.append("2025-01-22 15:53:09.515 1219 1811 E VerityUtils:Unable to finalize restore of wat.inz.kolektorlogow").append("\n");

        // Tworzymy InputStream z tego stringa
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        mockbufferedReader = new BufferedReader(reader);

        mockcollectorLogsFilter.setFilter(null, "E", null, null);
        mockcollectorLogsSort.setColumnName("Priority");
        mockcollectorLogsSort.setDirection(true);
    }

    @Test
    public void generateLogs() {
       // collectorLogs.generateLogs(mockbufferedReader, mockdbConnection);
       // assertEquals(2, collectorLogs.getLogsList().size());
    }
//
//    @Test
//    public void filterOutLogs() {
//        collectorLogs.generateLogs(mockbufferedReader, mockdbConnection);
//        assertEquals(2, collectorLogs.filterOutLogs(mockcollectorLogsFilter).size());
//    }
//
////    @Test
////    public void sortOutLogs() {
////        collectorLogs.generateLogs(mockbufferedReader, mockdbConnection);
////        collectorLogs.sortOutLogs(mockcollectorLogsSort);
////      //  when(mockcollectorLog.getPid()).thenReturn("");
////       assertEquals("1219", collectorLogs.getLogsList().get(1).getPid());
////    }
//
//    @Test
//    public void destroyLogsList() {
//        collectorLogs.generateLogs(mockbufferedReader, mockdbConnection);
//        collectorLogs.destroyLogsList();
//        assertEquals(0, collectorLogs.getLogsList().size());
//    }
//
//    @Test
//    public void getLogsList() {
//        collectorLogs.generateLogs(mockbufferedReader, mockdbConnection);
//        assertEquals(2, collectorLogs.getLogsList().size());
//    }
}