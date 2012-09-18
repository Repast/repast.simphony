#!/bin/bash

java -Xmx512m -cp "lib/*" repast.simphony.batch.ClusterOutputCombiner . combined_data
