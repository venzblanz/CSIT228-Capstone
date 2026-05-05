-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 05, 2026 at 10:34 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `javacapstone`
--

-- --------------------------------------------------------

--
-- Table structure for table `queue_form`
--

CREATE TABLE `queue_form` (
  `form_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `first_name` varchar(20) NOT NULL,
  `middle_initial` varchar(4) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `age` int(11) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `purpose` varchar(100) NOT NULL,
  `symptoms` varchar(255) NOT NULL,
  `patient_type` varchar(40) NOT NULL,
  `contact_number` varchar(30) NOT NULL,
  `form_type` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `queue_form`
--

INSERT INTO `queue_form` (`form_id`, `user_id`, `first_name`, `middle_initial`, `last_name`, `age`, `gender`, `purpose`, `symptoms`, `patient_type`, `contact_number`, `form_type`) VALUES
(1, 2, 'Venz Virni', 'T', 'Blanza', 20, 'Male', 'X-ray', 'Hiwig Tiil', 'Person with Disability', '09919106710', 'Diagnostics and Laboratory'),
(2, 2, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Postnatal Care', 'SSS', 'Regular', '09233211123', 'Women\'s Health'),
(3, 2, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(4, 2, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(5, 2, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(6, 2, 'Venz Virni', 'T', 'Blanza', 20, 'Male', 'Check-up', 'SDASD', 'Person with Disability', '09671103230', 'General Wellness'),
(7, 2, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Family Planning Consultation', 'Ambot uroy', 'Regular', '09919106710', 'Women\'s Health'),
(8, 2, 'Juner John', 'V', 'Caimor', 21, 'Male', 'ENT (Ear, Nose, Throat)', 'Runny', 'Regular', '0988231202', 'Specialized Fields'),
(9, 2, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Breast Examination', 'WOW', 'Regular', '23123123123123', 'Women\'s Health'),
(10, 2, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', 'Pregnant', 'Pregnant', '09919106710', 'Women\'s Health');

-- --------------------------------------------------------

--
-- Table structure for table `queue_line`
--

CREATE TABLE `queue_line` (
  `queue_id` int(11) NOT NULL,
  `form_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `department` varchar(100) NOT NULL,
  `queue_number` varchar(20) NOT NULL,
  `status` varchar(30) DEFAULT 'Waiting',
  `created_at` datetime DEFAULT current_timestamp(),
  `staff_assigned` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `queue_line`
--

INSERT INTO `queue_line` (`queue_id`, `form_id`, `user_id`, `department`, `queue_number`, `status`, `created_at`, `staff_assigned`) VALUES
(1, 1, 2, 'Diagnostics and Laboratory', 'D-01', 'Completed', '2026-05-04 19:54:32', 'Dr. Shaun Murphy'),
(2, 2, 2, 'Women\'s Health', 'W-01', 'Cancelled', '2026-05-04 20:00:31', 'Dr. Gregory House'),
(3, 5, 2, 'Women\'s Health', 'W-02', 'Waiting', '2026-05-04 21:08:45', ''),
(4, 6, 2, 'General Wellness', 'G-01', 'Waiting', '2026-05-04 21:11:34', ''),
(5, 7, 2, 'Women\'s Health', 'W-03', 'Waiting', '2026-05-04 21:19:51', ''),
(6, 8, 2, 'Specialized Fields', 'S-01', 'Waiting', '2026-05-04 21:22:02', ''),
(7, 9, 2, 'Women\'s Health', 'W-04', 'Waiting', '2026-05-04 21:33:58', ''),
(8, 10, 2, 'Women\'s Health', 'W-01', 'Waiting', '2026-05-05 13:05:44', '');

-- --------------------------------------------------------

--
-- Table structure for table `schedules`
--

CREATE TABLE `schedules` (
  `schedule_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `schedule_date` date NOT NULL,
  `time_slot` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `schedules`
--

INSERT INTO `schedules` (`schedule_id`, `service_id`, `schedule_date`, `time_slot`) VALUES
(1, 1, '2026-05-05', '8:00 AM'),
(2, 1, '2026-05-05', '9:00 AM'),
(3, 1, '2026-05-05', '10:00 AM'),
(4, 1, '2026-05-05', '1:00 PM'),
(5, 1, '2026-05-05', '2:00 PM'),
(6, 2, '2026-05-05', '8:00 AM'),
(7, 2, '2026-05-05', '11:00 AM'),
(8, 2, '2026-05-05', '3:00 PM'),
(9, 3, '2026-05-05', '9:00 AM'),
(10, 3, '2026-05-05', '10:00 AM'),
(11, 3, '2026-05-05', '2:00 PM'),
(12, 4, '2026-05-05', '9:00 AM'),
(13, 4, '2026-05-05', '11:00 AM'),
(14, 4, '2026-05-05', '1:00 PM'),
(15, 5, '2026-05-05', '10:00 AM'),
(16, 5, '2026-05-05', '2:00 PM'),
(17, 5, '2026-05-05', '3:00 PM'),
(18, 6, '2026-05-05', '8:00 AM'),
(19, 6, '2026-05-05', '1:00 PM'),
(20, 6, '2026-05-05', '4:00 PM');

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `service_id` int(11) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `service_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`service_id`, `service_name`, `service_type`) VALUES
(1, 'Check-up', 'General Wellness'),
(2, 'Vaccination', 'General Wellness'),
(3, 'Postnatal Care', 'Women\'s Health'),
(4, 'Pediatrics', 'Specialized Fields'),
(5, 'Dental', 'Specialized Fields'),
(6, 'X-Ray', 'Diagnostics & Laboratory');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `mobile_number` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('patient','admin') DEFAULT 'patient',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `mobile_number`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'Juner John Caimor', '912345678', 'caimorjuner@gmail.com', '123123', 'patient', '2026-05-01 11:16:19'),
(2, 'Admin', '12345678900', 'admin@admin.com', '123456', 'admin', '2026-05-04 03:47:37');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `queue_form`
--
ALTER TABLE `queue_form`
  ADD PRIMARY KEY (`form_id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `queue_line`
--
ALTER TABLE `queue_line`
  ADD PRIMARY KEY (`queue_id`),
  ADD KEY `fk_form_id` (`form_id`),
  ADD KEY `fk1_user_id` (`user_id`);

--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `service_id` (`service_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`service_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `queue_form`
--
ALTER TABLE `queue_form`
  MODIFY `form_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `queue_line`
--
ALTER TABLE `queue_line`
  MODIFY `queue_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `schedules`
--
ALTER TABLE `schedules`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `service_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `queue_form`
--
ALTER TABLE `queue_form`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `queue_line`
--
ALTER TABLE `queue_line`
  ADD CONSTRAINT `fk1_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `fk_form_id` FOREIGN KEY (`form_id`) REFERENCES `queue_form` (`form_id`);

--
-- Constraints for table `schedules`
--
ALTER TABLE `schedules`
  ADD CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
