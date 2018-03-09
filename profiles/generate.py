import csv
import json
import os
import sys
import tempfile
from subprocess import call

'''
Basically, what this ugly piece of crap does is for each profile in the 
profiles.json file, it opens it up, generates everything using a seperate
Java tool, and does the necessary post-processing to make everything go backwards.
'''


def flush_out(string):
    print(string)
    sys.stdout.flush()


def join(file_name, tojoin):
    with open(file_name, 'a', newline='') as file, open(tojoin, 'r',
                                                        newline='') as file_to_join:
        r = csv.reader(file_to_join)
        w = csv.writer(file)

        # dump headers
        next(r)

        for row in r:
            w.writerow(row)


# invert things
def invert(filename):
    with \
            tempfile.NamedTemporaryFile(dir=".", delete=False, mode="w",
                                        newline='') as tmp, \
            open(filename, 'r', newline='') as f:
        r = csv.reader(f)
        w = csv.writer(tmp)
        header = next(r)
        w.writerow(header)
        for row in r:
            newrow = [row[0]]
            for item in row[1:]:
                newrow.append(str(-float(item)))
            w.writerow(newrow)
    os.replace(tmp.name, filename)


profiles = json.load(open('profiles.json'))

for (name, profile) in profiles.items():
    flush_out("Generating profile " + name)
    for i in range(len(profile['points']) - 1):
        flush_out("Generating segment " + str(i + 1))
        call(
            'java -jar profilegeneration.jar profiles.properties ' + name + str(
                i) + ' ' + str(profile['points'][i]['x']) + ' ' + str(
                profile['points'][i]['y']) + ' ' + str(
                profile['points'][i]['angle']) + ' ' + str(
                profile['points'][i + 1]['x']) + ' ' + str(
                profile['points'][i + 1]['y']) + ' ' + str(
                profile['points'][i + 1]['angle']), shell=True)

    flush_out("Joining segments")
    with open(name + "_left.csv", 'w', newline='') as left, \
            open(name + "_right.csv", 'w', newline='') as right:
        left.write(
            'dt,x,y,position,velocity,acceleration,jerk,heading' + os.linesep)
        right.write(
            'dt,x,y,position,velocity,acceleration,jerk,heading' + os.linesep)

    for i in range(len(profile['points']) - 1):
        join(name + '_left.csv', name + str(i) + '_left.csv')
        join(name + '_right.csv', name + str(i) + '_right.csv')

    for i in range(len(profile['points']) - 1):
        os.remove(name + str(i) + "_left.csv")
        os.remove(name + str(i) + "_right.csv")

    flush_out("Running post-processing")
    # swap left and right
    os.rename(name + '_left.csv', name + '.csv')
    os.rename(name + '_right.csv', name + '_left.csv')
    os.rename(name + '.csv', name + '_right.csv')

    if profile['flip']:
        invert(name + "_left.csv")
        invert(name + "_right.csv")