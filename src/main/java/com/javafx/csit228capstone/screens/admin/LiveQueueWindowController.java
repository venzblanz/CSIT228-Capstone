package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LiveQueueWindowController {

    @FXML private Button tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized;
    @FXML private Label nowServingNumber, nowServingDept, nowServingStaff;
    @FXML private VBox nowServingCard;
    @FXML private FlowPane nowServingMultiCards;
    @FXML private FlowPane waitingCards, doneCards, cancelledCards;
    @FXML private Label refreshLabel;

    private String currentDept = "All";
    private Timeline autoRefresh;

    private record QueueCard(String number, String subtitle, boolean isActive) {}
    private record ServingCard(String dept, String number) {}

    @FXML
    private void initialize() {
        loadQueue();
        autoRefresh = new Timeline(new KeyFrame(Duration.seconds(10), e -> loadQueue()));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    public void shutdown() {
        if (autoRefresh != null) autoRefresh.stop();
    }

    private void loadQueue() {
        String dept = currentDept;

        Task<Void> task = new Task<>() {
            String servingNumber = "";
            String servingDept   = "No one currently being served";
            String servingStaff  = "";

            final List<ServingCard> servingAllList = new ArrayList<>();
            final List<QueueCard>   waitingList    = new ArrayList<>();
            final List<QueueCard>   doneList       = new ArrayList<>();
            final List<QueueCard>   cancelledList  = new ArrayList<>();  // FIXED — moved inside Task

            @Override
            protected Void call() {
                if (dept.equals("All")) {
                    String[] departments = {
                            "Diagnostics and Laboratory",
                            "Women's Health",
                            "General Wellness",
                            "Specialized Fields"
                    };
                    String servingSql = "SELECT queue_number FROM queue_line " +
                            "WHERE status = 'Serving' AND department = ? " +
                            "ORDER BY created_at DESC LIMIT 1";
                    for (String d : departments) {
                        try (Connection c = DatabaseConfig.getConnection();
                             PreparedStatement ps = c.prepareStatement(servingSql)) {
                            ps.setString(1, d);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                servingAllList.add(new ServingCard(d, rs.getString("queue_number")));
                            } else {
                                servingAllList.add(new ServingCard(d, "---"));
                            }
                        } catch (Exception e) {
                            System.err.println("[LiveQueue] Serving (All) error: " + e.getMessage());
                        }
                    }
                } else {
                    String servingSql = "SELECT queue_number, department " +
                            "FROM queue_line WHERE status = 'Serving' AND department = ? " +
                            "ORDER BY created_at DESC LIMIT 1";
                    try (Connection c = DatabaseConfig.getConnection();
                         PreparedStatement ps = c.prepareStatement(servingSql)) {
                        ps.setString(1, dept);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            servingNumber = rs.getString("queue_number");
                            servingDept   = rs.getString("department");
                        } else {
                            servingDept = "No one currently being served";
                        }
                    } catch (Exception e) {
                        System.err.println("[LiveQueue] Serving error: " + e.getMessage());
                    }
                }

                // Waiting
                String waitingSql = "SELECT queue_number, department FROM queue_line " +
                        "WHERE status = 'Waiting'" +
                        (dept.equals("All") ? "" : " AND department = ?") +
                        " ORDER BY created_at ASC";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(waitingSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        waitingList.add(new QueueCard(
                                rs.getString("queue_number"),
                                rs.getString("department"),
                                true));
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Waiting error: " + e.getMessage());
                }

                // Completed
                String completedSql = "SELECT queue_number FROM queue_line " +
                        "WHERE status = 'Completed'" +
                        (dept.equals("All") ? "" : " AND department = ?") +
                        " ORDER BY created_at DESC LIMIT 10";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(completedSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        doneList.add(new QueueCard(rs.getString("queue_number"), "Completed", false));
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Completed error: " + e.getMessage());
                }

                // Cancelled
                String cancelledSql = "SELECT queue_number FROM queue_line " +
                        "WHERE status = 'Cancelled'" +
                        (dept.equals("All") ? "" : " AND department = ?") +
                        " ORDER BY created_at DESC LIMIT 10";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(cancelledSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        cancelledList.add(new QueueCard(rs.getString("queue_number"), "Cancelled", false));
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Cancelled error: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void succeeded() {
                if (dept.equals("All")) {
                    nowServingCard.setVisible(false);
                    nowServingCard.setManaged(false);
                    nowServingMultiCards.setVisible(true);
                    nowServingMultiCards.setManaged(true);
                    nowServingMultiCards.getChildren().clear();
                    for (ServingCard sc : servingAllList)
                        nowServingMultiCards.getChildren().add(createServingCard(sc.dept(), sc.number()));
                } else {
                    nowServingMultiCards.setVisible(false);
                    nowServingMultiCards.setManaged(false);
                    nowServingCard.setVisible(true);
                    nowServingCard.setManaged(true);
                    nowServingNumber.setText(servingNumber);
                    nowServingDept.setText(servingDept);
                    nowServingDept.setMaxWidth(Double.MAX_VALUE);
                    nowServingDept.setAlignment(Pos.CENTER);
                    nowServingStaff.setText("");
                }

                // Waiting
                waitingCards.getChildren().clear();
                if (waitingList.isEmpty()) {
                    Label none = new Label("No waiting patients");
                    none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                    waitingCards.getChildren().add(none);
                } else {
                    for (QueueCard card : waitingList)
                        waitingCards.getChildren().add(createCard(card.number(), card.subtitle(), true));
                }

                // Completed
                doneCards.getChildren().clear();
                if (doneList.isEmpty()) {
                    Label none = new Label("No completed entries");
                    none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                    doneCards.getChildren().add(none);
                } else {
                    for (QueueCard card : doneList)
                        doneCards.getChildren().add(createCard(card.number(), "Completed", false));
                }

                // Cancelled
                cancelledCards.getChildren().clear();
                if (cancelledList.isEmpty()) {
                    Label none = new Label("No cancelled entries");
                    none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                    cancelledCards.getChildren().add(none);
                } else {
                    for (QueueCard card : cancelledList)
                        cancelledCards.getChildren().add(createCard(card.number(), "Cancelled", false));
                }

                refreshLabel.setText("🔄 Last refreshed: just now");
            }

            @Override
            protected void failed() {
                System.err.println("[LiveQueue] Task failed: " + getException().getMessage());
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private VBox createServingCard(String dept, String number) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(6);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 5); " +
                "-fx-padding: 25px 40px; -fx-min-width: 240px; " +
                "-fx-border-color: #2b78e4 transparent transparent transparent; " +
                "-fx-border-width: 6px 0 0 0; -fx-border-radius: 12px;");

        String shortDept = dept.equals("Diagnostics and Laboratory") ? "Diagnostics & Lab" : dept;

        Label nowServingLabel = new Label("NOW SERVING");
        nowServingLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b; -fx-font-weight: bold;");

        Label numberLabel = new Label(number);
        numberLabel.setStyle(number.equals("---")
                ? "-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #cbd5e1;"
                : "-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");

        Label deptLabel = new Label(shortDept);
        deptLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2b78e4; -fx-font-weight: bold;");

        card.getChildren().addAll(nowServingLabel, numberLabel, deptLabel);
        return card;
    }

    private VBox createCard(String number, String subtitle, boolean isActive) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(4);
        Label numberLabel   = new Label(number);
        Label subtitleLabel = new Label(subtitle);

        if (isActive) {
            card.getStyleClass().add("queue-card");
            numberLabel.getStyleClass().add("queue-card-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        } else if (subtitle.equals("Completed")) {
            card.getStyleClass().add("queue-card-completed");
            numberLabel.getStyleClass().add("queue-card-completed-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        } else {
            card.getStyleClass().add("queue-card-cancelled");
            numberLabel.getStyleClass().add("queue-card-cancelled-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        }

        card.getChildren().addAll(numberLabel, subtitleLabel);
        return card;
    }

    @FXML private void onTabAll()         { currentDept = "All";                        setActiveTab(tabAll);         loadQueue(); }
    @FXML private void onTabDiag()        { currentDept = "Diagnostics and Laboratory";  setActiveTab(tabDiag);        loadQueue(); }
    @FXML private void onTabWomens()      { currentDept = "Women's Health";              setActiveTab(tabWomens);      loadQueue(); }
    @FXML private void onTabGeneral()     { currentDept = "General Wellness";            setActiveTab(tabGeneral);     loadQueue(); }
    @FXML private void onTabSpecialized() { currentDept = "Specialized Fields";          setActiveTab(tabSpecialized); loadQueue(); }

    private void setActiveTab(Button active) {
        for (Button b : new Button[]{tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized})
            b.getStyleClass().setAll("queue-tab");
        active.getStyleClass().setAll("queue-tab-active");
    }
}