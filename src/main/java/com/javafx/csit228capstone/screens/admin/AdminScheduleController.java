package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.screens.schedule.ScheduleDialogController;
import com.javafx.csit228capstone.screens.schedule.ScheduleEditServiceController;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import java.util.function.Consumer;
import javafx.scene.effect.ColorAdjust;

public class AdminScheduleController implements Initializable {

    @FXML private AdminMenuController menuController;

    @FXML private Label screenLabel;
    @FXML private Label selectedDateLabel;
    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private VBox timeSlotsContainer;
    @FXML private VBox legendBox;

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate today;

    private final Map<String, List<Service>> slotServices = new LinkedHashMap<>();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO(DatabaseConfig.getConnection());

    private static final String CLOSING_TIME = "5:00 PM";

    private static final List<String> TIME_SLOTS = List.of("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_HEADER_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        today = LocalDate.now();
        selectedDate = today;
        currentYearMonth = YearMonth.from(today);

        for (String slot : TIME_SLOTS) {
            slotServices.put(slot, new ArrayList<>());
        }

        if (menuController != null) {
            menuController.setActiveButton(menuController.getScheduleBtn());
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
        List<Service> display = new ArrayList<>(services);

        for (int idx = 0; idx < display.size(); idx++) {
            Service svc = display.get(idx);
            HBox chip = buildAdminChip(svc, timeSlot, chipsBox);
            animateChipIn(chip, idx * 30L);
            chipsBox.getChildren().add(chip);
        }

        chipsBox.getChildren().add(buildAddButton(timeSlot, chipsBox));

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

    private HBox buildAdminChip(Service service, String timeSlot, HBox chipsBox) {
        HBox chip = new HBox(4);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());
        chip.setCursor(javafx.scene.Cursor.HAND);

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        Button removeBtn = new Button("×");
        removeBtn.setMnemonicParsing(false);
        removeBtn.getStyleClass().add("chip-remove");
        removeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    scheduleDAO.removeServiceFromSlot(selectedDate, timeSlot, service.getServiceId(), service.isRecurring());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                FadeTransition ft = new FadeTransition(Duration.millis(160), chip);
                ScaleTransition st = new ScaleTransition(Duration.millis(160), chip);
                ft.setToValue(0);
                st.setToX(0.7);
                st.setToY(0.7);
                ParallelTransition out = new ParallelTransition(chip, ft, st);
                out.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent ev) {
                        slotServices.get(timeSlot).remove(service);
                        chipsBox.getChildren().remove(chip);
                    }
                });
                out.play();
            }
        });

        chip.getChildren().addAll(dot, nameLabel, removeBtn);

        chip.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent e) {
                if (e.getTarget() != removeBtn) {
                    showEditServiceDialog(service, timeSlot);
                }
            }
        });

        return chip;
    }

    private Button buildAddButton(String timeSlot, HBox chipsBox) {
        Button addBtn = new Button("+ Add");
        addBtn.getStyleClass().add("add-button");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showAddServiceDialog(timeSlot, chipsBox, addBtn);
            }
        });

        ScaleTransition hoverIn = new ScaleTransition(Duration.millis(110), addBtn);
        hoverIn.setToX(1.07);
        hoverIn.setToY(1.07);
        ScaleTransition hoverOut = new ScaleTransition(Duration.millis(110), addBtn);
        hoverOut.setToX(1.0);
        hoverOut.setToY(1.0);

        addBtn.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent e) {
                hoverIn.playFromStart();
            }
        });

        addBtn.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent e) {
                hoverOut.playFromStart();
            }
        });

        return addBtn;
    }

    private void showEditServiceDialog(Service service, String timeSlot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/schedule/schedule-edit-service.fxml"));
            Parent root = loader.load();

            ScheduleEditServiceController ctrl = loader.getController();
            ctrl.init(service, selectedDate, timeSlot, selectedDateLabel.getText(), scheduleDAO, new Consumer<String>() {
                @Override
                public void accept(String newDoctor) {
                    service.setDoctorName(newDoctor);
                    renderTimeSlots();
                }
            });

            Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(timeSlotsContainer.getScene().getWindow());

            StackPane wrapper = new StackPane(root);
            wrapper.setStyle("-fx-background-color: transparent; -fx-padding: 24px;");

            Scene scene = new Scene(wrapper);
            scene.setFill(Color.TRANSPARENT);

            scene.getStylesheets().add(getClass().getResource("/styles/schedule.css").toExternalForm());
            dialog.setScene(scene);

            root.setTranslateY(-18);
            root.setOpacity(0);
            TranslateTransition tt = new TranslateTransition(Duration.millis(250), root);
            tt.setToY(0);
            tt.setInterpolator(Interpolator.EASE_OUT);
            FadeTransition ft = new FadeTransition(Duration.millis(250), root);
            ft.setToValue(1);
            new ParallelTransition(root, tt, ft).play();

            ColorAdjust dim = new ColorAdjust();
            dim.setBrightness(-0.4);
            timeSlotsContainer.getScene().getRoot().setEffect(dim);
            dialog.setOnHidden(e -> timeSlotsContainer.getScene().getRoot().setEffect(null));

            dialog.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAddServiceDialog(String timeSlot, HBox chipsBox, Button addBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/schedule/schedule-dialog.fxml"));
            Parent root = loader.load();

            ScheduleDialogController ctrl = loader.getController();
            ctrl.init(timeSlot, selectedDateLabel.getText(), selectedDate, scheduleDAO, new Consumer<Service>() {
                @Override
                public void accept(Service service) {
                    slotServices.get(timeSlot).add(service);
                    HBox chip = buildAdminChip(service, timeSlot, chipsBox);
                    int addIndex = chipsBox.getChildren().indexOf(addBtn);
                    chipsBox.getChildren().add(addIndex, chip);
                    animateChipIn(chip, 0);
                }
            });

            Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(timeSlotsContainer.getScene().getWindow());

            StackPane wrapper = new StackPane(root);
            wrapper.setStyle("-fx-background-color: transparent; -fx-padding: 24px;");

            Scene scene = new Scene(wrapper);
            scene.setFill(Color.TRANSPARENT);

            scene.getStylesheets().add(getClass().getResource("/styles/schedule.css").toExternalForm());
            dialog.setScene(scene);

            root.setTranslateY(-18);
            root.setOpacity(0);
            TranslateTransition tt = new TranslateTransition(Duration.millis(250), root);
            tt.setToY(0);
            tt.setInterpolator(Interpolator.EASE_OUT);
            FadeTransition ft = new FadeTransition(Duration.millis(250), root);
            ft.setToValue(1);
            new ParallelTransition(root, tt, ft).play();

            ColorAdjust dim = new ColorAdjust();
            dim.setBrightness(-0.4);
            timeSlotsContainer.getScene().getRoot().setEffect(dim);
            dialog.setOnHidden(e -> timeSlotsContainer.getScene().getRoot().setEffect(null));

            dialog.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        if (date.equals(selectedDate)) {
            btn.getStyleClass().add("cal-cell-selected");
        } else if (date.equals(today)) {
            btn.getStyleClass().add("cal-cell-today");
        } else {
            btn.getStyleClass().add("cal-cell");
        }

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