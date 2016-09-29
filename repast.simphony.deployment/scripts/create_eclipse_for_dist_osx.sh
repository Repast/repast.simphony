#!/bin/bash
# Script used to build new Repast Simphony eclipse distribution
# For R.S. 2.4

# remove old eclipse
rm -rf Eclipse.app

# untar new eclipse into current directory
tar -xzvf ../../Downloads/eclipse-committers-neon-1-macosx-cocoa-x86_64.tar


# install Groovy and Repast plugins
# look at the features in a working install to get the feature names
REPOSITORIES=http://dist.springsource.org/snapshot/GRECLIPSE/e4.6,
REPOSITORIES+="file:///Users/nick/SimphonyRelease/repast.simphony.updatesite.2.4.0",
REPOSITORIES+=http://download.eclipse.org/releases/neon

GROOVY_FEATURES=org.codehaus.groovy18.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy20.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy21.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy22.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy23.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy24.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy.eclipse.feature.feature.group,
GROOVY_FEATURES+=org.codehaus.groovy.jdt.patch.feature.group

SIMPHONY_FEATURES=repast.simphony.feature.feature.group
GMF_FEATURES=org.eclipse.gmf.tooling.feature.group,
GMF_FEATURES+=org.eclipse.gmf.tooling.runtime.feature.group,
GMF_FEATURES+=org.eclipse.wst.xml_ui.feature.feature.group

set -x
open -W ./Eclipse.app --args -application org.eclipse.equinox.p2.director -repository $REPOSITORIES -installIU $GROOVY_FEATURES,$SIMPHONY_FEATURES,$GMF_FEATURES

# remove references to local Repast update site
rm ./Eclipse.app/Contents/eclipse/configuration/*.log
sed -i'.temp' '/repast.simphony.updatesite/d' ./Eclipse.app/Contents/eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.artifact.repository.prefs
sed -i'.temp' '/repast.simphony.updatesite/d' ./Eclipse.app/Contents/eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.metadata.repository.prefs
rm ./Eclipse.app/Contents/eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.artifact.repository.prefs.temp
rm ./Eclipse.app/Contents/eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.metadata.repository.prefs.temp

# to check if all repast.simphony.updatesite mentions were removed uncomment below
grep -Rl "repast.simphony.updatesite" .
