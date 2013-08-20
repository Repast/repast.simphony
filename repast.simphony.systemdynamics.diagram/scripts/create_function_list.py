import csv, shutil

NAME_COL = 0
NUM_ARGS = 11

def run():
    # copy existing one into scripts as a backup
    src = "../src/repast/simphony/systemdynamics/sheets/functions.txt"
    dst = "./functions_backup.txt"
    shutil.copyfile(src, dst)
    
    # put the existing functions into a map
    # so we can resuse their args
    func_map = dict()
    with open(dst, 'rb') as old_func:
        reader = csv.reader(old_func, delimiter=':')
        for row in reader:
            func_map[row[0].strip()] = row[1].strip()
    
    f = open("functions.txt", 'wb')
    with open('implementedFunctions.csv', 'rb') as csvfile:
        reader = csv.reader(csvfile)
        # skip header
        next(reader)
        for row in reader:
            func = row[0]
            arg = func_map.get(func)
            
            func_def = ''
            if arg != None:
                func_def = '{0} : {1} : common'.format(func, arg)
            else:
                arg_count = int(row[11])
                arg_def = ''
                if arg_count > 0:
                    arg_def = 'X'
        
                for x in range(1, int(row[11])):
                    arg_def += ', X' + str(x)
    
                func_def = '{0} : ({1}) : common'.format(row[0], arg_def)
            f.write(func_def + '\n')
    f.close()
    
if __name__ == '__main__':
    run()