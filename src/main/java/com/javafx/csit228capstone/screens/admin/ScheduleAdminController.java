package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.screens.schedule.AddServiceDialog;
import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.ScheduleDAO;
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

public class ScheduleAdminController implements Initializable {

    @FXML private Label scheduleLabel;
    @FXML private Label selectedDateLabel;
    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;
    @FXML private TextField searchField;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private VBox timeSlotsContainer;

    @FXML private com.javafx.csit228capstone.screens.admin.AdminMenuController menuController;

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate today;

    private final Map<String, List<Service>> slotServices = new LinkedHashMap<>();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO(DatabaseConfig.getConnection());

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
        if (menuController != null) {
            menuController.setActiveButton(menuController.getScheduleBtn());
        }

        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        renderCalendar();
        updateDateHeader();
        loadServicesForDate(selectedDate);
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

    // ─── Load from DB ─────────────────────────────────────────────────────────

    private void loadServicesForDate(LocalDate date) {
        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }
        try {
            Map<String, List<Service>> loaded = scheduleDAO.getScheduleForDate(date);
            loaded.forEach((slot, services) -> slotServices.put(slot, services));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Time Slot Rendering ──────────────────────────────────────────────────

    private void renderTimeSlots() {
        timeSlotsContainer.getChildren().clear();
        for (String timeSlot : TIME_SLOTS) {
            timeSlotsContainer.getChildren().add(buildTimeRow(timeSlot));
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

            for (Service service : new ArrayList<>(slotServices.get(timeSlot))) {
                chipsBox.getChildren().add(buildChip(service, timeSlot, chipsBox));
            }

            Button addBtn = new Button("+ Add");
            addBtn.getStyleClass().add("add-button");
            addBtn.setOnAction(e -> showAddServiceDialog(timeSlot, chipsBox, addBtn));
            chipsBox.getChildren().add(addBtn);

            row.getChildren().addAll(timeLabel, chipsBox);
        }

        return row;
    }

    private HBox buildChip(Service service, String timeSlot, HBox chipsBox) {
        HBox chip = new HBox(4);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        Button removeBtn = new Button("×");
        removeBtn.setMnemonicParsing(false);
        removeBtn.getStyleClass().add("chip-remove");
        removeBtn.setOnAction(e -> {
            slotServices.get(timeSlot).remove(service);
            chipsBox.getChildren().remove(chip);
        });

        chip.getChildren().addAll(dot, nameLabel, removeBtn);
        return chip;
    }

    private void showAddServiceDialog(String timeSlot, HBox chipsBox, Button addBtn) {
        AddServiceDialog dialog = new AddServiceDialog(timeSlot, selectedDateLabel.getText(), service -> {
            slotServices.get(timeSlot).add(service);
            HBox chip = buildChip(service, timeSlot, chipsBox);
            int addIndex = chipsBox.getChildren().indexOf(addBtn);
            chipsBox.getChildren().add(addIndex, chip);
        });
        dialog.show(timeSlotsContainer.getScene().getWindow());
    }

    // ─── Search ───────────────────────────────────────────────────────────────

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

    // ─── Calendar ─────────────────────────────────────────────────────────────

    private void renderCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.format(MONTH_YEAR_FORMATTER));

        int firstDayOfWeek = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        YearMonth prevMonth = currentYearMonth.minusMonths(1);
        int prevMonthDays = prevMonth.lengthOfMonth();
        for (int i = 0; i < firstDayOfWeek; i++) {
            int day = prevMonthDays - firstDayOfWeek + i + 1;
            calendarGrid.add(createDayLabel(String.valueOf(day), "cal-cell-inactive"), i, 0);
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
            calendarGrid.add(createDayLabel(String.valueOf(nextDay++), "cal-cell-inactive"), col, row);
            col++;
            if (col == 7) col = 0;
        }
    }

    private Button createDayButton(int day, LocalDate date) {
        Button btn = new Button(String.valueOf(day));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER);

        if (date.equals(selectedDate))  btn.getStyleClass().add("cal-cell-selected");
        else if (date.equals(today))    btn.getStyleClass().add("cal-cell-today");
        else                            btn.getStyleClass().add("cal-cell");

        btn.setOnAction(e -> {
            selectedDate = date;
            renderCalendar();
            updateDateHeader();
            loadServicesForDate(selectedDate);
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

    public LocalDate getSelectedDate() { return selectedDate; }
    public Map<String, List<Service>> getSlotServices() { return slotServices; }
}