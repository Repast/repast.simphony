#!/bin/bash

cd $SLURM_SUBMIT_DIR

TOTAL_RUNS=$1 # Total number of runs
PARAMETER_FILE=$2 # unrolled param file
INSTANCE="instance_"$SLURM_PROCID

PROCS=$(($SLURM_NTASKS_PER_NODE * $SLURM_JOB_NUM_NODES))
RUNS_PER_INSTANCE=$(($TOTAL_RUNS / $PROCS))
REMAINDER=$(($TOTAL_RUNS % $PROCS))

if [ "$SLURM_PROCID" -lt "$REMAINDER" ]
then
    RUNS_PER_INSTANCE=$(($RUNS_PER_INSTANCE + 1))
fi

BEGIN=$(( $SLURM_PROCID * $RUNS_PER_INSTANCE + 1 ))

if [ "$SLURM_PROCID" -ge "$REMAINDER" ]
then
    BEGIN=$(( $BEGIN + $REMAINDER ))
fi

END=$(( $BEGIN + $RUNS_PER_INSTANCE - 1 ))

echo "Instance_$SLURM_PROCID - RUNS_PER_INSTANCE: $RUNS_PER_INSTANCE, LINES: $BEGIN to $END"

mkdir $INSTANCE
cd $INSTANCE

sed -n "$BEGIN","$END"p "$PARAMETER_FILE" > localParamFile.txt
ln -s "../data" data

java -Xmx512m \
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
    -id $SLURM_PROCID \
    -pinput localParamFile.txt
