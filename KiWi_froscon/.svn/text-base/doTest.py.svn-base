#!/usr/bin/python
# This script purpose is to help you to detect any possible error and
# error reasons during the test run (ant doTests).
# the usage is simple : python doTest.py, this will call the ant doTest
# and after this will search in the output log for the tests with failures
# and will list them.

import os


def getOutput(file):
    lines = []
    while 1:
        line = file.readline()
        if (contains(line, "Total tests run:")) :
            lines.append(line)
        if (contains(line, "[echo] * running test suite")) :
            lines.append(line)

        if not line:
            break

    return lines


def contains(string, toSearch):
    return string.find(toSearch) >= 0

def isFailure(s) :
    return not contains(s, "Failures: 0") or not contains(s, "Skips: 0")

def isDescr(s) :
    return contains(s, "[echo] * running test suite")

def isException(s) :
    return contains(s, "[testng] Caused by:")


def getFailures(lines) :
    faliures=[]
    for s in lines:
        if (isFailure(s) and not isDescr(s)) or isException(s) :
            faliures.append(s)

    return faliures;


os.system("ant doTest > log.deleteMe")
file = open("log.deleteMe", "r")

lines = getOutput(file)
failures = getFailures(lines)
print "This test fails :"
for failure in failures :
    index = lines.index(failure)
    descIndex = index - 1;
    desc = lines[descIndex]
    failed = desc.split("test-build")[1];

    result = failed.replace(" ...", "")[1:];

    print result

print "Run this test alone to get more information about it"
file.close()
#os.remove("xxx")
