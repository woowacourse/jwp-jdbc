-- 데이터 베이스 이름 입력해주시면됩니다~
use optimize_mission;

-- numbers table 생성

create table numbers(
	n int(11),
    primary key(n)
);

insert into numbers values (1),(2),(3),(4),(5),(6),(7),(8),(9),(10);
insert into numbers values (11),(12),(13),(14),(15),(16),(17),(18),(19),(20);
insert into numbers values (21),(22),(23),(24),(25),(26),(27),(28),(29),(30);

-- primary key 지정
ALTER TABLE `survey_results_public`
CHANGE COLUMN `Respondent` `Respondent` INT(11) NOT NULL ,
ADD PRIMARY KEY (`Respondent`);
;


-- mission 1 (Coding as a Hobby)

# hobby 컬럼 타입 변경

ALTER TABLE `survey_results_public`
CHANGE COLUMN `Hobby` `Hobby` VARCHAR(64) NULL DEFAULT NULL ;

# hobby 인덱스 추가
alter table survey_results_public add index idx_hobby(hobby);

# 실행 결과
select hobby,(count(*) / (select count(*) from survey_results_public)) from survey_results_public group by hobby;

-- mission 2

-- 인덱스 추가

-- devtype 테이블 생성

CREATE TABLE `DevType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `devtype` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

 -- devtype 데이터 삽입

insert into DevType(devtype) select
  SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType, ';', numbers.n), ';', -1) as dev
from
  numbers inner join survey_results_public
  on CHAR_LENGTH(survey_results_public.DevType)
     -CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', ''))>=numbers.n-1
group by dev;

-- yearsOfProf 테이블 생성

create table `yearsCodingProf` (
	Respondent int not null,
	yearsCodingProf varchar(20),
    primary key (Respondent)
);

-- yearsOfProf 데이터 삽입

insert into yearsCodingProf
select Respondent, yearsCodingProf
from survey_results_public;


-- devtype <-> yearsCodingProf join table 생성

create table yearsCodingProf_devtype (
	id int not null AUTO_INCREMENT,
    Respondent int,
    DevType_id int,
  PRIMARY KEY  (`id`),
  FOREIGN KEY (`Respondent`) REFERENCES `yearsCodingProf` (`Respondent`),
  FOREIGN KEY (`DevType_id`) REFERENCES `DevType` (`id`)
);

-- 데이터 삽입
insert into yearsCodingProf_devtype(Respondent,DevType_id)
select a.Respondent, b.id
from
(select Respondent, DevType
from survey_results_public
)as a inner join DevType as b
on a.DevType like concat('%',b.devType,'%');


-- 인덱스 삽입

alter table DevType add index idx_devtype (devtype);
alter table yearsCodingProf add index idx_yearsCodingProf (yearsCodingProf);
alter table yearsCodingProf_devtype add index idx_Respondent_Dev_id (Respondent,Devtype_id);
alter table yearsCodingProf_devtype add index idx_Dev_id_Respondent (Devtype_id,Respondent);
alter table yearsCodingProf_devtype add index idx_Dev_id (Devtype_id);

# 실행 쿼리
select
a.devtype, tmp.average
from
(
select a.DevType_id, avg(b.yearsCodingProf) as average
from
yearsCodingProf_devtype as a
inner join
(select * from yearsCodingProf where yearsCodingProf != 'NA') as b
on a.Respondent = b.Respondent
group by a.devtype_id
order by average desc
) as tmp
Left Outer join
DevType as a
on tmp.devtype_id = a.id
