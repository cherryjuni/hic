
select * from guser.gsct_sql_info
where upper(sql_stmt) like 'AUSER.ALOT_LOAN_ONLN_PAY'
;

select * from guser.gsct_prgm where sc_path like '%PayProcSC%';

select * from guser.gsct_prgm where sc_path like '%PayBC%';

--BC_NO
--ALOMA_PAYBC
--EACPA_PAYMNGBC




