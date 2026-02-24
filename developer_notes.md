## Repast Simphony Eclipse Develoment Environment

Some blurb about who should be using this...

Building Repast Simphony....

### Required components:

- Repast Simphony source 'development' branch: https://github.com/Repast/repast.simphony
- Repast Simphony feature and update site 'master' branch: https://github.com/Repast/repast.simphony.feature
  - Check this out locally to a different parent folder than the RS source code
    
- Eclipse Committers 2025-12

- The Workspace Java compiler level needs to be 1.8 to avoid Java module dependency issues. The user environment and JRE can still be compliance level 17+ and work OK even with the dev environment build using 1.8, as long as the dev JRE is 17+.

- Groovy Eclipse 4.38 (2025-12) plugin
  - https://groovy.jfrog.io/artifactory/plugins-release/e4.38
  - Install only "Main Package (required)"
  - The default Groovy Compiler is 3.0.X

 - Eclipse Graphical Modeling Framework (GMF) 
   - Install SDK and Runtime plugins

- Xpand SDK 2.2.0
   - Install Xpand SDK 2.2 from our update site mirror
   - https://web.cels.anl.gov/projects/Repast/xpand-mirror/
     - Install the "-2.2.0" plugins
   - NOTE: The Xpand/Xtend plugins are no longer supported by Eclipse update sites which is why the Repast team provides this mirror that works with current Eclipse releases.

- Eclipse XML editors and tools

- Uninstall the WildWebDeveloper formatter and XML features (part of Eclipse default
  - Search for XML in "what's already installed"

- In Eclipse Groovy settings, make sure compiler set to  3.0.x

- In Eclipse Java Compiler Error/Warning settings, change Forbidden access to WARNING

- Disable check for automatic updates in preferences
This is only on a per-workspace basis. To disable updates by default, edit the plugin_customization.ini in eclipse\plugins\org.eclipse.epp.package.committers_XXX as follows (set to false):
org.eclipse.equinox.p2.ui.sdk.scheduler/enabled=false

### Building the Repast Simphony Eclipse update site
Here we will build the Repast Simphony projects and build the update site jar files.

1. Refresh and clean the Eclipse developer workspace containing the RS source code and feature/update site projects.

2. If you have any new RS plugins that have not been part of the update site
before, you will need to set their build properties correctly. At the very
least, they should include META-INF/, bin/, icons/ (if there are any),
plugin-jpf.xml and lib (if there are any libraries).

3. If repast.simphony.demos or other projects are in the workspace, remove them. They need to be removed from the workspace directory entirely so they are not picked up by the deploy.xml.

4. Update the hard-coded Repast version number in all project files.  This is easiest done by just using Eclipse to find and replace, but manually review the changes before applying. These include for example:
    - In r.s.eclipse, change the REPAST_VERSION to the new version number. 
    - In r.s.eclipse.RepastSimphonyPlugin.java in r.s.eclipse
change the REPAST_SIMPHONY_PLUGIN_VERSION to the new version number.
    - Update the version number in repast.simphony.distributed.batch.ui/src-standalone StandAloneMain.
    - Update all of the plugin_jpf.xmls to the new version number
    - Update the plugin.restrict.simphony property in boot.properties in r.s.runtime with the new version number.
    - Update the feature version in feature.xml in r.s.feature in the overview tab.
      - In the Included plugins tab, click the versions button and select force feature version into plugin. The listed plugins should now be updated to the correct version number.

5. Update the license files in each project including the copyright date and any other language as may be needed.  Eclipse find and replace works great for updating the dates.

6. In r.s.deploymeny run deploy.xml as an Ant build to create the bin and src jar. You will be propmted to provide the version number of the **NEW** release version. If there is not an existing bin_and_src.jar when this is run, it will need to be run twice in order to get the bin_and_src.jar itself into the "RawClasspath".

7. In r.s.updatesite, open the site.xml file. Click "Build All" to create the site.
    - This will create the r.s.updatesite structure:
      - /plugins and contents
      - /features and content
      - artifacts.jar
      - content.jar
      - site.xml

8. Sign the update site jars using the JDK jarsigner and developer sigining certificate (not documented here).

#### Build javadocs and groovydocs

#### Create batch runner jars
TODO

#### Build macOS and Windows release installers
TODO

#### Github merge and release
TODO

#### CELS update site repo

#### Update project website repast.github.io

