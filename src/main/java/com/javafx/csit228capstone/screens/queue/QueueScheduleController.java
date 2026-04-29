package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class QueueScheduleController implements Initializable{

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
    private String schedType;

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_HEADER_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, MMMM d");
    private static final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    public void initializeData(String type){
        schedType = type;
        if(type.equals("General Wellness")){
            scheduleLabel.setText("General Wellness Schedule");
        }else if (type.equals("Women's Health")){
            scheduleLabel.setText("Women's Health Schedule");
        }else if (type.equals("Specialized Fields")){
            scheduleLabel.setText("Specialized Fields Schedule");
        }else{
            scheduleLabel.setText("Diagnostics and Laboratory Schedule");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        renderCalendar();
        updateDateHeader();

        prevMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            renderCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            renderCalendar();
        });

        previousButton.setOnAction(e -> handlePrevious());
        confirmButton.setOnAction(e -> handleConfirm());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch(newVal));
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
            setStyle(btn);
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

    private void setStyle(Button button) {
        button.setPrefSize(38,38);
        button.setMaxSize(38,38);
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

    private void handlePrevious() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-form.fxml", previousButton, "/styles/queue-form.css", (QueueFormController queueFormController) -> queueFormController.initializeData(schedType));
    }

    private void handleConfirm() {
        if (selectedDate == null) { return; }
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-review.fxml", confirmButton, "/styles/queue-review.css", (QueueReviewController queueReviewController) -> queueReviewController.initializeData(schedType));
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            timeSlotsContainer.getChildren().forEach(node -> node.setVisible(true));
            return;
        }
        String lower = query.toLowerCase();
        timeSlotsContainer.getChildren().forEach(node -> {
            if (node instanceof javafx.scene.layout.HBox hbox) {
                boolean matches = hbox.getChildren().stream().anyMatch(child -> {
                    if (child instanceof VBox vbox) {
                        return vbox.getChildren().stream().anyMatch(inner -> {
                            if (inner instanceof Label lbl) {
                                return lbl.getText().toLowerCase().contains(lower);
                            }
                            return false;
                        });
                    }
                    return false;
                });
                node.setVisible(matches || !hasSlotCard(hbox));
                node.setManaged(matches || !hasSlotCard(hbox));
            }
        });
    }

    private boolean hasSlotCard(javafx.scene.layout.HBox hbox) {
        return hbox.getChildren().stream().anyMatch(c -> c instanceof VBox);
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}