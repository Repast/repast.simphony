#!/bin/bash

#SBATCH --job-name=<Job_Name>
#SBATCH --partition=<Queue_Name>
#SBATCH --nodes=<nodes>
#SBATCH --ntasks-per-node=<ppn>
#SBATCH --time=<walltime>
#SBATCH --output=output.txt
#SBATCH --mail-user=<email>
#SBATCH --mail-type=all

cd $SLURM_SUBMIT_DIR

ROOT=$( /bin/pwd )
PARAMETER_FILE=$ROOT/unrolledParamFile.txt
TOTAL_LINES=`wc -l $PARAMETER_FILE | cut -f1 -d' '`
echo "Total lines: $TOTAL_LINES"

PROCS=$(($SLURM_NTASKS_PER_NODE * $SLURM_JOB_NUM_NODES))
if [ "$TOTAL_LINES" -lt "$PROCS" ]
then
    echo "Number of allocated processes ($PROCS) is greater than the number of parameter combinations to run ($TOTAL_LINES). Nu\
mber of processes is equal to --nodes ($SLURM_JOB_NUM_NODES) * --ntasks-per_node ($SLURM_NTASKS_PER_NODE). Aborting job."
exit
fi

echo "Total Processes: $PROCS"

srun -n $PROCS --ntasks-per-node=$SLURM_NTASKS_PER_NODE $SLURM_SUBMIT_DIR/repastwrapper_slurm.sh $TOTAL_LINES $PARAMETER_FILE

