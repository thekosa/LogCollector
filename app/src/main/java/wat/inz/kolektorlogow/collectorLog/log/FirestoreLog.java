package wat.inz.kolektorlogow.collectorLog.log;

import lombok.Getter;

public @Getter class FirestoreLog {
    private CollectorLogDate dateTime;
    private String pid;
    private String tid;
    private String priority;
    private String tag;
    private String message;

    public FirestoreLog(CollectorLog log) {
        this.dateTime = log.getDateTime();
        this.pid = log.getPid();
        this.tid = log.getTid();
        this.priority = log.getPriority();
        this.tag = log.getTag();
        this.message = log.getMessage();
    }
}
