-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.24 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for hardware
CREATE DATABASE IF NOT EXISTS `hardware` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hardware`;

-- Dumping structure for table hardware.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.brand: ~0 rows (approximately)
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

-- Dumping structure for table hardware.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.category: ~0 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table hardware.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.city: ~4 rows (approximately)
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` (`id`, `name`) VALUES
	(1, 'Ampara'),
	(2, 'Kandy'),
	(3, 'Colombo'),
	(4, 'Jaffna');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;

-- Dumping structure for table hardware.company
CREATE TABLE IF NOT EXISTS `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `contact_number` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.company: ~0 rows (approximately)
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;

-- Dumping structure for table hardware.company_branch
CREATE TABLE IF NOT EXISTS `company_branch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int NOT NULL,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `branch_contact_number` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `company_branch_address_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_company_branch_company1_idx` (`company_id`),
  KEY `fk_company_branch_company_branch_address1_idx` (`company_branch_address_id`),
  CONSTRAINT `FK_company_branch_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `fk_company_branch_company_branch_address1` FOREIGN KEY (`company_branch_address_id`) REFERENCES `company_branch_address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.company_branch: ~0 rows (approximately)
/*!40000 ALTER TABLE `company_branch` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_branch` ENABLE KEYS */;

-- Dumping structure for table hardware.company_branch_address
CREATE TABLE IF NOT EXISTS `company_branch_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(45) COLLATE utf8_bin NOT NULL,
  `line2` varchar(45) COLLATE utf8_bin NOT NULL,
  `city_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_company_branch_address_city1_idx` (`city_id`),
  CONSTRAINT `fk_company_branch_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.company_branch_address: ~0 rows (approximately)
/*!40000 ALTER TABLE `company_branch_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_branch_address` ENABLE KEYS */;

-- Dumping structure for table hardware.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.customer: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- Dumping structure for table hardware.grn
CREATE TABLE IF NOT EXISTS `grn` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  `unique_id` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_supplier1_idx` (`supplier_id`),
  KEY `fk_grn_user1_idx` (`user_id`),
  CONSTRAINT `fk_grn_supplier1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  CONSTRAINT `fk_grn_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.grn: ~0 rows (approximately)
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;

-- Dumping structure for table hardware.grn_item
CREATE TABLE IF NOT EXISTS `grn_item` (
  `grn_item_id` int NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `buying_price` double NOT NULL,
  `grn_id` int NOT NULL,
  `stock_id` int NOT NULL,
  PRIMARY KEY (`grn_item_id`),
  KEY `fk_grn_item_grn1_idx` (`grn_id`),
  KEY `fk_grn_item_stock1_idx` (`stock_id`),
  CONSTRAINT `fk_grn_item_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `fk_grn_item_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.grn_item: ~0 rows (approximately)
/*!40000 ALTER TABLE `grn_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn_item` ENABLE KEYS */;

-- Dumping structure for table hardware.grn_payment
CREATE TABLE IF NOT EXISTS `grn_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grn_id` int NOT NULL,
  `payment_type_id` int NOT NULL,
  `payment` double NOT NULL,
  `balance` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_payment_grn1_idx` (`grn_id`),
  KEY `fk_grn_payment_payment_type1_idx` (`payment_type_id`),
  CONSTRAINT `fk_grn_payment_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `fk_grn_payment_payment_type1` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.grn_payment: ~0 rows (approximately)
/*!40000 ALTER TABLE `grn_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn_payment` ENABLE KEYS */;

-- Dumping structure for table hardware.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  `unique_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_customer1_idx` (`customer_id`),
  KEY `fk_invoice_user1_idx` (`user_id`),
  CONSTRAINT `fk_invoice_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `fk_invoice_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.invoice: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;

-- Dumping structure for table hardware.invoice_item
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stock_id` int NOT NULL,
  `qty` int NOT NULL,
  `invoice_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_item_stock1_idx` (`stock_id`),
  KEY `fk_invoice_item_invoice1_idx` (`invoice_id`),
  CONSTRAINT `fk_invoice_item_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `fk_invoice_item_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.invoice_item: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;

