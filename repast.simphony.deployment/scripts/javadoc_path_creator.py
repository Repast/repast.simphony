# this script will create <sourcepath> and <classpath> elements for the
# suitable to include in an ant task.

import os

src_exclude = ["repast.simphony.bin_and_src", "repast.simphony.deployment",
               "repast.simphony.distributedBatch", "saf.core.ui", ".git",
               ".gitignore", ".DS_Store", "libs.bsf", "libs.piccolo"]

classpath_exclude = [".git",".gitignore", ".DS_Store"]

def run():
    print "<sourcepath>"
    for dir in os.listdir("../.."):
        if (not dir in src_exclude):
            path = "../%s/src" % dir
            if os.path.exists("../" + path):
                print ("\t<pathelement path=\"%s\"/>" % path)
    
    print "</sourcepath>"
    
    print "<classpath>"
    for dir in os.listdir("../.."):
        if (not dir in classpath_exclude):
            path = "../%s/lib" % dir
            if os.path.exists("../" + path):
                print ("\t<fileset dir=\"%s\">") % path
                print "\t\t<include name=\"**/*.jar\"/>"
                print ("\t</fileset>")
    
    print "</classpath>"
    


if __name__ == '__main__':
    run()