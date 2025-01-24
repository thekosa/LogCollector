package wat.inz.kolektorlogow.core.modifiers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class CollectorLogsFilterTest {

    private CollectorLogsFilter filter;

    @Before
    public void setUp() {
        filter = new CollectorLogsFilter("tag", "E", "1234", "2345");
    }

    @Test
    public void setFilter() {
        filter.setFilter("tag", "E", "1234", "2345");
        assertTrue(Objects.equals(filter.getTagFilter(), "tag")
                && Objects.equals(filter.getPriorityFilter(), "E")
                && Objects.equals(filter.getPidFilter(), "1234")
                && Objects.equals(filter.getTidFilter(), "2345"));
    }

    @Test
    public void getTagFilter() {
        assertEquals("tag", filter.getTagFilter());
    }

    @Test
    public void getPriorityFilter() {
        assertEquals("E", filter.getPriorityFilter());
    }

    @Test
    public void getPidFilter() {
        assertEquals("1234", filter.getPidFilter());
    }

    @Test
    public void getTidFilter() {
        assertEquals("2345", filter.getTidFilter());
    }
}