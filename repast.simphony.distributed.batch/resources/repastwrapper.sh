#!/bin/bash

cd $PBS_O_WORKDIR

totr=$1 # total runs
rpi=$2 # runs per instance
fin=$3 # first instance number
param_file=$4 # unrolled param file

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
    inputArg=`sed -n "$begin","$end"p "$param_file"`
    mkdir $instanceDir
    cd $instanceDir
    # echo $inputArg
    java -Xmx512m -cp "../lib/*" repast.simphony.batch.InstanceRunner \
        ../scenario.rs/batch_params.xml ../scenario.rs "$inputArg" $instance
fi
