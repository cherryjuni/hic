select * from tab order by tname;

-- 모든 테이블 검색
select * from all_tables
order by owner, table_name;

-- 로그인한 스키마에서만 검색
select * from user_tables;

-- 컬럼이름으로 테이블 검색
SELECT table_name, column_name
FROM all_tab_columns
WHERE column_name like '%CO_CD%'
;
