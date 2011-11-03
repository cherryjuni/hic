export LANG=ko_KR.IBM-eucKR
export JDK_HOME=/usr/java5_64
export PATH=$PATH:$JDK_HOME/bin:.
export SERVER_LIB=/HIC_APP/daemon/lib
export SERVER_CLASS=/HIC_APP/daemon/class

export TODAY=`date +%m%d%Y`
export JAVA_RUNTIME_LOG_HOME=$BATCH_HOME/log


# LOG ���丮 ����
if [ ! -d ${JAVA_RUNTIME_LOG_HOME} ]; then
        mkdir ${JAVA_RUNTIME_LOG_HOME}
fi

# classpath ����

#DB ��� lib
CLASSPATH=$CLASSPATH:$SERVER_LIB/xutil.jar
CLASSPATH=$CLASSPATH:$SERVER_CLASS


export CLASSPATH
