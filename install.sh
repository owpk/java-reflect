#!/bin/bash
PWD=`pwd`
RUN_SCRIPT=$PWD/reflect
REFLECT_DOT=$HOME/.reflect

uninstall() {
   if [[ -f /usr/bin/reflect ]]
   then
      echo "removing /usr/bin/reflect"
      sudo rm /usr/bin/reflect
   fi

   if [[ -f $REFLECT_DOT ]]
   then
      echo "removing $REFLECT_DOT"
      rm -r $REFLECT_DOT
   fi

   if [[ -f $RUN_SCRIPT ]]
   then
      echo "removing $RUN_SCRIPT"
      rm $RUN_SCRIPT
   fi

   if [[ -f $PWD/reflect.jar ]]
   then
      echo "removing jar"
      rm $PWD/reflect.jar
   fi
}

install() {
   if [[ ! -d $REFLECT_DOT  ]]
   then
      echo "making direcory $REFLECT_DOT"
      mkdir $REFLECT_DOT
   fi

   ./mvnw clean package

   echo "copy jar to $REFLECT_DOT"
   cp reflect.jar $REFLECT_DOT

   echo "#!/bin/bash" > $RUN_SCRIPT
   echo "java -jar $REFLECT_DOT/reflect.jar \"\$@\"" >> $RUN_SCRIPT

   sudo chmod +x $RUN_SCRIPT

   echo "copy $RUN_SCRIPT to /usr/bin"
   sudo cp $RUN_SCRIPT /usr/bin
   reflect -h
}

if [[ $1 == '--uninstall' ]]
then
   uninstall
else
   install
fi

