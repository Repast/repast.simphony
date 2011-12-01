#!/bin/bash
# fill in user specific data for local machine
echo "Hello world!" 
echo $1

stat_map="$2"

r.info map=$stat_map

exit 0