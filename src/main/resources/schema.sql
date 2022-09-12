-- ----------------------------
-- Table structure for products
-- ----------------------------
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `price` double DEFAULT NULL,
  `image` varchar(1000) DEFAULT NULL,
  `category` varchar(100) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `creation_by` int DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

