package wat.inz.kolektorlogow;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import wat.inz.kolektorlogow.DAO.FirestoreDeviceDAOTest;
import wat.inz.kolektorlogow.core.log.CollectorLogDateTest;
import wat.inz.kolektorlogow.core.log.CollectorLogTest;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsFilterTest;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsSortTest;
import wat.inz.kolektorlogow.meta.FirestoreDeviceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FirestoreDeviceDAOTest.class,
        FirestoreDeviceTest.class,
        CollectorLogTest.class,
        CollectorLogsFilterTest.class,
        CollectorLogsSortTest.class,
        CollectorLogDateTest.class
})
public class AllUnitTests {
}
