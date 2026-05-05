package com.javafx.csit228capstone.model;

public class QueueInsertValue {
    private int queueId;
    private int formId;

    public QueueInsertValue(int queueId, int formId) {
        this.queueId = queueId;
        this.formId = formId;
    }

    public int getQueueId() {
        return queueId;
    }

    public int getFormId() {
        return formId;
    }
}
