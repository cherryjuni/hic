# batch env profile call
BATCH_HOME=/HIC_APP/batch
export BATCH_HOME
. $BATCH_HOME/config/batch.profile
EXEC_DAY=`date +"%C%y-%m-%d"`
LOG_TIME=`date +"%H%M%S"`
LOG_FILE="PayCnfmProc_"$LOG_TIME".log"
java -Dbatch.home=/HIC_APP/batch com.cabis.batch.a.lo.ma.PayCnfmProc HICDB zuser hiczusr26 $EXEC_DAY > /HIC_APP/batch/log/alo/PayCnfmProc/$LOG_FILE
