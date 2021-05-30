#!/bin/bash
PWD=`pwd`
RUN_SCRIPT=$PWD/reflect
REFLECT_DOT=$HOME/.reflect

if [[ ! -d $REFLECT_DOT  ]]
then
   mkdir $REFLECT_DOT
fi

./mvnw clean package
cp reflect.jar $REFLECT_DOT

echo "#!/bin/bash" > $RUN_SCRIPT
echo "java -jar $REFLECT_DOT/reflect.jar" >> $RUN_SCRIPT

sudo cmod +x $RUN_SCRIPT
sudo cp $RUN_SCRIPT /usr/bin
reflect -h
