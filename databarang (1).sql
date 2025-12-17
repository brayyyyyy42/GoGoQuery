-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 17 Des 2024 pada 05.17
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `databarang`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `mscart`
--

CREATE TABLE `mscart` (
  `UserID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL,
  `Quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `mscart`
--

INSERT INTO `mscart` (`UserID`, `ItemID`, `Quantity`) VALUES
(2, 3, 4),
(3, 2, 3),
(4, 4, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `msitem`
--

CREATE TABLE `msitem` (
  `ItemID` int(11) NOT NULL,
  `ItemName` varchar(255) NOT NULL,
  `ItemPrice` float NOT NULL,
  `ItemDesc` varchar(255) DEFAULT NULL,
  `ItemCategory` varchar(255) NOT NULL,
  `ItemStock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `msitem`
--

INSERT INTO `msitem` (`ItemID`, `ItemName`, `ItemPrice`, `ItemDesc`, `ItemCategory`, `ItemStock`) VALUES
(1, 'Wireless Mouse', 20000, 'Ergonomic wireless mouse', 'Electronics', 50),
(2, 'Mechanical Keyboard', 80000, 'RGB backlit keyboard', 'Electronics', 30),
(3, 'USB-C Cable', 10000, '1 meter USB-C to USB-A cable', 'Accessories', 100),
(4, 'Batik Shirt', 50000, 'Traditional batik shirt', 'Clothing', 16),
(5, 'Instant Coffee', 15000, 'Pack of 10 sachets', 'Beverages', 80),
(6, 'jojit', 10, 'sdawdaadaasdadwadad', 'Electronics', 1),
(7, 'Sepatu super', 100, 'sepatu super punya kekuatan sponsored by pt nueliindo petroleum\nwowwwwwwwwwwwwwwwwwwwwwwwwwww\nsuper sekaliiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii\n', 'bryan', 300);

-- --------------------------------------------------------

--
-- Struktur dari tabel `msuser`
--

CREATE TABLE `msuser` (
  `UserID` int(11) NOT NULL,
  `UserDOB` date NOT NULL,
  `UserEmail` varchar(255) NOT NULL,
  `UserPassword` varchar(255) NOT NULL,
  `UserRole` varchar(255) NOT NULL,
  `UserGender` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `msuser`
--

INSERT INTO `msuser` (`UserID`, `UserDOB`, `UserEmail`, `UserPassword`, `UserRole`, `UserGender`) VALUES
(1, '1990-05-10', 'admin@gomail.com', 'admin123', 'Manager', 'Male'),
(2, '1985-08-15', 'johndoe@gomail.com', 'password123', 'Shopper', 'Male'),
(3, '1992-03-22', 'janedoe@gomail.com', 'jane456', 'Shopper', 'Female'),
(4, '1995-07-10', 'agus_pratama@gomail.com', 'agus789', 'Shopper', 'Male'),
(5, '1998-11-25', 'siti_nuraini@gomail.com', 'siti123', 'Shopper', 'Female'),
(6, '2004-12-18', 'nuelanimal@gomail.com', 'nuel123', 'Shopper', 'Male'),
(7, '2005-12-02', 'bry@gomail.com', 'bry123', 'Manager', 'Male');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `TransactionID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL,
  `Quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transactiondetail`
--

INSERT INTO `transactiondetail` (`TransactionID`, `ItemID`, `Quantity`) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 4),
(3, 2, 3),
(4, 4, 1),
(5, 5, 2),
(6, 4, 3),
(7, 4, 6);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactionheader`
--

CREATE TABLE `transactionheader` (
  `TransactionID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `DateCreated` date DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transactionheader`
--

INSERT INTO `transactionheader` (`TransactionID`, `UserID`, `DateCreated`, `Status`) VALUES
(1, 1, '2024-12-01', 'Completed'),
(2, 2, '2024-12-02', 'Completed'),
(3, 3, '2024-12-03', 'Completed'),
(4, 4, '2024-12-04', 'Completed'),
(5, 5, '2024-12-05', 'Completed'),
(6, 1, '2024-12-15', 'Completed'),
(7, 1, '2024-12-17', 'Pending');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `mscart`
--
ALTER TABLE `mscart`
  ADD PRIMARY KEY (`UserID`,`ItemID`),
  ADD KEY `ItemID` (`ItemID`);

--
-- Indeks untuk tabel `msitem`
--
ALTER TABLE `msitem`
  ADD PRIMARY KEY (`ItemID`);

--
-- Indeks untuk tabel `msuser`
--
ALTER TABLE `msuser`
  ADD PRIMARY KEY (`UserID`);

--
-- Indeks untuk tabel `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD PRIMARY KEY (`TransactionID`,`ItemID`),
  ADD KEY `ItemID` (`ItemID`);

--
-- Indeks untuk tabel `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD PRIMARY KEY (`TransactionID`),
  ADD KEY `UserID` (`UserID`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `msitem`
--
ALTER TABLE `msitem`
  MODIFY `ItemID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `msuser`
--
ALTER TABLE `msuser`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `transactionheader`
--
ALTER TABLE `transactionheader`
  MODIFY `TransactionID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `mscart`
--
ALTER TABLE `mscart`
  ADD CONSTRAINT `mscart_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`),
  ADD CONSTRAINT `mscart_ibfk_2` FOREIGN KEY (`ItemID`) REFERENCES `msitem` (`ItemID`);

--
-- Ketidakleluasaan untuk tabel `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD CONSTRAINT `transactiondetail_ibfk_1` FOREIGN KEY (`TransactionID`) REFERENCES `transactionheader` (`TransactionID`),
  ADD CONSTRAINT `transactiondetail_ibfk_2` FOREIGN KEY (`ItemID`) REFERENCES `msitem` (`ItemID`);

--
-- Ketidakleluasaan untuk tabel `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD CONSTRAINT `transactionheader_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
