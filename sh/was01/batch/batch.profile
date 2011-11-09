export LANG=ko_KR.IBM-eucKR
export BATCH_HOME=/HIC_APP/batch
export JDK_HOME=/usr/java5_64
export JAVA_HOME=/usr/java5_64
export PATH=$PATH:$JDK_HOME/bin:.
export PATH=$PATH:$JAVA_HOME
export LIBPATH=$LIBPATH:/usr/opt/db2_08_01/lib:.

export TODAY=`date +%m%d%Y`
export JAVA_RUNTIME_LOG_HOME=$BATCH_HOME/log

# The following three lines have been added by UDB DB2.
if [ -f /cbtest/sqllib/db2profile ]; then
    . /cbtest/sqllib/db2profile
fi

# LOG 디렉토리 생성
if [ ! -d ${JAVA_RUNTIME_LOG_HOME} ]; then
        mkdir ${JAVA_RUNTIME_LOG_HOME}
fi

# classpath 설정
CLASSPATH=$CLASSPATH:$BATCH_HOME/classes
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/log4j.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/batch.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/commons-net-1.1.0.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/xercesImpl.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/dom4j-full.jar
CLASSPATH=$CLASSPATH:/HIC_APP/jeus/webhome/app_home/cabis/WEB-INF/lib/x.jar
CLASSPATH=$CLASSPATH:/HIC_APP/jeus/webhome/app_home/cabis/WEB-INF/lib/xutil.jar
CLASSPATH=$CLASSPATH:/HIC_APP/batch/lib/j2ee.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/commons-lang-2.2.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/commons-io-1.3.2.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/commons-dbutils-1.1.jar
#CLASSPATH=$CLASSPATH:$BATCH_HOME/classes
CLASSPATH=$CLASSPATH:/HIC_APP/jeus/webhome/app_home/cabis/WEB-INF/classes

#DB 사용 lib
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/ojdbc14.jar

#MSSQL lib
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/msbase.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/mssqlserver.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/msutil.jar

#mail lib
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/mail.jar
CLASSPATH=$CLASSPATH:$BATCH_HOME/lib/activation.jar
export CLASSPATH
export PATH

#for projobs-agent
umask 002
