-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 02, 2026 at 05:30 PM
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
-- Table structure for table `queue_history`
--

CREATE TABLE `queue_history` (
  `id` int(11) NOT NULL,
  `queue_number` int(11) NOT NULL,
  `service` varchar(100) NOT NULL,
  `status` varchar(100) NOT NULL,
  `queue_date` date NOT NULL,
  `department` varchar(100) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `staff_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `queue_history`
--

INSERT INTO `queue_history` (`id`, `queue_number`, `service`, `status`, `queue_date`, `department`, `patient_id`, `staff_name`) VALUES
(1, 101, 'General Consultation', 'Completed', '2026-04-15', 'General Wellness', 1, 'Dr. Smith'),
(2, 102, 'Blood Test', 'Pending', '2026-05-01', 'Diagnostics & Laboratory', 1, 'Nurse Joy'),
(3, 102, 'Blood Test', 'Pending', '2026-05-01', 'Diagnostics & Laboratory', 1, 'Nurse Joy');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
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

INSERT INTO `users` (`id`, `full_name`, `mobile_number`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'Juner John Caimor', '912345678', 'caimorjuner@gmail.com', '123123', 'patient', '2026-05-01 11:16:19');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `queue_history`
--
ALTER TABLE `queue_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_patient` (`patient_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `queue_history`
--
ALTER TABLE `queue_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `queue_history`
--
ALTER TABLE `queue_history`
  ADD CONSTRAINT `fk_patient` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
