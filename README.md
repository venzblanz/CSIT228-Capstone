# MedServe - Public Health Center Queue Management System

## Contributors
- Alexa Christel Baniasia
- Venz Virni Blanza
- Juner John Caimor
- Rea Emerald Sabellita
- Sarah Mae Sario

## About MedServe
MedServe is a JavaFX desktop application that streamlines public health center operations by digitizing the queue and appointment process. It addresses long waiting times and missed updates by allowing patients to join queues online, monitor their real-time position, and receive status updates without unnecessary on-site waiting. The system supports priority queueing for PWD, pregnant, and senior patients following a 2-priority to 1-regular serving rule. Staff can manage and call queue numbers through an admin dashboard, while patients can view doctor and service availability through a schedule screen before joining a queue.

## Implemented Features
- Login / Register with session management
- Patient account and profile management
- Department-based queueing across General Wellness, Women's Health, Specialized Fields, and Diagnostics and Laboratory
- Priority queueing system (PWD, Pregnant, Senior patients served ahead of regular)
- Real-time queue position tracking with estimated waiting time
- Queue status updates (Waiting, Serving, Done, Cancelled)
- Service and doctor schedule viewer with date picker and category filters
- Admin live queue monitor with auto-refresh and department tabs
- Admin queue management with Call Number, Complete, and Cancel controls
- Queue information detail screen showing full patient and appointment data
- Auto-cancellation of unserved queues from past dates

## Technologies Used
- Java 21
- JavaFX 25 with FXML
- JDBC with MySQL
- CSS for UI styling

## Design Patterns Used
- **Singleton** — `DatabaseConfig`, `SessionManager`, `SceneNavigator`, `FormManager`, `ScheduleDAO`, `ScheduleSelectionManager`
- **DAO Pattern** — `QueueLineDAO`, `QueueFormDAO` for all database operations
- **MVC** — FXML controllers separated from model and data access layers

## OOP Concepts Applied
- **Encapsulation** — model classes such as `QueueTicket`, `QueueInformation`, `QueueInsertValue`, and `Service` with private fields and getters
- **Abstraction** — DAO layer abstracts all database operations from the controller logic
- **Polymorphism** — scene navigation using generic controller callbacks via `SceneNavigator`
