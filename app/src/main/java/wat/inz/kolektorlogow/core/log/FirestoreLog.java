package wat.inz.kolektorlogow.core.log;

import lombok.Getter;
import lombok.Setter;

public @Getter class FirestoreLog {
    private CollectorLogDate dateTime;
    private String pid;
    private String tid;
    private String priority;
    private String tag;
    private String message;
    private long ordinalNumber; //Firestore doesn't serialize static fields
    @Setter
    private static long staticOrdinalNumber;

    public FirestoreLog(CollectorLog log) {
        this.dateTime = log.getDateTime();
        this.pid = log.getPid();
        this.tid = log.getTid();
        this.priority = log.getPriority();
        this.tag = log.getTag();
        this.message = log.getMessage();
        ordinalNumber = ++staticOrdinalNumber;
    }
}
