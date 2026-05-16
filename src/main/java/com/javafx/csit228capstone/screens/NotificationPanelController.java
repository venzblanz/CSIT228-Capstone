package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.model.Notification;
import com.javafx.csit228capstone.utils.NotificationDAO;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationPanelController {

    @FXML
    private final StackPane rootStack;

    private Pane overlay;
    private AnchorPane panel;
    private VBox notifList;
    private Label unreadBadge;
    private Button markAllBtn;
    private Button deleteAllBtn;

    private String activeFilter = "ALL";

    private final int userId = SessionManager.getInstance().getUserId();
    private boolean isOpen = false;

    private static final Duration ANIM        = Duration.millis(280);
    private static final double   PANEL_WIDTH = 440;

    public NotificationPanelController(StackPane rootStack) {
        this.rootStack = rootStack;
        buildOverlay();
        buildPanel();
        rootStack.getStylesheets().add(
                getClass().getResource("/styles/notification-panel.css").toExternalForm()
        );
        rootStack.getChildren().addAll(overlay, panel);
        refresh();
    }

    private void buildOverlay() {
        overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.30);");
        overlay.setVisible(false);
        overlay.setManaged(false);
        overlay.setOnMouseClicked(e -> closePanel());
        overlay.prefWidthProperty().bind(rootStack.widthProperty());
        overlay.prefHeightProperty().bind(rootStack.heightProperty());
        StackPane.setAlignment(overlay, Pos.TOP_LEFT);
    }

    private void buildPanel() {
        panel = new AnchorPane();
        panel.getStyleClass().add("notif-panel");
        panel.setPrefWidth(PANEL_WIDTH);
        panel.setMinWidth(PANEL_WIDTH);
        panel.setMaxWidth(PANEL_WIDTH);
        panel.setTranslateX(PANEL_WIDTH);
        panel.prefHeightProperty().bind(rootStack.heightProperty());
        StackPane.setAlignment(panel, Pos.TOP_RIGHT);

        FontIcon bellIcon = makeFontIcon(FontAwesomeSolid.BELL, 18, "white");

        Label title = new Label("Notifications");
        title.getStyleClass().add("notif-header-title");

        unreadBadge = new Label();
        unreadBadge.getStyleClass().add("notif-badge");
        unreadBadge.setVisible(false);

        HBox titleRow = new HBox(8, bellIcon, title, unreadBadge);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        markAllBtn = new Button("Mark all read");
        markAllBtn.setGraphic(makeFontIcon(FontAwesomeSolid.CHECK_DOUBLE, 12, "white"));
        markAllBtn.getStyleClass().add("notif-action-btn-ghost");
        markAllBtn.setOnAction(e -> markAllRead());

        deleteAllBtn = new Button("Clear all");
        deleteAllBtn.setGraphic(makeFontIcon(FontAwesomeSolid.TRASH, 12, "white"));
        deleteAllBtn.getStyleClass().add("notif-action-btn-ghost");
        deleteAllBtn.setOnAction(e -> deleteAll());

        markAllBtn.setMinWidth(Region.USE_PREF_SIZE);
        deleteAllBtn.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, titleRow, spacer, markAllBtn, deleteAllBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 16, 14, 16));
        header.getStyleClass().add("notif-header");

        Separator sep = new Separator();
        sep.getStyleClass().add("notif-separator");

        HBox filterRow = buildFilterRow();
        filterRow.setPadding(new Insets(10, 16, 10, 16));
        filterRow.getStyleClass().add("notif-filter-row");

        Separator sep2 = new Separator();
        sep2.getStyleClass().add("notif-separator");

        notifList = new VBox(6);
        notifList.setPadding(new Insets(10));
        notifList.getStyleClass().add("notif-list");

        ScrollPane scrollPane = new ScrollPane(notifList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("notif-scroll");

        VBox content = new VBox(header, sep, filterRow, sep2, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);

        panel.getChildren().add(content);
    }

    private HBox buildFilterRow() {
        ToggleGroup group = new ToggleGroup();

        String[][] filters = {
                {"ALL",        "All"},
                {"UNREAD",     "Unread"},
                {"JOINED",     "Joined"},
                {"CANCELLED",  "Cancelled"},
                {"ALMOST_TURN","Alerts"},
        };

        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER_LEFT);

        for (String[] f : filters) {
            String key   = f[0];
            String label = f[1];

            ToggleButton btn = new ToggleButton(label);
            btn.setToggleGroup(group);
            btn.getStyleClass().add("notif-filter-pill");
            if (key.equals("ALL")) btn.setSelected(true);

            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    activeFilter = key;
                    refresh();
                }
            });

            row.getChildren().add(btn);
        }

        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null && oldT != null) oldT.setSelected(true);
        });

        return row;
    }

    public void openPanel() {
        if (isOpen) return;
        isOpen = true;
        overlay.setVisible(true);
        overlay.setManaged(true);
        overlay.toFront();
        panel.toFront();

        TranslateTransition tt = new TranslateTransition(ANIM, panel);
        tt.setToX(0);
        tt.play();
        refresh();
    }

    public void closePanel() {
        if (!isOpen) return;
        isOpen = false;

        TranslateTransition tt = new TranslateTransition(ANIM, panel);
        tt.setToX(PANEL_WIDTH);
        tt.setOnFinished(e -> {
            overlay.setVisible(false);
            overlay.setManaged(false);
        });
        tt.play();
    }

    private void markAllRead() {
        NotificationDAO.markAllRead(userId);
        refresh();
    }

    private void deleteAll() {
        NotificationDAO.deleteAll(userId);
        refresh();
    }

    public void refresh() {
        List<Notification> all = NotificationDAO.getAll(userId);

        long unread = all.stream().filter(n -> !n.isRead()).count();
        unreadBadge.setText(String.valueOf(unread));
        unreadBadge.setVisible(unread > 0);

        List<Notification> filtered = applyFilter(all);
        notifList.getChildren().clear();

        boolean hasAny = !all.isEmpty();
        markAllBtn.setVisible(hasAny);
        deleteAllBtn.setVisible(hasAny);

        if (filtered.isEmpty()) {
            notifList.setAlignment(Pos.TOP_CENTER);
            notifList.getChildren().add(buildEmptyState());
            return;
        }

        notifList.setAlignment(Pos.TOP_LEFT);
        for (Notification n : filtered) {
            notifList.getChildren().add(buildCard(n));
        }
    }

    private List<Notification> applyFilter(List<Notification> list) {
        return switch (activeFilter) {
            case "UNREAD"      -> list.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
            case "JOINED"      -> list.stream().filter(n -> "JOINED".equals(n.getType())).collect(Collectors.toList());
            case "CANCELLED"   -> list.stream().filter(n -> "CANCELLED".equals(n.getType())).collect(Collectors.toList());
            case "ALMOST_TURN" -> list.stream().filter(n -> "ALMOST_TURN".equals(n.getType())).collect(Collectors.toList());
            default            -> list;
        };
    }

    private VBox buildEmptyState() {
        FontIcon icon = makeFontIcon(FontAwesomeSolid.BELL, 32, "#CBD5E1");

        String filterLabel = switch (activeFilter) {
            case "UNREAD"      -> "unread";
            case "JOINED"      -> "joined";
            case "CANCELLED"   -> "cancelled";
            case "ALMOST_TURN" -> "alert";
            default            -> null;
        };

        Label msg = new Label(filterLabel == null
                ? "You're all caught up!"
                : "No " + filterLabel + " notifications");
        msg.getStyleClass().add("notif-empty-label");

        Label sub = new Label("Check back later for updates.");
        sub.getStyleClass().add("notif-empty-sub");

        VBox box = new VBox(8, icon, msg, sub);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(48, 20, 20, 20));
        return box;
    }

    private HBox buildCard(Notification n) {
        Region bar = new Region();
        bar.setMinWidth(5);
        bar.setPrefWidth(5);
        bar.setMaxWidth(5);
        bar.setStyle(
                "-fx-background-color: " + typeColor(n.getType()) + ";" +
                        "-fx-background-radius: 4 0 0 4;"
        );

        FontIcon iconNode = makeFontIcon(typeIcon(n.getType()), 15, typeIconColor(n.getType()));

        StackPane iconBadge = new StackPane(iconNode);
        iconBadge.setMinSize(32, 32);
        iconBadge.setMaxSize(32, 32);
        iconBadge.setStyle(
                "-fx-background-color: " + typeBgColor(n.getType()) + ";" +
                        "-fx-background-radius: 8;"
        );

        Label titleLabel = new Label(n.getTitle());
        titleLabel.getStyleClass().add(n.isRead() ? "notif-card-title-read" : "notif-card-title-unread");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setEllipsisString("…");

        Label msgLabel = new Label(n.getMessage());
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(Double.MAX_VALUE);
        msgLabel.getStyleClass().add("notif-card-msg");

        HBox meta = new HBox(6);
        meta.setAlignment(Pos.CENTER_LEFT);

        if (!n.isRead()) {
            Pane dot = new Pane();
            dot.setMinSize(6, 6);
            dot.setMaxSize(6, 6);
            dot.getStyleClass().add("notif-unread-dot");
            meta.getChildren().add(dot);
        }

        Label time = new Label(formatTime(n.getCreatedAt()));
        time.getStyleClass().add("notif-card-time");
        meta.getChildren().add(time);

        VBox text = new VBox(2, titleLabel, msgLabel, meta);
        text.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(text, Priority.ALWAYS);

        Button del = new Button("x");
        del.getStyleClass().add("notif-delete-x");
        del.setOpacity(0);
        del.setOnAction(e -> {
            NotificationDAO.deleteOne(n.getNotifId());
            refresh();
        });

        HBox body = new HBox(12, iconBadge, text, del);
        body.setAlignment(Pos.CENTER_LEFT);
        body.setPadding(new Insets(11, 10, 11, 12));
        HBox.setHgrow(text, Priority.ALWAYS);

        HBox card = new HBox(bar, body);
        card.setFillHeight(true);
        card.getStyleClass().add(n.isRead() ? "notif-card-read" : "notif-card-unread");

        card.setOnMouseEntered(e -> del.setOpacity(1));
        card.setOnMouseExited(e  -> del.setOpacity(0));

        card.setOnMouseClicked(e -> {
            if (!n.isRead()) {
                NotificationDAO.markRead(n.getNotifId());
                n.setRead(true);
                refresh();
            }
        });

        return card;
    }


    private FontIcon makeFontIcon(FontAwesomeSolid icon, int size, String hexColor) {
        FontIcon fi = new FontIcon(icon);
        fi.setIconSize(size);
        fi.setIconColor(Color.web(hexColor));
        return fi;
    }

    private String formatTime(LocalDateTime createdAt) {
        if (createdAt == null) return "";
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(createdAt, now).toMinutes();
        if (minutes < 1)  return "Just now";
        if (minutes < 60) return minutes + " min ago";
        long hours = minutes / 60;
        if (hours < 24)   return hours + " hr ago";
        return createdAt.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"));
    }

    private String typeColor(String type) {
        return switch (type) {
            case "JOINED"      -> "#5DAE52";
            case "CANCELLED"   -> "#D82F2F";
            case "ALMOST_TURN" -> "#218AD5";
            default            -> "#94A3B8";
        };
    }

    private String typeBgColor(String type) {
        return switch (type) {
            case "JOINED"      -> "#EAFAF0";
            case "CANCELLED"   -> "#FEF2F2";
            case "ALMOST_TURN" -> "#EBF4FD";
            default            -> "#F1F5F9";
        };
    }

    private String typeIconColor(String type) {
        return switch (type) {
            case "JOINED"      -> "#5DAE52";
            case "CANCELLED"   -> "#D82F2F";
            case "ALMOST_TURN" -> "#218AD5";
            default            -> "#64748B";
        };
    }

    private FontAwesomeSolid typeIcon(String type) {
        return switch (type) {
            case "JOINED"      -> FontAwesomeSolid.USER_CHECK;
            case "CANCELLED"   -> FontAwesomeSolid.CALENDAR_TIMES;
            case "ALMOST_TURN" -> FontAwesomeSolid.BELL;
            default            -> FontAwesomeSolid.BULLHORN;
        };
    }
}