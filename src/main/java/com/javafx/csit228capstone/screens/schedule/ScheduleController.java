package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.effect.ColorAdjust;

public class ScheduleController implements Initializable {

    @FXML private com.javafx.csit228capstone.helper.MenuController menuController;
    @FXML private Label screenLabel;
    @FXML private Label selectedDateLabel;
    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;
    @FXML private TextField searchField;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private VBox timeSlotsContainer;

    @FXML private HBox categoryFilterBar;
    @FXML private Button btnGeneralWellness;
    @FXML private Button btnWomensHealth;
    @FXML private Button btnSpecializedFields;
    @FXML private Button btnDiagnosticsLab;

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate today;
    private String activeCategory = null;

    private final Map<String, List<Service>> slotServices = new LinkedHashMap<>();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO(DatabaseConfig.getConnection());

    private static final String CLOSING_TIME = "5:00 PM";

    private static final List<String> TIME_SLOTS = List.of("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_HEADER_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menuController != null) {
            menuController.setActiveButton(menuController.getScheduleBtn());
        }

        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        prevMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                currentYearMonth = currentYearMonth.minusMonths(1);
                renderCalendar();
            }
        });

        nextMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                currentYearMonth = currentYearMonth.plusMonths(1);
                renderCalendar();
            }
        });

        if (searchField != null) {
            searchField.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
                @Override
                public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String o, String n) {
                    handleSearch(n);
                }
            });
        }

        renderCalendar();
        updateDateHeader();
        loadServicesForDate(selectedDate);
        renderTimeSlots();

        AnimationHelper.fadeIn(timeSlotsContainer);
    }

    private void loadServicesForDate(LocalDate date) {
        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }
        try {
            Map<String, List<Service>> scheduleMap = scheduleDAO.getScheduleForDate(date);
            for (Map.Entry<String, List<Service>> entry : scheduleMap.entrySet()) {
                slotServices.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderTimeSlots() {
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

    private HBox buildTimeRow(String timeSlot) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("time-row");
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setMinHeight(52);

        Label timeLabel = new Label(timeSlot);
        timeLabel.setMinWidth(65);
        timeLabel.getStyleClass().add("time-label");

        if (timeSlot.equals(CLOSING_TIME)) {
            Label closedLabel = new Label("Closed");
            closedLabel.getStyleClass().add("closed-label");
            row.getChildren().addAll(timeLabel, closedLabel);
            return row;
        }

        HBox chipsBox = new HBox(8);
        chipsBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(chipsBox, Priority.ALWAYS);

        List<Service> services = slotServices.getOrDefault(timeSlot, List.of());
        List<Service> display = new ArrayList<>();

        if (activeCategory != null) {
            for (Service s : services) {
                if (s.getServiceType().equals(activeCategory)) {
                    display.add(s);
                }
            }
        } else {
            display.addAll(services);
        }

        if (display.isEmpty()) {
            Label dash = new Label("—");
            dash.getStyleClass().add("closed-label");
            chipsBox.getChildren().add(dash);
        } else {
            for (int idx = 0; idx < display.size(); idx++) {
                Service svc = display.get(idx);
                HBox chip = buildPatientChip(svc);
                animateChipIn(chip, idx * 30L);
                chipsBox.getChildren().add(chip);
            }
        }

        row.getChildren().addAll(timeLabel, chipsBox);
        return row;
    }

    private void animateChipIn(HBox chip, long delayMs) {
        chip.setScaleX(0.75);
        chip.setScaleY(0.75);
        chip.setOpacity(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), chip);
        st.setToX(1);
        st.setToY(1);
        st.setDelay(Duration.millis(delayMs));
        st.setInterpolator(Interpolator.EASE_OUT);
        FadeTransition ft = new FadeTransition(Duration.millis(200), chip);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        new ParallelTransition(chip, st, ft).play();
    }

    private HBox buildPatientChip(Service service) {
        HBox chip = new HBox(6);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());
        chip.setCursor(javafx.scene.Cursor.HAND);

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        chip.getChildren().addAll(dot, nameLabel);

        chip.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent e) {
                showDoctorPopup(service, chip);
            }
        });

        HBox outer = new HBox();
        outer.getChildren().add(chip);
        return outer;
    }

    private void showDoctorPopup(Service service, javafx.scene.Node chipNode) {
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(timeSlotsContainer.getScene().getWindow());

        Label serviceName = new Label(service.getName());
        serviceName.getStyleClass().add("dialog-title");

        Label doctorLabel = new Label(service.getDoctorDisplay());
        doctorLabel.getStyleClass().add("dialog-subtitle");

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("dialog-cancel-btn");
        closeBtn.setCursor(javafx.scene.Cursor.HAND);
        closeBtn.setOnAction(e -> popup.close());

        VBox content = new VBox(14);
        content.getChildren().addAll(serviceName, doctorLabel, closeBtn);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(24));
        content.getStyleClass().add("dialog-root");
        content.setMinWidth(280);

        StackPane rootPane = new StackPane(content);
        rootPane.setStyle("-fx-background-color: transparent; -fx-padding: 20px;");

        Scene scene = new Scene(rootPane);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/styles/schedule.css").toExternalForm());
        popup.setScene(scene);

        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(-0.4);
        timeSlotsContainer.getScene().getRoot().setEffect(dim);
        popup.setOnHidden(e -> timeSlotsContainer.getScene().getRoot().setEffect(null));

        rootPane.setOpacity(0);
        popup.show();

        javafx.geometry.Point2D chipPos = chipNode.localToScreen(0, 0);
        if (chipPos != null) {
            double popupWidth = popup.getWidth();
            double popupHeight = popup.getHeight();
            double chipWidth = chipNode.getBoundsInLocal().getWidth();

            popup.setX(chipPos.getX() + (chipWidth / 2) - (popupWidth / 2));
            popup.setY(chipPos.getY() - popupHeight + 5);
        }

        content.setTranslateY(15);
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), content);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(250), rootPane);
        ft.setToValue(1.0);

        new ParallelTransition(tt, ft).play();
    }

    private void setActiveCategory(String category, Button clicked, String baseStyle, String activeStyle) {
        boolean turningOn = !category.equals(activeCategory);
        activeCategory = turningOn ? category : null;

        resetFilterButtons();

        if (turningOn) {
            clicked.getStyleClass().remove(baseStyle);
            if (!clicked.getStyleClass().contains(activeStyle)) {
                clicked.getStyleClass().add(activeStyle);
            }
            if (screenLabel != null) {
                screenLabel.setText(activeCategory);
            }

            ScaleTransition pulse = new ScaleTransition(Duration.millis(130), clicked);
            pulse.setFromX(0.88);
            pulse.setFromY(0.88);
            pulse.setToX(1.0);
            pulse.setToY(1.0);
            pulse.setInterpolator(Interpolator.EASE_OUT);
            pulse.play();
        } else {
            if (screenLabel != null) {
                screenLabel.setText("Schedule");
            }
        }

        renderTimeSlots();
    }

    private void resetFilterButtons() {
        swapStyle(btnGeneralWellness, "filter-btn-blue-active", "filter-btn-blue");
        swapStyle(btnWomensHealth, "filter-btn-pink-active", "filter-btn-pink");
        swapStyle(btnSpecializedFields, "filter-btn-green-active", "filter-btn-green");
        swapStyle(btnDiagnosticsLab, "filter-btn-purple-active", "filter-btn-purple");
    }

    private void swapStyle(Button btn, String remove, String add) {
        if (btn != null) {
            btn.getStyleClass().remove(remove);
            if (!btn.getStyleClass().contains(add)) {
                btn.getStyleClass().add(add);
            }
        }
    }

    @FXML
    private void onClickBtnGeneralWellness() {
        setActiveCategory("General Wellness", btnGeneralWellness, "filter-btn-blue", "filter-btn-blue-active");
    }

    @FXML
    private void onClickBtnWomensHealth() {
        setActiveCategory("Women's Health", btnWomensHealth, "filter-btn-pink", "filter-btn-pink-active");
    }

    @FXML
    private void onClickBtnSpecializedFields() {
        setActiveCategory("Specialized Fields", btnSpecializedFields, "filter-btn-green", "filter-btn-green-active");
    }

    @FXML
    private void onClickBtnDiagnosticsLab() {
        setActiveCategory("Diagnostics & Laboratory", btnDiagnosticsLab, "filter-btn-purple", "filter-btn-purple-active");
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            for (javafx.scene.Node n : timeSlotsContainer.getChildren()) {
                n.setVisible(true);
                n.setManaged(true);
            }
            return;
        }

        String lower = query.toLowerCase();

        for (javafx.scene.Node node : timeSlotsContainer.getChildren()) {
            if (!(node instanceof HBox)) {
                continue;
            }

            HBox row = (HBox) node;
            boolean matches = false;

            if (row.getChildren().size() > 1 && row.getChildren().get(1) instanceof HBox) {
                HBox chipsBox = (HBox) row.getChildren().get(1);

                matches = searchNodeForText(chipsBox, lower);
            }

            node.setVisible(matches);
            node.setManaged(matches);
        }
    }

    private boolean searchNodeForText(javafx.scene.Node node, String query) {
        if (node instanceof Label) {
            Label lbl = (Label) node;
            if (lbl.getText() != null && lbl.getText().toLowerCase().contains(query)) {
                return true;
            }
        } else if (node instanceof javafx.scene.Parent) {
            for (javafx.scene.Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                if (searchNodeForText(child, query)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void renderCalendar() {
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

    private Button createDayButton(int day, LocalDate date) {
        Button btn = new Button(String.valueOf(day));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER);

        boolean isPast = date.isBefore(today);

        if (date.equals(selectedDate)) {
            btn.getStyleClass().add("cal-cell-selected");
        } else if (date.equals(today)) {
            btn.getStyleClass().add("cal-cell-today");
        } else if (isPast) {
            btn.getStyleClass().add("cal-cell-inactive");
        } else {
            btn.getStyleClass().add("cal-cell");
        }

        if (isPast) {
            btn.setDisable(true);
        } else {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    selectedDate = date;
                    renderCalendar();
                    updateDateHeader();
                    loadServicesForDate(selectedDate);
                    renderTimeSlots();
                }
            });
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

    private void updateDateHeader() {
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