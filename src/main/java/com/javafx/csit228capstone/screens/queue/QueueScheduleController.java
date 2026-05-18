package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.model.QueueInsertValue;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.*;
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

public class QueueScheduleController implements Initializable {
    @FXML private Label scheduleLabel;
    @FXML private Label scheduleLabel1;
    @FXML private Label selectedDateLabel;
    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;
    @FXML private TextField searchField;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private VBox timeSlotsContainer;
    @FXML private Button previousButton;
    @FXML private Button confirmButton;
    @FXML private Label error_message;
    @FXML private Label gwLabel;
    @FXML private Label whLabel;
    @FXML private Label sfLabel;
    @FXML private Label dlLabel;
    @FXML private HBox error_container;

    @FXML private com.javafx.csit228capstone.helper.MenuController menuController;

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate today;
    private String schedType;
    private QueueInsertValue qiv;
    private QueueTicket qt;
    private LocalDate pickedDate;
    private String pickedService;
    private String pickedTime;
    private HBox selectedChip;

    // track active category filter (null = show all)
    private String activeCategory = null;

    private final Map<String, List<Service>> slotServices = new LinkedHashMap<>();
    private final ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();
    private final FormManager formManager = FormManager.getInstance();
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

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

    public void initializeData(String type){
        menuController.setActiveButton(menuController.getQueueBtn());
        schedType = type;
        setUpScreen(type);
    }
    private void setUpScreen(String type){
        if(type.equals("General Wellness")){
            scheduleLabel1.setText("General Wellness Schedule");
            whLabel.setVisible(false);
            whLabel.setManaged(false);
            sfLabel.setVisible(false);
            sfLabel.setManaged(false);
            dlLabel.setVisible(false);
            dlLabel.setManaged(false);
        }else if (type.equals("Women's Health")){
            scheduleLabel1.setText("Women's Health Schedule");
            gwLabel.setVisible(false);
            gwLabel.setManaged(false);
            sfLabel.setVisible(false);
            sfLabel.setManaged(false);
            dlLabel.setVisible(false);
            dlLabel.setManaged(false);
        }else if (type.equals("Specialized Fields")){
            scheduleLabel1.setText("Specialized Fields Schedule");
            whLabel.setVisible(false);
            whLabel.setManaged(false);
            gwLabel.setVisible(false);
            gwLabel.setManaged(false);
            dlLabel.setVisible(false);
            dlLabel.setManaged(false);
        }else{
            scheduleLabel1.setText("Diagnostics and Laboratory Schedule");
            whLabel.setVisible(false);
            whLabel.setManaged(false);
            sfLabel.setVisible(false);
            sfLabel.setManaged(false);
            gwLabel.setVisible(false);
            gwLabel.setManaged(false);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error_message.setVisible(false);
        error_container.setVisible(false);
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
        loadServicesForDate(selectedDate, schedType);
        renderTimeSlots();

        previousButton.setOnAction(e -> handlePrevious());
        confirmButton.setOnAction(e -> handleConfirm());

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

    private void loadServicesForDate(LocalDate date, String type) {
        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }
        try {
            Map<String, List<Service>> loaded = scheduleDAO.getScheduleForDate(date);
            loaded.forEach((slot, services) -> {
                List<Service> list = new ArrayList<>();
                for(Service s: services) {
                    if(Objects.equals(s.getServiceType(), type)){
                        list.add(s);
                    }
                }
                slotServices.put(slot, list);
            });
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

            List<Service> services = slotServices.get(timeSlot);

            // apply category filter
            List<Service> filtered = (activeCategory == null) ? services :
                    services.stream()
                    .filter(s -> s.getServiceType().equals(activeCategory))
                    .toList();

            if (filtered.isEmpty()) {
                Label emptyLabel = new Label("—");
                emptyLabel.getStyleClass().add("closed-label");
                chipsBox.getChildren().add(emptyLabel);
            } else {
                for (Service service : filtered) {
                    HBox chip = buildChip(service);
                    chip.setOnMouseClicked(e -> {
                        e.consume();
                        if (selectedChip != null) {
                            selectedChip.setStyle("");
                        }
                        selectedChip = chip;
                        selectedChip.setStyle("-fx-background-color: #f0f0f0;");
                        setQueueSchedule(service, timeSlot);
                        clearError();
                    });
                    chip.setOnMouseEntered(e -> {
                        chip.setCursor(javafx.scene.Cursor.HAND);
                    });
                    chipsBox.getChildren().add(chip);
                }
            }

            row.getChildren().addAll(timeLabel, chipsBox);
        }

        return row;
    }

    private HBox buildChip(Service service) {
        HBox chip = new HBox(6);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        chip.getChildren().addAll(dot, nameLabel);
        return chip;
    }

    // ─── Category Filter ──────────────────────────────────────────────────────

    private void setActiveCategory(String category) {
        // toggle off if same category clicked again
        activeCategory = category.equals(activeCategory) ? null : category;
        scheduleLabel.setText(activeCategory != null ? activeCategory : "Schedule");
        renderTimeSlots();
    }

    @FXML private void onClickBtnGeneralWellness()   { setActiveCategory("General Wellness"); }
    @FXML private void onClickBtnWomenHealth()        { setActiveCategory("Women's Health"); }
    @FXML private void onClickBtnSpecializedFields()  { setActiveCategory("Specialized Fields"); }
    @FXML private void onClickBtnDiagnosticsLab()     { setActiveCategory("Diagnostics & Laboratory"); }

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

        boolean isPast = date.isBefore(today);

        if (date.equals(selectedDate))      btn.getStyleClass().add("cal-cell-selected");
        else if (date.equals(today))        btn.getStyleClass().add("cal-cell-today");
        else if (isPast)                    btn.getStyleClass().add("cal-cell-inactive");
        else                                btn.getStyleClass().add("cal-cell");

        // patients can only select today or future dates
        if (!isPast) {
            btn.setOnAction(e -> {
                selectedDate = date;
                renderCalendar();
                updateDateHeader();
                loadServicesForDate(selectedDate, schedType);
                renderTimeSlots();
            });
        } else {
            btn.setMouseTransparent(true);
            btn.setFocusTraversable(false);
        }

        return btn;
    }

    private Label createDayLabel(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }
    private void setQueueSchedule(Service service, String time){
        pickedTime = time;
        pickedDate = selectedDate;
        pickedService = service.getName();

        ScheduleSelectionManager.getInstance().saveSchedule(pickedDate, pickedTime, pickedService);
    }

    private void updateDateHeader() {
        selectedDateLabel.setText(selectedDate.format(DATE_HEADER_FORMATTER));
    }

    private void handlePrevious() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-form.fxml", previousButton, "/styles/queue-form.css", (QueueFormController queueFormController) -> queueFormController.initializeData(schedType));
    }

    private void handleConfirm() {
        if (selectedDate == null || pickedService == null || pickedTime == null) {
            showError("Please select a service!");
            return;
        }
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-review.fxml", confirmButton, "/styles/queue-review.css", (QueueReviewController queueReviewController) -> queueReviewController.initializeData(schedType));
    }
    private void showError(String message){
        error_message.setVisible(true);
        error_container.setVisible(true);
        error_message.setText(message);
        error_message.setStyle("-fx-text-fill: red;");
    }
    private void clearError(){
        error_container.setVisible(false);
        error_message.setVisible(false);
    }

    public LocalDate getSelectedDate() { return selectedDate; }
    public String getSelectedTime() { return pickedTime; }
    public String getSelectedService() { return pickedService; }
}
