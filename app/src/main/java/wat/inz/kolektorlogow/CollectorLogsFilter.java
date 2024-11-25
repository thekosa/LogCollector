package wat.inz.kolektorlogow;

import lombok.Data;

public @Data class CollectorLogsFilter {
    private String tagFilter;
    private String priorityFilter;
    private String pidFilter;
    private String tidFilter;

    public CollectorLogsFilter(String tagFilter, String priorityFilter, String pidFilter, String tidFilter) {
        setFilter(tagFilter, priorityFilter, pidFilter, tidFilter);
    }

    public void setFilter(String tagFilter, String priorityFilter, String pidFilter, String tidFilter) {
        if (isNull(tagFilter, priorityFilter, pidFilter, tidFilter)) {
            clearFilters();
            return;
        }
        this.tagFilter = normalizeFilter(tagFilter);
        this.priorityFilter = normalizeFilter(priorityFilter);
        this.pidFilter = normalizeFilter(pidFilter);
        this.tidFilter = normalizeFilter(tidFilter);
    }

    private String normalizeFilter(String filter) {
        return (filter == null || filter.isBlank() || filter.equals("*")) ? null : filter;
    }

    private void clearFilters() {
        this.tagFilter = null;
        this.priorityFilter = null;
        this.pidFilter = null;
        this.tidFilter = null;
    }

    public boolean isNull() {
        return tagFilter == null && priorityFilter == null && pidFilter == null && tidFilter == null;
    }

    private boolean isNull(String tagFilter, String priorityFilter, String pidFilter, String tidFilter) {
        return tagFilter == null && priorityFilter == null && pidFilter == null && tidFilter == null;
    }
}
