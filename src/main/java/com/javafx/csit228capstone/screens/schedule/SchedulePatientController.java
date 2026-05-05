package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SchedulePatientController implements Initializable {

    @FXML private Label scheduleLabel;
    @FXML private Label selectedDateLabel;
    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;
    @FXML private TextField searchField;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private Button previousButton;
    @FXML private Button confirmButton;
    @FXML private VBox timeSlotsContainer;

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate today;

    private final Map<String, List<Service>> slotServices = new LinkedHashMap<>();

    private static final String CLOSING_TIME = "5:00 PM";

    private static final List<String> TIME_SLOTS = List.of(
            "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
            "4:00 PM", "5:00 PM"
    );

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_HEADER_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, MMMM d");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        renderCalendar();
        updateDateHeader();
        renderTimeSlots();

        prevMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            renderCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            renderCalendar();
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch(newVal));
    }

    private void renderTimeSlots() {
        timeSlotsContainer.getChildren().clear();

        for (String timeSlot : TIME_SLOTS) {
            HBox row = buildTimeRow(timeSlot);
            timeSlotsContainer.getChildren().add(row);
        }
    }

    private HBox buildTimeRow(String timeSlot) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("time-row");
        row.setPadding(new Insets(10, 10, 10, 10));
        row.setMinHeight(48);

        Label timeLabel = new Label(timeSlot);
        timeLabel.setMinWidth(65);
        timeLabel.getStyleClass().add("time-label");

        if (timeSlot.equals(CLOSING_TIME)) {
            Label closedLabel = new Label("Closed");
            closedLabel.getStyleClass().add("closed-label");
            row.getChildren().addAll(timeLabel, closedLabel);
        } else {
            HBox chipsBox = new HBox(8);
            chipsBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(chipsBox, Priority.ALWAYS);

            List<Service> services = slotServices.get(timeSlot);
            if (services.isEmpty()) {
                Label emptyLabel = new Label("—");
                emptyLabel.getStyleClass().add("closed-label");
                chipsBox.getChildren().add(emptyLabel);
            } else {
                for (Service service : services) {
                    chipsBox.getChildren().add(buildChip(service));
                }
            }

            row.getChildren().addAll(timeLabel, chipsBox);
        }

        return row;
    }

    private HBox buildChip(Service service) {
        HBox chip = new HBox(6);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getServiceType());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getServiceType());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        chip.getChildren().addAll(dot, nameLabel);
        return chip;
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            timeSlotsContainer.getChildren().forEach(node -> {
                node.setVisible(true);
                node.setManaged(true);
            });
            return;
        }

        String lower = query.toLowerCase();
        timeSlotsContainer.getChildren().forEach(node -> {
            if (node instanceof HBox row) {
                boolean matches = row.getChildren().stream().anyMatch(child -> {
                    if (child instanceof HBox chipsBox) {
                        return chipsBox.getChildren().stream().anyMatch(chip -> {
                            if (chip instanceof HBox chipHBox) {
                                return chipHBox.getChildren().stream().anyMatch(inner ->
                                        inner instanceof Label lbl &&
                                                lbl.getText().toLowerCase().contains(lower)
                                );
                            }
                            return false;
                        });
                    }
                    return false;
                });
                node.setVisible(matches);
                node.setManaged(matches);
            }
        });
    }

    private void renderCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.format(MONTH_YEAR_FORMATTER));

        int firstDayOfWeek = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        YearMonth prevMonth = currentYearMonth.minusMonths(1);
        int prevMonthDays = prevMonth.lengthOfMonth();
        for (int i = 0; i < firstDayOfWeek; i++) {
            int day = prevMonthDays - firstDayOfWeek + i + 1;
            Label lbl = createDayLabel(String.valueOf(day), "cal-cell-inactive");
            calendarGrid.add(lbl, i, 0);
        }

        int col = firstDayOfWeek;
        int row = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Button btn = createDayButton(day, date);
            btn.setPrefSize(38, 38);
            btn.setMaxSize(38, 38);
            calendarGrid.add(btn, col, row);
            col++;
            if (col == 7) { col = 0; row++; }
        }

        int nextDay = 1;
        while (col != 0) {
            Label lbl = createDayLabel(String.valueOf(nextDay++), "cal-cell-inactive");
            calendarGrid.add(lbl, col, row);
            col++;
            if (col == 7) col = 0;
        }
    }

    private Button createDayButton(int day, LocalDate date) {
        Button btn = new Button(String.valueOf(day));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER);

        if (date.equals(selectedDate)) {
            btn.getStyleClass().add("cal-cell-selected");
        } else if (date.equals(today)) {
            btn.getStyleClass().add("cal-cell-today");
        } else {
            btn.getStyleClass().add("cal-cell");
        }

        btn.setOnAction(e -> {
            selectedDate = date;
            renderCalendar();
            updateDateHeader();
            // database for the selected date
            renderTimeSlots();
        });

        return btn;
    }

    private Label createDayLabel(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }

    private void updateDateHeader() {
        selectedDateLabel.setText(selectedDate.format(DATE_HEADER_FORMATTER));
    }

    @FXML private void onClickBtnGeneralWellness()     { scheduleLabel.setText("General Wellness"); }
    @FXML private void onClickBtnWomenHealth()     { scheduleLabel.setText("Women's Health"); }
    @FXML private void onClickBtnSpecializedFields() { scheduleLabel.setText("Specialized Fields"); }
    @FXML private void onClickBtnDiagnosticsLab()  { scheduleLabel.setText("Diagnostics & Laboratory"); }

    public LocalDate getSelectedDate() { return selectedDate; }
}