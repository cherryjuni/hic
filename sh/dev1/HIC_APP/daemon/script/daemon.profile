export LANG=ko_KR.IBM-eucKR
export BATCH_HOME=/HIC_APP/batch
export JDK_HOME=/usr/java5_64
#export PATH=$PATH:/usr/opt/db2_08_01/lib:$JDK_HOME/bin:.
export PATH=$PATH:$JDK_HOME/bin:.
#export LIBPATH=$LIBPATH:/usr/opt/db2_08_01/lib:.
export SERVER_LIB=/HIC_APP/daemon/lib
export JEUS_LIBS=/HIC_APP/jeus6/webhome/app_home/cabis/WEB-INF/lib
export JEUS_CLASS=/HIC_APP/jeus6/webhome/app_home/cabis/WEB-INF/classes
export JEUS_DBLIB=/HIC_APP/jeus6/lib/datasource
export JEUS_APPLIB=/HIC_APP/jeus6/lib/application
export JEUS_SYSLIB=/HIC_APP/jeus6/lib/system
export SERVER_CLASS=/HIC_APP/daemon/class

export TODAY=`date +%m%d%Y`
export JAVA_RUNTIME_LOG_HOME=$BATCH_HOME/log


# LOG 디렉토리 생성
if [ ! -d ${JAVA_RUNTIME_LOG_HOME} ]; then
        mkdir ${JAVA_RUNTIME_LOG_HOME}
fi

# classpath 설정
CLASSPATH=$CLASSPATH:$JEUS_LIBS/batch.jar
CLASSPATH=$CLASSPATH:$JEUS_LIBS/log4j.jar
CLASSPATH=$CLASSPATH:$JEUS_LIBS/commons-net-1.0.0.jar
CLASSPATH=$CLASSPATH:$JEUS_APPLIB/xercesImpl.jar
CLASSPATH=$CLASSPATH:$JEUS_APPLIB/dom4j-full.jar
CLASSPATH=$CLASSPATH:$JEUS_CLASS

#DB 사용 lib
CLASSPATH=$CLASSPATH:$JEUS_DBLIB/ojdbc14.jar
#CLASSPATH=$CLASSPATH:$SERVER_LIB/xutil.jar
#CLASSPATH=$CLASSPATH:$JEUS_LIBS/x.jar
CLASSPATH=$CLASSPATH:$SERVER_CLASS
CLASSPATH=$CLASSPATH:$JEUS_LIBS/miplatform-3.2.jar


export CLASSPATH
