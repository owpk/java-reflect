#!/bin/bash
NAME=jreflect
PWD=`pwd`
RUN_SCRIPT=$PWD/$NAME
REFLECT_DOT=$HOME/.reflect
TARGET=/usr/bin/$NAME

uninstall() {
   if [[ -f $TARGET ]]
   then
      echo "found $TARGET"
      man $NAME | awk 'NR==4'
      echo "sure delete $TARGET? [y/n]"
      read delete
      if [[ ${delete,,} == "y" ]]
      then
         sudo rm $TARGET
      else
         echo "abort nstalling..."
         exit 1
      fi
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

compile() {
   ./mvnw clean package
}

install() {
   uninstall
   if [[ ! -d $REFLECT_DOT  ]]
   then
      echo "making direcory $REFLECT_DOT"
      mkdir $REFLECT_DOT
   fi

   compile

   echo "copy jar to $REFLECT_DOT"
   cp reflect.jar $REFLECT_DOT

   echo "#!/bin/bash" > $RUN_SCRIPT
   echo "java -jar $REFLECT_DOT/reflect.jar \"\$@\"" >> $RUN_SCRIPT

   sudo chmod +x $RUN_SCRIPT

   echo "copy $RUN_SCRIPT to /usr/bin"
   sudo cp $RUN_SCRIPT /usr/bin
   $TARGET -h
}

if [[ $1 == '--uninstall' ]]
then
   uninstall
elif [[ $1 == '--compile' ]]
then
   compile
else
   install
fi

