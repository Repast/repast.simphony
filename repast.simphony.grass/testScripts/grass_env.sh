# Need to modify this file for local testing on user's machine
UseLocation="$1"     # 1st argument, GRASS location
UseMapset="$2"         # 2nd argument, GRASS mapset
ExeFile="$3"         # 3rd argument, script file to execute

export GISDBASE=/usr/local/grassdata
export LOCATION_NAME=$UseLocation
export MAPSET=$UseMapset
export GRASS_GUI=text
export DIGITIZER=none
export GISBASE=/usr/local/grass-6.3.cvs
export GISRC=$HOME/.grassrc6

echo "GISDBASE: $GISDBASE"                > $GISRC
echo "LOCATION_NAME: $LOCATION_NAME"     >> $GISRC
echo "MAPSET: $MAPSET"                   >> $GISRC
echo "GRASS_GUI: $GRASS_GUI"             >> $GISRC
echo "DIGITIZER: $DIGITIZER"             >> $GISRC

export PATH=$PATH:$GISBASE/bin:$GISBASE/scripts
export LD_LIBRARY_PATH=/usr/local/bin:$GISBASE/lib

# Set the GIS_LOCK variable to current process id
GIS_LOCK=$$
export GIS_LOCK

# make the file executable and change group permissions
chmod 775 $ExeFile
chgrp medland $ExeFile

# run the script
if [ $ExeFile = "sim_display" ]; then
    source $ExeFile &
else
    source $ExeFile
fi

exit 0

