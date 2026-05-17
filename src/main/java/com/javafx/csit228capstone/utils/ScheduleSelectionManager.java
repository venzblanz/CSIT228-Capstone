package com.javafx.csit228capstone.utils;

import java.time.LocalDate;

public class ScheduleSelectionManager {
    private static final ScheduleSelectionManager instance = new ScheduleSelectionManager();

    private LocalDate pickedDate;
    private String pickedTime;
    private String pickedService;

    private ScheduleSelectionManager() {}

    public static ScheduleSelectionManager getInstance() {
        return instance;
    }

    public void saveSchedule(LocalDate pickedDate, String pickedTime, String pickedService) {
        this.pickedDate = pickedDate;
        this.pickedTime = pickedTime;
        this.pickedService = pickedService;
    }

    public LocalDate getPickedDate() {
        return pickedDate;
    }

    public String getPickedTime() {
        return pickedTime;
    }

    public String getPickedService() {
        return pickedService;
    }

    public boolean hasSchedule() {
        return pickedDate != null && pickedTime != null && pickedService != null;
    }

    public void clear() {
        pickedDate = null;
        pickedTime = null;
        pickedService = null;
    }
}
