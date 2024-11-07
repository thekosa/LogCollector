package wat.inz.kolektorlogow.tableview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wat.inz.kolektorlogow.CollectorLog;

public class TableViewModel {
    public List<String> getColumnHeaders() {
        return Arrays.asList("Date", "Time", "PID", "TID", "Priority", "Tag", "Message");
    }

    public List<List<String>> getCellData(List<CollectorLog> logs) {
        List<List<String>> cellData = new ArrayList<>();

        for (CollectorLog log : logs) {
            List<String> row = Arrays.asList(
                    log.getDate(),
                    log.getPid(),
                    log.getTid(),
                    log.getPriority(),
                    log.getTag(),
                    log.getMessage()
            );
            cellData.add(row);
        }
        return cellData;
    }
}
