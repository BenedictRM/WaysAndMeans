-- MySQL dump 10.13  Distrib 5.5.25, for Win64 (x86)
--
-- Host: localhost    Database: game
-- ------------------------------------------------------
-- Server version	5.5.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `candidates`
--

DROP TABLE IF EXISTS `candidates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `candidates` (
  `_PK_CANDIDATES` bigint(20) NOT NULL AUTO_INCREMENT,
  `_FK_PLAYER` bigint(20) DEFAULT NULL,
  `_FK_GAME` bigint(20) DEFAULT NULL,
  `YEA` smallint(5) NOT NULL DEFAULT '0',
  `NAY` smallint(5) NOT NULL DEFAULT '0',
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`_PK_CANDIDATES`),
  KEY `FK_CONSTRAINTS_1` (`_FK_PLAYER`),
  KEY `FK_CONSTRAINTS_2` (`_FK_GAME`),
  CONSTRAINT `FK_CONSTRAINTS_1` FOREIGN KEY (`_FK_PLAYER`) REFERENCES `player` (`_PK_PLAYER`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_2` FOREIGN KEY (`_FK_GAME`) REFERENCES `game` (`_PK_GAME`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidates`
--

LOCK TABLES `candidates` WRITE;
/*!40000 ALTER TABLE `candidates` DISABLE KEYS */;
INSERT INTO `candidates` VALUES (79,77,1,0,0,'2015-01-05 05:29:50'),(80,78,1,0,0,'2015-01-05 21:31:42');
/*!40000 ALTER TABLE `candidates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `election`
--

DROP TABLE IF EXISTS `election`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `election` (
  `_PK_ELECTION` bigint(20) NOT NULL AUTO_INCREMENT,
  `_FK_CANDIDATES` bigint(20) DEFAULT NULL,
  `_FK_GAME` bigint(20) DEFAULT NULL,
  `YEA` int(4) NOT NULL DEFAULT '0',
  `NAY` int(4) NOT NULL DEFAULT '0',
  `ABSTAIN` int(4) NOT NULL DEFAULT '0',
  `CANDIDATE` varchar(255) NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`_PK_ELECTION`),
  KEY `FK_CONSTRAINTS_4` (`_FK_GAME`),
  KEY `FK_CONSTRAINTS_3` (`_FK_CANDIDATES`),
  CONSTRAINT `FK_CONSTRAINTS_3` FOREIGN KEY (`_FK_CANDIDATES`) REFERENCES `candidates` (`_PK_CANDIDATES`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_4` FOREIGN KEY (`_FK_GAME`) REFERENCES `game` (`_PK_GAME`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `election`
--

LOCK TABLES `election` WRITE;
/*!40000 ALTER TABLE `election` DISABLE KEYS */;
/*!40000 ALTER TABLE `election` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game` (
  `_PK_GAME` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `STARTED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `COMPLETED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`_PK_GAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'0000-00-00 00:00:00','0000-00-00 00:00:00','0000-00-00 00:00:00'),(3,'2014-12-29 05:21:59','2015-01-02 22:11:31','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `_PK_PLAYER` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `REPUTATION_POINTS` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`_PK_PLAYER`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (77,'a','a',50),(78,'b','b',50);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_role`
--

DROP TABLE IF EXISTS `player_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_role` (
  `_PK_PLAYER_ROLE` bigint(20) NOT NULL AUTO_INCREMENT,
  `_FK_P2G` bigint(20) DEFAULT NULL,
  `PRESIDENT` tinyint(1) NOT NULL DEFAULT '0',
  `SENATOR` tinyint(1) NOT NULL DEFAULT '0',
  `ELECTED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`_PK_PLAYER_ROLE`),
  KEY `FK_CONSTRAINTS_7` (`_FK_P2G`),
  CONSTRAINT `FK_CONSTRAINTS_7` FOREIGN KEY (`_FK_P2G`) REFERENCES `player_to_game` (`_PK_P2G`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_role`
--

LOCK TABLES `player_role` WRITE;
/*!40000 ALTER TABLE `player_role` DISABLE KEYS */;
INSERT INTO `player_role` VALUES (24,79,0,0,'0000-00-00 00:00:00'),(25,80,0,0,'0000-00-00 00:00:00');
/*!40000 ALTER TABLE `player_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_to_game`
--

DROP TABLE IF EXISTS `player_to_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_to_game` (
  `_PK_P2G` bigint(20) NOT NULL AUTO_INCREMENT,
  `_FK_PLAYER` bigint(20) DEFAULT NULL,
  `_FK_GAME` bigint(20) DEFAULT NULL,
  `WHEN_JOINED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`_PK_P2G`),
  KEY `FK_CONSTRAINTS_6` (`_FK_GAME`),
  KEY `FK_CONSTRAINTS_5` (`_FK_PLAYER`),
  CONSTRAINT `FK_CONSTRAINTS_5` FOREIGN KEY (`_FK_PLAYER`) REFERENCES `player` (`_PK_PLAYER`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_6` FOREIGN KEY (`_FK_GAME`) REFERENCES `game` (`_PK_GAME`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_to_game`
--

LOCK TABLES `player_to_game` WRITE;
/*!40000 ALTER TABLE `player_to_game` DISABLE KEYS */;
INSERT INTO `player_to_game` VALUES (79,77,1,'2015-01-05 05:29:51'),(80,78,1,'2015-01-05 21:31:42');
/*!40000 ALTER TABLE `player_to_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_history`
--

DROP TABLE IF EXISTS `voting_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `voting_history` (
  `_PK_VOTING_HISTORY` bigint(20) NOT NULL AUTO_INCREMENT,
  `_FK_P2G` bigint(20) DEFAULT NULL,
  `VOTED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `ELECTION` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`_PK_VOTING_HISTORY`),
  KEY `FK_CONSTRAINTS_8` (`_FK_P2G`),
  CONSTRAINT `FK_CONSTRAINTS_8` FOREIGN KEY (`_FK_P2G`) REFERENCES `player_to_game` (`_PK_P2G`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_history`
--

LOCK TABLES `voting_history` WRITE;
/*!40000 ALTER TABLE `voting_history` DISABLE KEYS */;
INSERT INTO `voting_history` VALUES (53,79,'0000-00-00 00:00:00',0),(54,80,'0000-00-00 00:00:00',0);
/*!40000 ALTER TABLE `voting_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-05 14:35:44
