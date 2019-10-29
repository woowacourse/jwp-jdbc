ALTER TABLE `jwp_jdbc`.`survey_results_public`
ADD INDEX `idx_hobby` (`Hobby` ASC);

DROP TABLE IF EXISTS `SEQUENCE_OF_JOB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `SEQUENCE_OF_JOB` (
  `seq_num` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SEQUENCE_OF_JOB`
--

LOCK TABLES `SEQUENCE_OF_JOB` WRITE;
/*!40000 ALTER TABLE `SEQUENCE_OF_JOB` DISABLE KEYS */;
INSERT INTO `SEQUENCE_OF_JOB` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(17),(18),(19),(20);
/*!40000 ALTER TABLE `SEQUENCE_OF_JOB` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `new_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `new_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DevType` varchar(45) NOT NULL,
  `YearsCodingProf` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`DevType`,`YearsCodingProf`),
  KEY `index` (`DevType`)
) ENGINE=InnoDB AUTO_INCREMENT=938213 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_table`
--

-- LOCK TABLES `new_table` WRITE;
/*!40000 ALTER TABLE `new_table` DISABLE KEYS */;
INSERT INTO `new_table` (DevType, YearsCodingProf)
SELECT substring_index(substring_index(DevType, ';', sequence.seq_num), ';', -1),
        substring_index(substring_index(YearsCodingProf, ' ', 1), '-', 1)
FROM survey_results_public
JOIN SEQUENCE_OF_JOB as sequence
ON sequence.seq_num <= CHAR_LENGTH(DevType) - CHAR_LENGTH(REPLACE(DevType, ';', '')) + 1;
/*!40000 ALTER TABLE `new_table` ENABLE KEYS */;
-- UNLOCK TABLES;