#! /bin/bash
# Script used to build new Repast Simphony eclipse distribution

set -eu

VERSION=2.12.0
TMP=$PWD/tmp
mkdir -p $TMP

ROOT=$PWD

# ARCH=x86_64
ARCH=aarch64
# remove old eclipse
rm -rf Eclipse.app
cp -r $HOME/tmp/eclipse-$ARCH/Eclipse.app ./Eclipse.app

# For fresh eclipse
#  NOTE !!!! THIS ECLIPSE NEEDS TO HAVE BEEN LAUNCHED AT LEAST ONCE -- OPEN IT IN THE DMG !!!!
# hdiutil attach -nobrowse -noverify $HOME/Downloads/eclipse-committers-2020-06-R-macosx-cocoa-x86_64.dmg
# cp -r /Volumes/Eclipse/Eclipse.app .
# hdiutil detach /Volumes/Eclipse/

# open -W ./Eclipse.app

# install Groovy and Repast plugins
# look at the features in a working install to get the feature names

# rm -rf $PWD/../Repast
# unzip ~/Downloads/$REPAST_ZIP -d $PWD/../


rm -rf $PWD/repast.simphony.updatesite
UPDATE_SITE_ZIP=$HOME/Downloads/repast.simphony.updatesite.2.12.0.zip
unzip $UPDATE_SITE_ZIP -d $PWD/

#rm -rf $PWD/xpand.mirror
#tar xf $HOME/Downloads/xpand-mirror.tgz -C $PWD/


# Groovy (e4.39) and the Xpand mirror are no longer listed here; the local update
# site's p2 repository references pull them in via -followReferences on the director
# calls below.
REPOSITORIES=https://download.eclipse.org/releases/2025-12,
# REPOSITORIES+=https://download.eclipse.org/eclipse/updates/4.20,
REPOSITORIES+=file://$PWD/repast.simphony.updatesite
#echo $REPOSITORIES

# Groovy and Xpand are no longer installed explicitly - the Repast feature now
# imports them and -followReferences resolves them from the update site's repository
# references, so they are pulled in transitively when SIMPHONY_FEATURES installs.
# GROOVY_FEATURES=org.codehaus.groovy30.feature.feature.group,
# GROOVY_FEATURES+=org.codehaus.groovy.eclipse.feature.feature.group

# org.eclipse.emf.ecore.source.feature.group
# org.eclipse.emf.compare.ide.ui.feature.group

# XPAND_FEATURES=org.eclipse.xpand.sdk.feature.group/2.2.0.v201605260315
# v201605260315

# #
SIMPHONY_FEATURES=repast.simphony.feature.feature.group

# GMF_FEATURES=org.eclipse.gmf.feature.group
# GMF_FEATURES+=org.eclipse.gmf.tooling.runtime.feature.group,
# GMF_FEATURES+=org.eclipse.wst.xml_ui.feature.feature.group

# GMF_FEATURES=org.eclipse.xpand.sdk.feature.group,
# GMF_FEATURES+=org.eclipse.gmf.feature.group,
# GMF_FEATURES+=org.eclipse.gmf.runtime.sdk.feature.group

WILDWEB=org.eclipse.wildwebdeveloper.feature.feature.group
WILDWEB_NODE=org.eclipse.wildwebdeveloper.embedder.node.feature.feature.group
M2E=org.eclipse.m2e.feature.feature.group,
M2E+=org.eclipse.m2e.pde.feature.feature.group,
M2E+=org.eclipse.m2e.lemminx.feature.feature.group

NEWS_FEED_FEATURE=org.eclipse.recommenders.news.rcp.feature.feature.group

export PATH=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin:$PATH
export JAVA_HOME=`/usr/libexec/java_home`

rm -f eclipse_install.log
WORKSPACE=$HOME/eclipse-workspace-032024
rm -rf $WORKSPACE
# Note that without -destination arg, tries to install the plugins in .eclipse
# -destination /Users/nick/Documents/SimphonyRelease/ReleaseWorkArea64/Eclipse.app
# -data $HOME/eclipse-workspace-06-2022
# Also logged to /Users/nick/eclipse-workspace-06-2022/.metadata/.log
open -W ./Eclipse.app --stderr eclipse_install.log --stdout eclipse_install.log --args -clean -purgeHistory \
    -consoleLog \
    -application org.eclipse.equinox.p2.director -repository $REPOSITORIES \
    -followReferences \
    -destination /Users/nick/Documents/SimphonyRelease/ReleaseWorkArea64/Eclipse.app \
    -data $WORKSPACE \
    -installIU $SIMPHONY_FEATURES

rm -rf $WORKSPACE
open -W ./Eclipse.app --stderr eclipse_install.log --stdout eclipse_install.log --args -clean -purgeHistory \
    -consoleLog \
    -application org.eclipse.equinox.p2.director -repository $REPOSITORIES \
    -followReferences \
    -destination /Users/nick/Documents/SimphonyRelease/ReleaseWorkArea64/Eclipse.app \
    -data $WORKSPACE \
    -uninstallIU $M2E

