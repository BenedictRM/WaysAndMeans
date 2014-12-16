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
  `PK` bigint(20) NOT NULL AUTO_INCREMENT,
  `FK_PLAYER` bigint(20) DEFAULT NULL,
  `FK_GAME` bigint(20) DEFAULT NULL,
  `YEA` smallint(5) NOT NULL DEFAULT '0',
  `NAY` smallint(5) NOT NULL DEFAULT '0',
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PK`),
  KEY `FK_CONSTRAINTS_1` (`FK_PLAYER`),
  KEY `FK_CONSTRAINTS_2` (`FK_GAME`),
  CONSTRAINT `FK_CONSTRAINTS_1` FOREIGN KEY (`FK_PLAYER`) REFERENCES `player` (`APP_USERS_PK`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_2` FOREIGN KEY (`FK_GAME`) REFERENCES `game` (`PK_GAME_NUMBER`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidates`
--

LOCK TABLES `candidates` WRITE;
/*!40000 ALTER TABLE `candidates` DISABLE KEYS */;
/*!40000 ALTER TABLE `candidates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `election`
--

DROP TABLE IF EXISTS `election`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `election` (
  `PK` bigint(20) NOT NULL AUTO_INCREMENT,
  `FK_CANDIDATES` bigint(20) DEFAULT NULL,
  `FK_GAME` bigint(20) DEFAULT NULL,
  `YEA` int(4) NOT NULL DEFAULT '0',
  `NAY` int(4) NOT NULL DEFAULT '0',
  `ABSTAIN` int(4) NOT NULL DEFAULT '0',
  `CANDIDATE` varchar(255) NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PK`),
  KEY `FK_CONSTRAINTS_3` (`FK_CANDIDATES`),
  KEY `FK_CONSTRAINTS_4` (`FK_GAME`),
  CONSTRAINT `FK_CONSTRAINTS_3` FOREIGN KEY (`FK_CANDIDATES`) REFERENCES `candidates` (`PK`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_4` FOREIGN KEY (`FK_GAME`) REFERENCES `game` (`PK_GAME_NUMBER`) ON DELETE CASCADE ON UPDATE CASCADE
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
  `PK_GAME_NUMBER` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `STARTED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `COMPLETED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`PK_GAME_NUMBER`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'2014-10-21 05:52:34','2014-11-14 21:47:57','0000-00-00 00:00:00'),(2,'2014-11-14 22:32:33','0000-00-00 00:00:00','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `APP_USERS_PK` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `REPUTATION_POINTS` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`APP_USERS_PK`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_to_game`
--

DROP TABLE IF EXISTS `player_to_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_to_game` (
  `PK` bigint(20) NOT NULL AUTO_INCREMENT,
  `PLAYER_ID` bigint(20) NOT NULL,
  `GAME_ID` bigint(20) DEFAULT NULL,
  `WHEN_JOINED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PK`),
  KEY `PLAYER_ID` (`PLAYER_ID`),
  KEY `GAME_ID` (`GAME_ID`),
  CONSTRAINT `player_to_game_ibfk_1` FOREIGN KEY (`PLAYER_ID`) REFERENCES `player` (`APP_USERS_PK`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `player_to_game_ibfk_2` FOREIGN KEY (`GAME_ID`) REFERENCES `game` (`PK_GAME_NUMBER`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_to_game`
--

LOCK TABLES `player_to_game` WRITE;
/*!40000 ALTER TABLE `player_to_game` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_to_game` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-16 16:37:20
