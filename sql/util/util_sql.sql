select * from tab order by tname;

-- ��� ���̺� �˻�
select * from all_tables
order by owner, table_name;

-- �α����� ��Ű�������� �˻�
select * from user_tables;

-- �÷��̸����� ���̺� �˻�
SELECT table_name, column_name
FROM all_tab_columns
WHERE column_name like '%CO_CD%'
;