rm -rf $WORKSPACE
open -W ./Eclipse.app --stderr eclipse_install.log --stdout eclipse_install.log --args -clean -purgeHistory \
    -consoleLog \
    -application org.eclipse.equinox.p2.director -repository $REPOSITORIES \
    -followReferences \
    -destination /Users/nick/Documents/SimphonyRelease/ReleaseWorkArea64/Eclipse.app \
    -data $WORKSPACE \
    -uninstallIU $WILDWEB

rm -rf $WORKSPACE
open -W ./Eclipse.app --stderr eclipse_install.log --stdout eclipse_install.log --args -clean -purgeHistory \
    -consoleLog \
    -application org.eclipse.equinox.p2.director -repository $REPOSITORIES \
    -followReferences \
    -destination /Users/nick/Documents/SimphonyRelease/ReleaseWorkArea64/Eclipse.app \
    -data $WORKSPACE \
    -uninstallIU $WILDWEB_NODE

# News feed doesn't seem to be a feature in 4.20
# -uninstallIU $NEWS_FEED_FEATURE
#remove references to local Repast update site
# rm -f ./Eclipse.app/Contents/eclipse/configuration/*.log

sed -i'.temp' '/repast.simphony.updatesite/d' ./Eclipse.app/Contents/Eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.artifact.repository.prefs
sed -i'.temp' '/repast.simphony.updatesite/d' ./Eclipse.app/Contents/Eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.metadata.repository.prefs
rm ./Eclipse.app/Contents/Eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.artifact.repository.prefs.temp
rm ./Eclipse.app/Contents/Eclipse/p2/org.eclipse.equinox.p2.engine/profileRegistry/epp.package.committers.profile/.data/.settings/org.eclipse.equinox.p2.metadata.repository.prefs.temp

PREFS=Eclipse.app/Contents/Eclipse/configuration/.settings/org.eclipse.ui.ide.prefs
if [[ -f "$PREFS" ]]; then
    sed -i '.temp' '/RECENT_WORKSPACES=*/d' $PREFS
    rm ${PREFS}.temp
fi

# turn off auto updating
# The committers package folder is versioned (e.g.
# org.eclipse.epp.package.committers_4.38.0.20251204-0849), so locate the file by
# glob instead of hard coding the version.
PLUGIN_CUST_FILE=$(ls ./Eclipse.app/Contents/Eclipse/plugins/org.eclipse.epp.package.*/plugin_customization.ini)
sed -i'.temp' 's/org.eclipse.equinox.p2.ui.sdk.scheduler\/enabled=true/org.eclipse.equinox.p2.ui.sdk.scheduler\/enabled=false/g' $PLUGIN_CUST_FILE
rm ${PLUGIN_CUST_FILE}.temp


# # sign the native libraries
JARS=( "gluegen-rt-natives-macosx-aarch64" "gluegen-rt-natives-macosx-amd64" "jogl-all-natives-macosx-aarch64" "jogl-all-natives-macosx-amd64")

for j in ${JARS[@]}; do
    mkdir -p $TMP/$j
    cd $TMP/$j
    JAR=$ROOT/Eclipse.app/Contents/Eclipse/plugins/libs.ext_$VERSION/lib/$j.jar
    echo $JAR
    jar xf $JAR

    for f in **/**/*.dylib; do
        echo "Signing $f"
        codesign -f -s  "XX"  --timestamp --options=runtime -v $f
    done

    jar cfm $JAR META-INF/MANIFEST.MF .
done

cd $ROOT

# https://developer.apple.com/documentation/xcode/notarizing_macos_software_before_distribution/customizing_the_notarization_workflow#3087734
# ynhp-urdk-yhwe-gqtl
# codesign -f -s  "Developer ID Application: Nicholson Collier (7Y7ZD85R48)"  --timestamp --options=runtime -v Eclipse.app/Contents/Eclipse/configuration/org.eclipse.equinox.app
codesign -f -s "XX"  --entitlements entitlements.plist --timestamp --options=runtime -v Eclipse.app/
# verify (-v) the signature
codesign -v ./Eclipse.app
# more verification
spctl -a -v Eclipse.app

rm -f $ROOT/batch_runner.jar
BATCH_RUNNER_PATH=$HOME/Downloads/batch_runner.jar
cp  $BATCH_RUNNER_PATH  "$ROOT/batch_runner.jar"
codesign -f -s  "XX" --entitlements entitlements.plist --timestamp --options=runtime -v "$ROOT/batch_runner.jar"

#
# # to check if all repast.simphony.updatesite mentions were removed uncomment below
# grep -Rl "repast.simphony.updatesite" .
