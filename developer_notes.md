# Repast Simphony Eclipse Develoment Environment

Some blurb about who should be using this...

Building Repast Simphony....

## Required components:

- Repast Simphony source 'development' branch: https://github.com/Repast/repast.simphony
- Repast Simphony feature and update site 'master' branch: https://github.com/Repast/repast.simphony.feature
  - Check this out locally to a different parent folder than the RS source code
    
- Eclipse Committers 2026-06

- The Workspace Java compiler level needs to be 1.8 to avoid Java module dependency issues. The user environment and JRE can still be compliance level 17+ and work OK even with the dev environment build using 1.8, as long as the dev JRE is 17+.

- Groovy Eclipse 4.40 (2026-06) plugin
  - https://groovy.jfrog.io/artifactory/plugins-release/e4.39
  - In the Install dialog, **uncheck "Group items by category"** to see the
    flat feature list, then install only:
      - Eclipse Groovy Development Tools (Compilerless)
      - Groovy Compiler 3.0
  - Do NOT install the "Main Package" / "Eclipse Groovy Development Tools" —
    that feature has a hard dependency on Groovy Compiler 5.0 and will pull
    it in, which causes a workspace/project compiler mismatch with the
    Repast-required Groovy 3.0.
  - With only the 3.0 compiler installed, the workspace compiler is
    unambiguously 3.0 and no manual switch is needed.
  - The plugin may be unsigned, so you will need to select all and Trust Selected.

 - Eclipse Graphical Modeling Framework (GMF) 
   - Install SDK and Runtime plugins

- Xpand SDK 2.2.0
   - Install Xpand SDK 2.2 from our update site mirror
   - https://web.cels.anl.gov/projects/Repast/xpand-mirror/
     - Install `Xpand SDK` feature at version `2.2.0` (this transitively
       installs all the Xpand/Xtend bundles Repast requires via
       `org.eclipse.xpand.all.feature`).
   - The plugin is unsigned, so you will need to select all and Trust Selected.
   - NOTE: The Xpand/Xtend plugins are no longer supported by Eclipse update sites which is why the Repast team provides this mirror that works with current Eclipse releases.

- Eclipse XML editors and tools

