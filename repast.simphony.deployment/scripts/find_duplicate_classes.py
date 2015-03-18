#!/usr/bin/env python

import os, sys, zipfile

def process_jar(f, count_map):
    try:
        with zipfile.ZipFile(f) as f_zip:
            for z_info in f_zip.infolist():
                if len(z_info.filename) > 6 and z_info.filename[-6:] == '.class':
                    key = z_info.filename.replace("/", ".")
                    if key in count_map:
                        val = count_map[key]
                        val[0] = val[0] + 1
                        val[1].append(f)
                    else:
                        count_map[key] = [1, [f]]
    except zipfile.BadZipFile:
        print("Bad Zipfile {0}".format(f))
        

def run(target_dir):
    if not os.path.exists(target_dir):
            print("Error: %s does not exist" % target_dir)
    else:
        count_map = {}
        for root, dirs, files in os.walk(target_dir):
            #print(root, dirs, files)
            for f in files:
                if f[-3:] == 'jar':
                    process_jar(os.path.join(root, f), count_map)
                    
        
        for key, val in count_map.items():
            if val[0] > 1:
                print("{0}:\n\t{1}\n".format(key, '\n\t'.join(val[1])))

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: find_duplicate_classes.py 'target directory'")
    else:
        run(sys.argv[1])
