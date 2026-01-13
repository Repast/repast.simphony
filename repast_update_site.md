## Repast Simphony Eclipse Update Site

Repast Simphony is provided as an Eclipse update site for users who wish to install Repast into Eclipse directly. This is also the required installation process for linux or any other system for which an automated installer is not available. The update site can also be installed into Eclipse on a Windows or macOS system. 

Please follow these step to install Repast Simphony via the update site:

1. Check the Repast Requirements for obtaining a compatible Java Development Kit.

2. Download Eclipse IDE for Eclipse Committers 2025-12. Eclipse is provided as a .zip or .tar archive. Extract the eclipse archive to the desired location.

    **It is critical that the Eclipse Committers archive is unpacked to a folder to which the user has write access (i.e., it shouldn't be unpacked to a system folder). Do not use the Eclipse installer application (for any OS) since it will install Eclipse and the Repast plugins to different folders. The Repast plugins must be located in the eclipse/plugins folder.**

3. Use the Eclipse Update Manager (under Help -> Install New Software) to install Repast and required dependencies from their respective update sites.
    - Install Groovy Eclipse e4.38 from the update site:
        - https://groovy.jfrog.io/artifactory/plugins-release/e4.38
        - Install the Main Package: Eclipse Groovy Development Tools
    - Install Xpand SDK 2.2 from our update site mirror:
        - https://web.cels.anl.gov/projects/Repast/xpand-mirror/
        - NOTE: The Xpand/Xtend plugins are no longer supported by Eclipse update sites which is why the Repast team provides this mirror that works with current Eclipse releases.
    - Install Repast from the update site:
        - https://web.cels.anl.gov/projects/Repast/update_site
    - Uninstall Wild Web Developer
4. Once you have downloaded Eclipse and installed all required plugins, check that the groovy compiler version is set to 3.0.x in Preference -> Groovy -> Compiler. Other values may prevent Repast Simphony and ReLogo from working correctly.

5. The demonstration models are not provided via the update site, but they are available in the downloads section at the top of the page.

Previous versions of the Repast update site are available as archives via the github releases for the respective versions [here](https://github.com/Repast/repast.simphony/releases/). To install one of these archived update sites (repast.simphony.updatesite.[version].zip), select the "Archive" button and then select the downloaded zip file. 