## Repast Simphony Eclipse Develoment Environment

### Required components:

- Repast Simphony 'development' branch
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