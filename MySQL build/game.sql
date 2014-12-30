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
-- Current Database: `game`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `game` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `game`;

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidates`
--

LOCK TABLES `candidates` WRITE;
/*!40000 ALTER TABLE `candidates` DISABLE KEYS */;
INSERT INTO `candidates` VALUES (45,43,3,1,0,'2014-12-29 06:18:29'),(46,44,3,1,0,'2014-12-29 06:18:25'),(47,45,3,1,0,'2014-12-29 06:18:20'),(48,46,3,1,0,'2014-12-29 06:18:16'),(49,47,3,1,0,'2014-12-29 06:18:05'),(50,48,3,1,0,'2014-12-29 06:17:57'),(51,49,3,1,0,'2014-12-29 06:17:51'),(52,50,3,1,0,'2014-12-29 06:17:46'),(53,51,3,1,0,'2014-12-29 06:17:40'),(54,52,3,1,0,'2014-12-29 06:17:31');
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `election`
--

LOCK TABLES `election` WRITE;
/*!40000 ALTER TABLE `election` DISABLE KEYS */;
INSERT INTO `election` VALUES (21,45,3,2,0,0,'a','2014-12-29 21:12:08'),(22,46,3,0,0,0,'aa','2014-12-29 06:18:29'),(23,47,3,0,0,0,'aaa','2014-12-29 06:18:29'),(24,48,3,0,0,0,'aaaa','2014-12-29 06:18:29'),(25,49,3,0,0,0,'b','2014-12-29 06:18:29'),(26,50,3,1,0,0,'bb','2014-12-29 06:53:24'),(27,51,3,1,0,0,'bbb','2014-12-29 22:00:42'),(28,52,3,0,0,0,'bbbb','2014-12-29 06:18:29'),(29,53,3,0,0,0,'c','2014-12-29 06:18:29'),(30,54,3,0,0,0,'cc','2014-12-29 06:18:29');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'2014-10-21 05:52:34','2014-12-29 06:04:59','0000-00-00 00:00:00'),(3,'2014-12-29 05:21:59','2014-12-29 06:17:30','0000-00-00 00:00:00');
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
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (43,'a','a',50),(44,'aa','aa',50),(45,'aaa','aaa',50),(46,'aaaa','aaaa',50),(47,'b','b',50),(48,'bb','bb',50),(49,'bbb','bbb',50),(50,'bbbb','bbbb',50),(51,'c','c',50),(52,'cc','cc',50);
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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_to_game`
--

LOCK TABLES `player_to_game` WRITE;
/*!40000 ALTER TABLE `player_to_game` DISABLE KEYS */;
INSERT INTO `player_to_game` VALUES (45,43,3,'2014-12-29 06:15:57'),(46,44,3,'2014-12-29 06:16:19'),(47,45,3,'2014-12-29 06:16:28'),(48,46,3,'2014-12-29 06:16:37'),(49,47,3,'2014-12-29 06:16:44'),(50,48,3,'2014-12-29 06:16:52'),(51,49,3,'2014-12-29 06:16:59'),(52,50,3,'2014-12-29 06:17:12'),(53,51,3,'2014-12-29 06:17:19'),(54,52,3,'2014-12-29 06:17:29');
/*!40000 ALTER TABLE `player_to_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_history`
--

DROP TABLE IF EXISTS `voting_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `voting_history` (
  `PK` bigint(20) NOT NULL AUTO_INCREMENT,
  `FK_PLAYER_ID` bigint(20) DEFAULT NULL,
  `FK_GAME_ID` bigint(20) DEFAULT NULL,
  `VOTED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `ELECTION` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PK`),
  KEY `FK_CONSTRAINTS_6` (`FK_GAME_ID`),
  KEY `FK_CONSTRAINTS_5` (`FK_PLAYER_ID`),
  CONSTRAINT `FK_CONSTRAINTS_5` FOREIGN KEY (`FK_PLAYER_ID`) REFERENCES `player_to_game` (`PLAYER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CONSTRAINTS_6` FOREIGN KEY (`FK_GAME_ID`) REFERENCES `player_to_game` (`GAME_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_history`
--

LOCK TABLES `voting_history` WRITE;
/*!40000 ALTER TABLE `voting_history` DISABLE KEYS */;
INSERT INTO `voting_history` VALUES (19,43,3,'2014-12-29 06:24:20',1),(20,44,3,'2014-12-29 06:53:24',1),(21,45,3,'2014-12-29 21:12:08',1),(22,46,3,'0000-00-00 00:00:00',0),(23,47,3,'2014-12-29 22:00:42',1),(24,48,3,'0000-00-00 00:00:00',0),(25,49,3,'0000-00-00 00:00:00',0),(26,50,3,'0000-00-00 00:00:00',0),(27,51,3,'0000-00-00 00:00:00',0),(28,52,3,'0000-00-00 00:00:00',0);
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

-- Dump completed on 2014-12-29 23:17:18
