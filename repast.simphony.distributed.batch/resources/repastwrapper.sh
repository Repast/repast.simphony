#!/bin/bash

cd $SUBMIT_DIR

totr=$1 # total runs
rpi=$2 # runs per instance
fin=$3 # first instance number
paramFile=$4 # unrolled param file

instance=$((PBS_VNODENUM + fin))

instanceDir="instance_"$instance

begin=$(((instance * rpi) + 1))

if [ "$begin" -le "$totr" ]
    then
    end=$((begin + rpi - 1))
    if [ "$end" -gt "$totr" ]
        then
        end=$totr
    fi
    echo "Running lines $begin to $end"
    sed -n "$begin","$end"p "$paramFile" > localParamFile.txt
    mkdir $instanceDir
    cd $instanceDir
    ln -s "../data" data
    # echo $inputArg
    java -Xmx512m -XX:+IgnoreUnrecognizedVMOptions \
        --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
        --add-modules=ALL-SYSTEM \
        --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED \
        --add-exports=java.base/java.lang=ALL-UNNAMED \
        --add-opens java.base/java.util=ALL-UNNAMED \
        --add-exports=java.xml/com.sun.org.apache.xpath.internal.objects=ALL-UNNAMED \
        --add-exports=java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED \
        --add-opens java.base/java.lang=ALL-UNNAMED \
        -cp "../lib/*" repast.simphony.batch.InstanceRunner \
        -pxml ../scenario.rs/batch_params.xml \
        -scenario ../scenario.rs \
        -id $instance \
        -pinput localParamFile.txt
fi
