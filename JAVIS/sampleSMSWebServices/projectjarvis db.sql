-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: mysql5.gear.host
-- Generation Time: Aug 19, 2017 at 05:27 PM
-- Server version: 5.7.18-log
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectjarvis`
--

-- --------------------------------------------------------

--
-- Table structure for table `jarviscommand`
--

CREATE TABLE `jarviscommand` (
  `Command` text NOT NULL,
  `Time` varchar(20) NOT NULL,
  `ID` int(11) NOT NULL,
  `Sender` varchar(20) NOT NULL,
  `Type` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jarviscommand`
--

INSERT INTO `jarviscommand` (`Command`, `Time`, `ID`, `Sender`, `Type`) VALUES
('Nothing', 'Nothing', 2, '*****', 'USER'),
('Hello world', 'CURRENT_TIMESTAMP', 3, '447700900000', 'USER'),
('Hello world', '2017-01-01 00:00:00', 4, '447700900000', 'USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jarviscommand`
--
ALTER TABLE `jarviscommand`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID` (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jarviscommand`
--
ALTER TABLE `jarviscommand`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
