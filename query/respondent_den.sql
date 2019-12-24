-- devType 테이블 생성
create table dev_type(
	id bigint auto_increment,
	title varchar(255),
	primary key(id)
);

-- dev type별로 삽입
insert into dev_type(title) 
select distinct substring_index(devType, ';', 1) from survey_results_public;

-- 비정규화 테이블 생성
create table respondent_den(
	id bigint auto_increment,
    respondent_number int,
    yearsCodingProf_int int,
    dev_type_id bigint,
    primary key(id),
    foreign key(dev_type_id) references dev_type(id)
);

-- 비정규화 테이블 삽입
insert into respondent_den(respondent_number, yearsCodingProf_int, dev_type_id)
	select id, yearsCodingProf_int, rdt.dev_type_id
	from respondent as r
		inner join respondent_dev_type as rdt
        on r.id = rdt.respondent_id;
delete from respondent_den;
        
-- 비정규화 테이블에서 조회하기 0.17 ± 0.02 sec
select title, years_avg
from (
select dev_type_id as dtid, round(avg(yearsCodingProf_int), 1) as years_avg
from respondent_den
where dev_type_id != (select id from dev_type where title = 'NA')
group by dev_type_id) as to_join
	inner join dev_type as dt
    on dtid = dt.id;