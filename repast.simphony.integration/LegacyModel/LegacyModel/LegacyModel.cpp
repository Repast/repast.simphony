// LegacyModel.cpp : Defines the entry point for the console application.
//
// Developed my Michael J. North
// Copyright 2007. All rights reserved.
// Please see the Repast Simphony main license for usage details.

#include "stdafx.h"
#include <fstream>
#include <iostream>

using namespace std;

int _tmain(int argc, _TCHAR* argv[])
{
	double input1 = 0.0;
	double input2 = 0.0;

	ifstream inputFile;
	inputFile.open("input.txt", ios::in);
	inputFile >> input1;
	inputFile >> input2;
	inputFile.close();

	ofstream outputFile;
	outputFile.open("output.txt", ios::out);
	outputFile << (2 * input1);
	outputFile << endl;
	outputFile << (2 * input2);
	outputFile << endl;
	outputFile.close();

	return 0;
}

