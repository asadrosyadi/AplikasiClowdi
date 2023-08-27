-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 20, 2023 at 02:24 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `clowdi`
--

-- --------------------------------------------------------

--
-- Table structure for table `datasensor`
--

CREATE TABLE `datasensor` (
  `id` int(11) NOT NULL,
  `HWID` varchar(256) NOT NULL,
  `email` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `rh` int(11) NOT NULL,
  `suhu_luar` text NOT NULL,
  `suhu_dalam` text NOT NULL,
  `rain_drop` text NOT NULL,
  `ldr` text NOT NULL,
  `kondisi_jemuran` text NOT NULL,
  `kondisi_heater` text NOT NULL,
  `kondisi_mesin` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `datasensor`
--

INSERT INTO `datasensor` (`id`, `HWID`, `email`, `time`, `rh`, `suhu_luar`, `suhu_dalam`, `rain_drop`, `ldr`, `kondisi_jemuran`, `kondisi_heater`, `kondisi_mesin`) VALUES
(1, 'VP221201D', 'admin@admin.com', '2023-08-08 00:31:08', 63, '30', '25', '70', '836', 'luar', 'ON', 'Auto'),
(2, 'VP221201D', 'admin@admin.com', '2023-08-08 00:31:10', 63, '30', '25', '70', '836', 'luar', 'ON', 'Auto'),
(3, 'VP221201D', 'admin@admin.com', '2023-08-08 00:31:13', 63, '30', '25', '70', '836', 'luar', 'ON', 'Auto'),
(4, 'asd', 'rosyadi.asad@gmail.com ', '2023-08-15 00:34:58', 0, '0', '0', '0', '0', 'dalam', 'OFF', 'OFF');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `nama` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `token` text NOT NULL,
  `HWID` text NOT NULL,
  `kondisi_mesin` text NOT NULL,
  `mode_mesin` text NOT NULL,
  `heater` text NOT NULL,
  `kondisi_jemuran` text NOT NULL,
  `nilai_kering` text NOT NULL,
  `batas_suhu` text NOT NULL,
  `batas_nilai_hujan` text NOT NULL,
  `batas_nilai_siang` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `nama`, `email`, `password`, `token`, `HWID`, `kondisi_mesin`, `mode_mesin`, `heater`, `kondisi_jemuran`, `nilai_kering`, `batas_suhu`, `batas_nilai_hujan`, `batas_nilai_siang`) VALUES
(1, 'admin', 'admin@admin.com', '$2y$10$KCGfI4Htsth8LBUvkeHueuiZ8ax1cuwTMlgNELjh4oXpR.SMLXm92', 'zwJdmaOCQoDW9XgH', 'VP221201D', 'ON', 'Manual', 'ON', 'Luar', '20', '23', '101', '114'),
(6, 'asad', 'rosyadi.asad@gmail.com', '$2y$10$t4ETEqcU0zn4Tw0wTaG1CuWb0bJME6qPYDCvncff79GvN7MyKxDuu', 'be8926ec35f5f23682a56c97a7cdfe83', 'asd', 'OFF', 'Otomatis', 'OFF', 'Dalam', '20', '25', '100', '115');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `datasensor`
--
ALTER TABLE `datasensor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `datasensor`
--
ALTER TABLE `datasensor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
