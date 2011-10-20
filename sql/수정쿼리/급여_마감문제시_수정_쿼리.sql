SELECT rowid, a.*        
     -- INTO C_M_H_210101_1        
      FROM DUSER.DPRT_SALR_PAY A        
     WHERE A.SALR_PAY_DIV_CD = 'P'        
       AND A.PAY_DT          =  '2011-10-21'        
       AND ((( 'P' = 'R') AND A.EMP_NO = '110012' ) OR (( 'P'      != 'R') AND 1=1))         
and EMP_NO='120164'	  
;

select  960239 - 940191 from dual
;