- Uninstall the WildWebDeveloper formatter and XML features (part of Eclipse default
  - Search for XML in "what's already installed"

- In Eclipse Java Compiler Error/Warning settings, change Forbidden access to WARNING

- Disable check for automatic updates in preferences
This is only on a per-workspace basis. To disable updates by default, edit the plugin_customization.ini in eclipse\plugins\org.eclipse.epp.package.committers_XXX as follows (set to false):
org.eclipse.equinox.p2.ui.sdk.scheduler/enabled=false

## Release Checklst

- [ ] Update version number in code
- [ ] Update year in license files
- [ ] Generate update site
- [ ] Sign the update site jars
- [ ] Create batch_runner_win.jar and batch_runner_osx.jar
- [ ] Create and sign Windows installer
- [ ] Create and sign Mac installer
- [ ] Test installers
- [ ] Build javadoc
- [ ] Build groovydoc
- [ ] Build asciidoc documents
- [ ] Stage Release on github
- [ ] Update repast.github.io
- [ ] Push live update site
- [ ] Github merge and release
- [ ] Mail list announcement

### Create batch runner jar files
- Create the batch_runner_osx.jar and batch_runner_win.jar for running the RS Batch standalone GUI from the shortcuts included with the RS installer.

- The ant script in r.s.distributed.batch/ui/dist/build_dist.xml will build the jars.  Normally you only need to provide the RS release version in the ant run dialog when building the jars.

### Create and sign Windows installer
- The Windows PowerShell script in r.s.deploymeny/scripts/create_eclipse_for_dist_windows.ps1 will create a fresh
  Eclipse + Repast Simphony and all depedencies from the update site all in one shot.
- After the Eclipse build step is tested, use the NSIS installer in r.s.deploymeny/installer_scripts to create
  the windows installer executable
- Sign the windows installer executable

### Building Repast Javadoc

1. **Run the Ant javadoc.xml in r.s.deployment using the Eclipse launch.**
     
   	- Update the javadoc title in javadoc.xml
   	- Check the link href to online Java API in javadoc.xml
   	- Make sure that links to Java base classes work in the generated javadoc.

2. **Run the buildGroovydocs ant script in r.s.relogo.runtime**
    
    - Groovydocs requires downloading a full Groovy SDK binary distribution. 
      This SDK is simply an archive with groovy jars that can be stored in some
      convienient system location, eg /libs.
      
    - The buildGroovyDocs.xml launch configuration Classpath settings must be
      updated to reflect the location of the unpacked groovy /libs folder.  
      These will be the "groovy-...jar" files and one or two of the other jars
      in the groovy /lib folder (see existing launch config).
      
        * Requires building with Groovy 2.5.x SDK since the generated doc HTML
        with version 3.x isn't compatible with the ReLogoReferenceCreator that 
        creates the RelogoPrimiatves.html.
      
    - The buildGroovyDocs.xml launch configuration JRE settings should be 
       "Separate JRE" not "Run in same JRE as workspace" since using the later
       causes issues (on Windows) even though they are the same JRE!
    
    - The version number in buildGroovyDocs.xml should be updated.

    - **The ReLogo API docs are generated in the following steps:**

      1. Use the existing Eclipse launch config to run buildGroovyDocs.xml.  The reference
        to specific Groovy JDK local libs is important.

        - The buildGroovyDocs.xml ant build file is used to create the Groovydoc API document.
        - This will create the docs/RelogoAPI folder with groovy doc html
        - This will copy the relevant parts of groovydoc into the Repast javadocs folder
          automatically. 

      2. Update the release version number in repast.simphony.relogo.util.ReLogoReferenceCreator 
          and then run it.
          
        - ReLogoPrimitives.html document generation (files in r.s.relogo.runtime):
          - there is an Excel file, separated into sheets for turtle, patch, link, 
            observer and utility primitives.
          - ReLogoReferenceCreator.groovy is used to parse the Excel file and to 
            create the primitives html file.
          - the primitives html file is then placed at the top level of the Repast 
            Simphony API documentation

        - The ReLogoReferenceCreator class has a number of tests within the reference 
          document creation:
          - check to see that the link exists in the ReLogo API
          - check to see which methods from the T,P,L,O,Us classes are not included 
            in the Primitives document

      3. Copy the RelogoPrimitives.html to the RepastSimphonyAPI base directory.

### Stage Release on github
- https://github.com/Repast/repast.simphony/releases
- Draft a new Release
- Do the github tag and release first so there is a release tag that will be associated
  with the release and also generate release notes.

### Update project website repast.github.io
- Update the Jekyll _config.yaml release version and date
- Update download.html with new links for installer, update sites, etc.

### Push live update site

- For the live update site (ANL Server) - When you commit to SVN it goes to the server, e.g. updating actually pushes the new updates site
- The RS update site URL is https://web.cels.anl.gov/projects/Repast/update_site/

  To upload a new update site, use sftp (winscp etc) to homes.cels.anl.gov (must use VPN need personal RSA key) and put the update site files in

  /nfs/pub_html/gce/projects/Repast/update_site

### Github Merge and Release
The following steps apply to the Repast code repository for development and master 
branches.  A similar process is used to tag other repository master branches. 
The additional repositories are: "RS Feature", "RS Models", and "RS Docs."

1. Branch "development" should be synchronized with the remote repository and
   contain the code for the release version.  If the repository has new commits 
   that should not be included in the release tag, you can simply select the 
   proper commit point in step 4. 
   
2. Switch to branch "master" and pull from the remote repository.

3. Merge branch "development" into master.

4. Create an annotated tag on branch master (as opposed to a lightweight tag, 
   see: http://git-scm.com/book/en/v2/Git-Basics-Tagging)

	 Create the tag ID (version) and message.  The tag is associated with a 
   particular commit which is selected in the list box here. 
	 
	 Note that the tag is only associated with a specific branch.  The same tag
	 cannot be used across multiple branches within a repository.  The only branch
	 that really needs tagging is "master."
	 
	 Select "Create tag and start push" to push the tag to remote.
	 
5. Push branch "master".

6. Finally, if the local tag is not pushed to remote, the 
   simple Git command "git push --follow-tags" will push the new tag correctly.
   It seems that the more general "git push --tags" is not recommended as this may
   push "lightweight"s tags not properly associated with a specific commit.
   
7. Switch back to branch "development"

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

#### Groovy Eclipse dependency handling in the update site

Two files control how the Groovy Eclipse plugin is resolved when a user installs
Repast Simphony: `site.xml` and `associateSites.xml` in `r.s.updatesite`.

- The feature (`repast.simphony.feature/feature.xml`) imports the Groovy features
  with a version range (`match="greaterOrEqual"`), not a pinned version, and does
  not name a specific Eclipse release.

- The Eclipse-release-specific selection comes entirely from the Groovy Eclipse
  repositories referenced in these two files:
    - `site.xml` lists each Groovy repo in a `<repository-reference>` element.
      These are compiled into `content.jar` at build time, so the site must be
      rebuilt (step 7) after changing them.
    - `associateSites.xml` lists the same repos in `<associateSite>` elements.
      This file is served as-is (not compiled) and lets p2 auto-discover the repos
      during install.

- Groovy Eclipse publishes a separate repository per Eclipse release
  (e4.39 = 2026-03, e4.40 = 2026-06, ...). Each build pins `org.eclipse.jdt.core`
  to one exact version, so p2 cannot install a Groovy build against the wrong
  Eclipse release and automatically resolves to the build matching the user's
  Eclipse. The greclipse feature versions are identical across releases (they
  differ only by qualifier), so this selection cannot be expressed as a version
  range in the feature — it depends only on which repos are listed here.

- To support multiple Eclipse releases, list every matching Groovy repo in both
  files. p2 prefers the newest and backtracks to an older one when the newer
  build's exact JDT version is not present in the user's Eclipse. When dropping
  support for an Eclipse release, remove its two entries.

#### Build javadocs and groovydocs

#### Create batch runner jars
TODO

#### Build macOS and Windows release installers
TODO

#### Github merge and release
TODO

#### CELS update site repo

#### Update project website repast.github.io

