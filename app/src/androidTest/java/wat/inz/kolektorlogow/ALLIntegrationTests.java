package wat.inz.kolektorlogow;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        IntegrationDeviceDAOTests.class,
        IntegrationLogsDAOTests.class
})
public class ALLIntegrationTests {
}
