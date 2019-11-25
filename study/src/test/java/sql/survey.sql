ALTER TABLE `jwp_jdbc`.`survey_results_public`
DROP INDEX `idx_hobby_student` ,
ADD INDEX `idx_hobby_student` (`Hobby` ASC, `Student` ASC) VISIBLE;
;

DROP TABLE IF EXISTS `numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `numbers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `years` varchar(45) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_year` (`years`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `numbers` WRITE;
/*!40000 ALTER TABLE `numbers` DISABLE KEYS */;
INSERT INTO `numbers` VALUES (1,'3-5 years',3),(2,'18-20 years',18),(3,'6-8 years',6),(4,'12-14 years',12),(5,'0-2 years',0),(6,'21-23 years',21),(7,'NA',0),(8,'24-26 years',24),(9,'9-11 years',9),(10,'15-17 years',15),(11,'27-29 years',27),(12,'30 or more years',30);
/*!40000 ALTER TABLE `numbers` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `dev_type`;
CREATE TABLE `jwp_jdbc`.`dev_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) not NULL,
  `years` VARCHAR(45) not NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `jwp_jdbc`.`dev_type`
ADD INDEX `ix_type` (`type` ASC) VISIBLE,
ADD INDEX `ix_year` (`years` ASC) VISIBLE;
;

INSERT INTO dev_type(type, years)
    SELECT
        substring_index(substring_index(survey_results_public.devtype, ';', numbers.id), ';', -1) as type,
        IF (yearscodingprof = '30 or more years', 30, cast(substring_index(yearscodingprof, '-', 1) as signed)) as years
    FROM
        numbers
    INNER JOIN
        survey_results_public ON char_length(survey_results_public.devtype) - char_length(replace(survey_results_public.devtype, ';', '')) >= numbers.id -1
    WHERE
        YearsCodingProf != 'NA' AND YearsCoding != 'NA';

SELECT type, AVG(years) AS average
FROM dev_type
GROUP BY type
ORDER BY average DESC;