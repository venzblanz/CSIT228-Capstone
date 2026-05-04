package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleStaffController implements Initializable {

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

    // Stores services per time slot: key = "8:00 AM", value = list of services
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

        // Initialize empty service lists for each slot
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

    // ─── Time Slot Rendering ──────────────────────────────────────────────────

    private void renderTimeSlots() {
        timeSlotsContainer.getChildren().clear();

        for (String timeSlot : TIME_SLOTS) {
            HBox row = buildTimeRow(timeSlot);
            timeSlotsContainer.getChildren().add(row);
        }
    }

    private HBox buildTimeRow(String timeSlot) {
        HBox row = new HBox(8);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.getStyleClass().add("time-row");
        row.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
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
            chipsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            HBox.setHgrow(chipsBox, javafx.scene.layout.Priority.ALWAYS);

            // Render existing services
            List<Service> services = slotServices.get(timeSlot);
            for (Service service : new ArrayList<>(services)) {
                chipsBox.getChildren().add(buildChip(service, timeSlot, chipsBox));
            }

            // + Add button
            Button addBtn = new Button("+ Add");
            addBtn.getStyleClass().add("add-button");
            addBtn.setOnAction(e -> handleAddService(timeSlot, chipsBox, addBtn));
            chipsBox.getChildren().add(addBtn);

            row.getChildren().addAll(timeLabel, chipsBox);
        }

        return row;
    }

    private HBox buildChip(Service service, String timeSlot, HBox chipsBox) {
        HBox chip = new HBox(4);
        chip.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getServiceType());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getServiceType());

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

    private void handleAddService(String timeSlot, HBox chipsBox, Button addBtn) {
        // TODO: Replace this with a real dialog/popup for selecting a service from DB later.
        // For now, opens a simple inline text input as a placeholder.
        showAddServiceDialog(timeSlot, chipsBox, addBtn);
    }

    private void showAddServiceDialog(String timeSlot, HBox chipsBox, Button addBtn) {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Add Service");
        dialog.setHeaderText("Add a service to " + timeSlot);
        dialog.setContentText("Service name:");

        dialog.showAndWait().ifPresent(name -> {
            if (name.isBlank()) return;

            Service service = new Service(name.trim(), resolveColor(name.trim()));
            slotServices.get(timeSlot).add(service);

            HBox chip = buildChip(service, timeSlot, chipsBox);
            int addIndex = chipsBox.getChildren().indexOf(addBtn);
            chipsBox.getChildren().add(addIndex, chip);
        });
    }

    // ─── Color Resolution (extend when DB is ready) ───────────────────────────

    private String resolveColor(String serviceName) {
        String lower = serviceName.toLowerCase();
        if (lower.contains("prenatal") || lower.contains("postnatal") || lower.contains("maternal")) return "pink";
        if (lower.contains("pediatric") || lower.contains("child"))                                  return "green";
        if (lower.contains("family planning") || lower.contains("tb") || lower.contains("dots"))     return "teal";
        if (lower.contains("blood") || lower.contains("lab") || lower.contains("urinalysis"))        return "amber";
        return "blue"; // default
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
            // TODO: reload services from DB for the selected date
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

    // ─── Category buttons ─────────────────────────────────────────────────────

    @FXML private void onClickBtnGeneralCare()     { scheduleLabel.setText("General Care"); }
    @FXML private void onClickBtnWomenHealth()     { scheduleLabel.setText("Women's Health"); }
    @FXML private void onClickBtnSpecializedCare() { scheduleLabel.setText("Specialized Care"); }
    @FXML private void onClickBtnLabDiagnostics()  { scheduleLabel.setText("Labs & Diagnostics"); }

    // ─── Getters for later use ────────────────────────────────────────────────

    public LocalDate getSelectedDate() { return selectedDate; }
    public Map<String, List<Service>> getSlotServices() { return slotServices; }
}