-- Dumping structure for table hardware.invoice_payment
CREATE TABLE IF NOT EXISTS `invoice_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int NOT NULL,
  `payment_type_id` int NOT NULL,
  `payment` double NOT NULL,
  `balance` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_payment_invoice1_idx` (`invoice_id`),
  KEY `fk_invoice_payment_payment_type1_idx` (`payment_type_id`),
  CONSTRAINT `fk_invoice_payment_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `fk_invoice_payment_payment_type1` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.invoice_payment: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_payment` ENABLE KEYS */;

-- Dumping structure for table hardware.payment_type
CREATE TABLE IF NOT EXISTS `payment_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.payment_type: ~3 rows (approximately)
/*!40000 ALTER TABLE `payment_type` DISABLE KEYS */;
INSERT INTO `payment_type` (`id`, `name`) VALUES
	(1, 'Cash'),
	(2, 'Card'),
	(3, 'Cheque');
/*!40000 ALTER TABLE `payment_type` ENABLE KEYS */;

-- Dumping structure for table hardware.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `brand_id` int NOT NULL,
  `name` text COLLATE utf8_bin NOT NULL,
  `product_no` varchar(50) COLLATE utf8_bin NOT NULL,
  `product_status_id` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_product_category1_idx` (`category_id`),
  KEY `fk_product_brand1_idx` (`brand_id`),
  KEY `fk_product_product_status1_idx` (`product_status_id`),
  CONSTRAINT `fk_product_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_product_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_product_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.product: ~0 rows (approximately)
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping structure for table hardware.product_status
CREATE TABLE IF NOT EXISTS `product_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(25) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.product_status: ~2 rows (approximately)
/*!40000 ALTER TABLE `product_status` DISABLE KEYS */;
INSERT INTO `product_status` (`id`, `name`) VALUES
	(1, 'Available'),
	(2, 'Unavailble');
/*!40000 ALTER TABLE `product_status` ENABLE KEYS */;

-- Dumping structure for table hardware.stock
CREATE TABLE IF NOT EXISTS `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `selling_price` double NOT NULL,
  `mfd` date NOT NULL,
  `exd` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_product1_idx` (`product_id`),
  CONSTRAINT `fk_stock_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.stock: ~0 rows (approximately)
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;

-- Dumping structure for table hardware.supplier
CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `contact_number` varchar(10) COLLATE utf8_bin NOT NULL,
  `email` varchar(45) COLLATE utf8_bin NOT NULL,
  `company_branch_id` int NOT NULL,
  `user_status_id` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_supplier_company_branch1_idx` (`company_branch_id`),
  KEY `fk_supplier_user_status1_idx` (`user_status_id`),
  CONSTRAINT `fk_supplier_company_branch1` FOREIGN KEY (`company_branch_id`) REFERENCES `company_branch` (`id`),
  CONSTRAINT `fk_supplier_user_status1` FOREIGN KEY (`user_status_id`) REFERENCES `user_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.supplier: ~0 rows (approximately)
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;

-- Dumping structure for table hardware.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `username` varchar(45) COLLATE utf8_bin NOT NULL,
  `password` varchar(45) COLLATE utf8_bin NOT NULL,
  `contact_number` varchar(10) COLLATE utf8_bin NOT NULL,
  `user_type_id` int NOT NULL,
  `user_status_id` int DEFAULT '1',
  `city_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_user_type_idx` (`user_type_id`),
  KEY `fk_user_user_status1_idx` (`user_status_id`),
  KEY `fk_user_city1_idx` (`city_id`),
  CONSTRAINT `fk_user_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `fk_user_user_status1` FOREIGN KEY (`user_status_id`) REFERENCES `user_status` (`id`),
  CONSTRAINT `fk_user_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.user: ~1 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `name`, `username`, `password`, `contact_number`, `user_type_id`, `user_status_id`, `city_id`) VALUES
	(1, 'tuwan', 'munaff', '1234', '0706211300', 1, 1, 3);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping structure for table hardware.user_status
CREATE TABLE IF NOT EXISTS `user_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.user_status: ~2 rows (approximately)
/*!40000 ALTER TABLE `user_status` DISABLE KEYS */;
INSERT INTO `user_status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');
/*!40000 ALTER TABLE `user_status` ENABLE KEYS */;

-- Dumping structure for table hardware.user_type
CREATE TABLE IF NOT EXISTS `user_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- Dumping data for table hardware.user_type: ~4 rows (approximately)
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` (`id`, `name`) VALUES
	(1, 'Admin'),
	(2, 'Data Enter'),
	(3, 'Account'),
	(4, 'Cashier');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
