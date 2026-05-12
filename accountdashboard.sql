-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 09, 2026 at 07:52 AM
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
-- Database: `accountdashboard`
--

-- --------------------------------------------------------

--
-- Table structure for table `expenses`
--

CREATE TABLE `expenses` (
  `id` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) NOT NULL,
  `category` varchar(50) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `expenses`
--

INSERT INTO `expenses` (`id`, `year`, `month`, `category`, `amount`) VALUES
(1, 2026, 'January', 'Food & Beverages', 450),
(2, 2026, 'January', 'Entertainment', 100),
(3, 2026, 'January', 'Leisure & Sports', 80),
(4, 2026, 'January', 'Services', 300),
(5, 2026, 'January', 'Shopping', 200),
(6, 2026, 'January', 'Telecom', 50),
(7, 2026, 'January', 'Utilities', 80),
(8, 2026, 'February', 'Food & Beverages', 390),
(9, 2026, 'February', 'Entertainment', 80),
(10, 2026, 'February', 'Leisure & Sports', 70),
(11, 2026, 'February', 'Services', 250),
(12, 2026, 'February', 'Shopping', 450),
(13, 2026, 'February', 'Telecom', 50),
(14, 2026, 'February', 'Utilities', 70),
(15, 2026, 'March', 'Food & Beverages', 470),
(16, 2026, 'March', 'Entertainment', 110),
(17, 2026, 'March', 'Leisure & Sports', 90),
(18, 2026, 'March', 'Services', 280),
(19, 2026, 'March', 'Shopping', 320),
(20, 2026, 'March', 'Telecom', 60),
(21, 2026, 'March', 'Utilities', 85),
(22, 2026, 'April', 'Food & Beverages', 510),
(23, 2026, 'April', 'Entertainment', 150),
(24, 2026, 'April', 'Leisure & Sports', 120),
(25, 2026, 'April', 'Services', 310),
(26, 2026, 'April', 'Shopping', 280),
(27, 2026, 'April', 'Telecom', 55),
(28, 2026, 'April', 'Utilities', 90),
(29, 2026, 'May', 'Food & Beverages', 430),
(30, 2026, 'May', 'Entertainment', 95),
(31, 2026, 'May', 'Leisure & Sports', 100),
(32, 2026, 'May', 'Services', 260),
(33, 2026, 'May', 'Shopping', 190),
(34, 2026, 'May', 'Telecom', 55),
(35, 2026, 'May', 'Utilities', 75),
(36, 2026, 'June', 'Food & Beverages', 480),
(37, 2026, 'June', 'Entertainment', 120),
(38, 2026, 'June', 'Leisure & Sports', 140),
(39, 2026, 'June', 'Services', 290),
(40, 2026, 'June', 'Shopping', 350),
(41, 2026, 'June', 'Telecom', 60),
(42, 2026, 'June', 'Utilities', 80),
(43, 2026, 'July', 'Food & Beverages', 500),
(44, 2026, 'July', 'Entertainment', 160),
(45, 2026, 'July', 'Leisure & Sports', 200),
(46, 2026, 'July', 'Services', 300),
(47, 2026, 'July', 'Shopping', 400),
(48, 2026, 'July', 'Telecom', 65),
(49, 2026, 'July', 'Utilities', 85),
(50, 2026, 'August', 'Food & Beverages', 460),
(51, 2026, 'August', 'Entertainment', 105),
(52, 2026, 'August', 'Leisure & Sports', 110),
(53, 2026, 'August', 'Services', 270),
(54, 2026, 'August', 'Shopping', 230),
(55, 2026, 'August', 'Telecom', 55),
(56, 2026, 'August', 'Utilities', 78),
(57, 2026, 'September', 'Food & Beverages', 420),
(58, 2026, 'September', 'Entertainment', 90),
(59, 2026, 'September', 'Leisure & Sports', 85),
(60, 2026, 'September', 'Services', 245),
(61, 2026, 'September', 'Shopping', 175),
(62, 2026, 'September', 'Telecom', 50),
(63, 2026, 'September', 'Utilities', 72),
(64, 2026, 'October', 'Food & Beverages', 490),
(65, 2026, 'October', 'Entertainment', 130),
(66, 2026, 'October', 'Leisure & Sports', 155),
(67, 2026, 'October', 'Services', 315),
(68, 2026, 'October', 'Shopping', 410),
(69, 2026, 'October', 'Telecom', 60),
(70, 2026, 'October', 'Utilities', 88),
(71, 2026, 'November', 'Food & Beverages', 530),
(72, 2026, 'November', 'Entertainment', 175),
(73, 2026, 'November', 'Leisure & Sports', 160),
(74, 2026, 'November', 'Services', 330),
(75, 2026, 'November', 'Shopping', 520),
(76, 2026, 'November', 'Telecom', 65),
(77, 2026, 'November', 'Utilities', 92),
(78, 2026, 'December', 'Food & Beverages', 600),
(79, 2026, 'December', 'Entertainment', 250),
(80, 2026, 'December', 'Leisure & Sports', 220),
(81, 2026, 'December', 'Services', 350),
(82, 2026, 'December', 'Shopping', 680),
(83, 2026, 'December', 'Telecom', 70),
(84, 2026, 'December', 'Utilities', 100);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `expenses`
--
ALTER TABLE `expenses`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `expenses`
--
ALTER TABLE `expenses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
