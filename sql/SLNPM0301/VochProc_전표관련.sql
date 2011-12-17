
-- VochProcDao.java:142
SELECT 						
   A.LOAN_PAMT,				
   A.STMP_FEE,					
   A.PAY_AMT,					
   0 TRT_FEE,	
   0 CPRT_FEE,					
   A.SET_FEE,					
   A.NOTA_FEE,					
   A.ETC_FEE,					
   A.LOAN_NO, 					
   A.LOAN_SEQ,	   				
   '0' RT_FEE_DFER_PROC_FG,		
   '0' CPRT_FEE_DFER_PROC_FG,	
   C.BANK_CD,	        	
   C.MO_ACNT_NO,		
   '0' STMP_FEE_AF_ACQT_FG, 
   A.PRXP_PAMT,		
   A.PRXP_INT		
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,  
   AUSER.APDT_PRDT_BASE     B,  
   AUSER.ALOT_MO_ACNT_BASE  C   
WHERE A.LOAN_NO  = '11121600004' --?	     
  AND A.LOAN_SEQ = '01' --?	     
  AND A.SNUM     = '01' --?	     
  AND A.PRDT_CD  = B.PRDT_CD    
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  and DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD
;

--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:143) : arg1= 11121600004
--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:144) : arg2= 01
--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:145) : arg3= 01

