package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class BaseScheduleController implements Initializable {

    @FXML protected Label screenLabel;
    @FXML protected Label selectedDateLabel;
    @FXML protected Label monthYearLabel;
    @FXML protected GridPane calendarGrid;
    @FXML protected Button prevMonthButton;
    @FXML protected Button nextMonthButton;
    @FXML protected VBox timeSlotsContainer;

    protected YearMonth currentYearMonth;
    protected LocalDate selectedDate;
    protected LocalDate today;

    protected final Map<String, List<Service>> slotServices = new LinkedHashMap<>();
    protected final ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();

    protected static final String CLOSING_TIME = "5:00 PM";
    protected static final List<String> TIME_SLOTS = List.of("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");
    protected static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    protected static final DateTimeFormatter DATE_HEADER_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        prevMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            renderCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            renderCalendar();
        });

        setupSpecifics();
        renderCalendar();
        updateDateHeader();
        loadServicesForDate(selectedDate);
    }

    protected abstract void setupSpecifics();
    protected abstract HBox buildTimeRow(String timeSlot);
    protected abstract boolean allowPastDates();

    protected void loadServicesForDate(LocalDate date) {
        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        new Thread(() -> {
            try {
                Map<String, List<Service>> scheduleMap = scheduleDAO.getScheduleForDate(date);
                Platform.runLater(() -> {
                    for (Map.Entry<String, List<Service>> entry : scheduleMap.entrySet()) {
                        slotServices.put(entry.getKey(), entry.getValue());
                    }
                    renderTimeSlots();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void renderTimeSlots() {
        timeSlotsContainer.getChildren().clear();
        for (int i = 0; i < TIME_SLOTS.size(); i++) {
            HBox row = buildTimeRow(TIME_SLOTS.get(i));
            row.setOpacity(0);
            timeSlotsContainer.getChildren().add(row);

            FadeTransition ft = new FadeTransition(Duration.millis(280), row);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(i * 45L));
            ft.play();
        }
    }

    protected void renderCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.format(MONTH_YEAR_FORMATTER));

        int firstDayOfWeek = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();
        YearMonth prevMonth = currentYearMonth.minusMonths(1);
        int prevMonthLen = prevMonth.lengthOfMonth();

        for (int i = 0; i < firstDayOfWeek; i++) {
            int day = prevMonthLen - firstDayOfWeek + i + 1;
            calendarGrid.add(createDayLabel(String.valueOf(day), "cal-cell-inactive"), i, 0);
        }

        int col = firstDayOfWeek;
        int row = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Button btn = createDayButton(day, date);
            btn.setPrefSize(36, 36);
            btn.setMaxSize(36, 36);
            calendarGrid.add(btn, col, row);
            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        int nextDay = 1;
        while (col != 0) {
            calendarGrid.add(createDayLabel(String.valueOf(nextDay++), "cal-cell-inactive"), col, row);
            col++;
            if (col == 7) {
                col = 0;
            }
        }
    }

    protected Button createDayButton(int day, LocalDate date) {
        Button btn = new Button(String.valueOf(day));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER);

        boolean isPast = date.isBefore(today);

        if (date.equals(selectedDate)) {
            btn.getStyleClass().add("cal-cell-selected");
        } else if (date.equals(today)) {
            btn.getStyleClass().add("cal-cell-today");
        } else if (isPast && !allowPastDates()) {
            btn.getStyleClass().add("cal-cell-inactive");
            btn.setDisable(true);
        } else {
            btn.getStyleClass().add("cal-cell");
        }

        if (!btn.isDisabled()) {
            btn.setOnAction(e -> {
                selectedDate = date;
                renderCalendar();
                updateDateHeader();
                loadServicesForDate(selectedDate);
            });
        }
        return btn;
    }

    protected Label createDayLabel(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }

    protected void updateDateHeader() {
        if (selectedDateLabel != null) {
            selectedDateLabel.setText(selectedDate.format(DATE_HEADER_FORMATTER));
        }
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public Map<String, List<Service>> getSlotServices() {
        return slotServices;
    }
}