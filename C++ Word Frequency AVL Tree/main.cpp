//
//  main.cpp
//
//  Created by Michael Giglio on 3/5/19.
//  Copyright © 2019 Michael Giglio. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <set>
#include <queue>
#include <unordered_map>

#include "AVL_ADT.h"
#include "Word.h"

using namespace std;

/*
 * The following struct was found on the website stackoverflow under the question:
 * "Elegant ways to count the frequency of words in a file" at the link:
 * https://stackoverflow.com/questions/4888879/elegant-ways-to-count-the-frequency-of-words-in-a-file
 */
struct letter_only: ctype<char> {
    letter_only(): ctype<char>(get_table()) {}

    static ctype_base::mask const* get_table() {
        static vector<ctype_base::mask>
        rc(ctype<char>::table_size, ctype_base::space);

        fill(&rc['A'], &rc['z' +1], ctype_base::alpha);
        return &rc[0];
    }
};

struct DATA {
    string word;
    double frequency;
    set<string> foundIn;
};

void display();
void select();
void loadDict();
void constructAVLTree();
void loadFiles();
int constructFreqDict(string fileName);
void search();
void displayInfo(string word);
void addString(string word, string fileName, DATA& dataItem);
void deleteWords();

AvlTree<DATA, string> freqDict;
unordered_map<int, string> list;

int main(int argc, const char * argv[]) {

    select();

    return 0;
}

void display() {
    cout << "Menu:" << endl;
    cout << "1. Load frequency dictionary from save file" << endl;
    cout << "2. Create frequency dictionary from article files" << endl;
    cout << "3. Display the frequency diagram" << endl;
    cout << "4. Display the AVL tree" << endl;
    cout << "5. Search for a word/phrase" << endl;
    cout << "6. Save frequency dictionary to file" << endl;
    cout << "7. Delete words/phrases from the dictionary" << endl;
    cout << "8. Exit" << endl;
}

void select() {
    int selection = 0;

    while (selection != 8) {
        display();

        cout << "\n" << "What would you like to do?" << endl;
        cin >> selection;

        while (selection < 0 || selection > 8) {
            cout << "ERROR: not a valid selection" << endl;
            cout << "\n" << "What would you like to do?" << endl;
            cin >> selection;
        }

        switch (selection) {
            case 1:
                loadDict();
                break;

            case 2:
                loadFiles();
                break;

            case 3:
                freqDict.AVL_PrintContent();
                break;

            case 4:
                freqDict.AVL_Print();
                break;

            case 5:
                search();
                break;

            case 6:
                freqDict.AVL_Save();
                break;

            case 7:
                deleteWords();
                break;

            default:
                break;
        }
    }
}

void loadDict() {
    ifstream inFile;
    string line;
    string w;
    int words = 0;

    inFile.open("freqDict.txt");

    if (!inFile.is_open()) {
        cout << "freqDict.txt not found." << endl;
    } else {

        while (getline(inFile, line)) {
            DATA dataItem;

            istringstream iss(line);

            getline(iss, dataItem.word, ',');
            iss >> dataItem.frequency;

            while (iss >> w) {
                dataItem.foundIn.insert(w);
            }

            freqDict.AVL_Insert(dataItem);
            words++;
        }

        cout << "\n" << "Frequency Dictionary created with " << words << " words" << "\n" << endl;
    }

    inFile.close();
}

void loadFiles() {
    int totalWords = 0;

    for (int i = 0; i < 6; i++) {
        stringstream stream;
        stream << "Text_" << (i+1) << ".txt";
        totalWords += constructFreqDict(stream.str());
    }

    freqDict.AVL_CalcFreq(totalWords);

    cout << "\n" << "Frequency Dictionary created. \n" << endl;
}

int constructFreqDict(string fileName) {
    string word;
    string words[3];
    stringstream stream;
    ifstream inFile;
    double totalWords = 0;
    bool firstThree = true;
    int i = 0;

    /*
     * The following inFile.imbue command was found on the website stackoverflow
     * under the question:
     * "Elegant ways to count the frequency of words in a file" at the link:
     * https://stackoverflow.com/questions/4888879/elegant-ways-to-count-the-frequency-of-words-in-a-file
     */
    inFile.imbue(locale(locale(), new letter_only()));
    inFile.open(fileName);

    DATA dataItem;

    if(inFile.is_open()) {
        while (inFile >> word) {

            addString(word, fileName, dataItem);

            if (firstThree == true) {
                words[i] = word;
                if (i == 2) {
                    stream << words[0] << " " << words[1];
                    addString(stream.str(), fileName, dataItem);
                    stream.str("");

                    stream << words[0] << " " << words[1] << " " << words[2];
                    addString(stream.str(), fileName, dataItem);
                    stream.str("");
                    firstThree = false;
                }
                i++;
            } else {
                words[0] = words[1];
                words[1] = words[2];
                words[2] = word;

                stream << words[0] << " " << words[1];
                addString(stream.str(), fileName, dataItem);
                stream.str("");

                stream << words[0] << " " << words[1] << " " << words[2];
                addString(stream.str(), fileName, dataItem);
                stream.str("");
            }

            totalWords++;
        }
    } else {
        cout << fileName << " could not be opened and was not included "
        << "in the frequency table" << endl;
    }

    return totalWords;
}

void addString(string word, string fileName, DATA& dataItem) {

    if (freqDict.AVL_Retrieve(word, dataItem)) {
        freqDict.AVL_Update(word, fileName);
    } else {
        dataItem.word = word;
        dataItem.frequency = 1;
        dataItem.foundIn.insert(fileName);
        freqDict.AVL_Insert(dataItem);
    }
}

void search() {

    while (true) {
        string input;
        priority_queue<Word> results;


        cout << "Please enter what you would like to search, enter a number to display information about a result or % to exit" << endl;
        getline(cin, input);

        for (int i = 0; i < input.length(); i++) {
            if (input[i] == '%') {
                return;
            }

            if (input[i] == '1') {
                displayInfo(list.at(1));
                return;
            }

            if (input[i] == '2') {
                displayInfo(list.at(2));
                return;
            }

            if (input[i] == '3') {
                displayInfo(list.at(3));
                return;
            }

            if (input[i] == '4') {
                displayInfo(list.at(4));
                return;
            }

            if (input[i] == '5') {
                displayInfo(list.at(5));
                return;
            }
        }

        freqDict.AVL_Search(input, results);

        if (results.empty()) {
            cout << "There were no results found." << endl;
        } else {
            cout << "\n Top 5 Results: " << endl;

            for (int i = 0; i < 5; i++) {
                cout << left << i+1 << ". " << setw(20) << results.top().word;
                cout << " appears " << results.top().freq;
                cout << " per thousand words" << endl;
                list[i+1] = results.top().word;

                results.pop();

                if (results.empty())
                    break;
            }

            cout << endl;
        }
    }
}

void displayInfo(string word) {
    DATA dataItem;

    if(freqDict.AVL_Retrieve(word, dataItem)) {
        cout << "\n" << left << "Word: " << dataItem.word << endl;
        cout << left << "Frequency: " << dataItem.frequency << endl;
        cout << left << "Found In: ";

        set<string>::iterator it = dataItem.foundIn.begin();

        while (it != dataItem.foundIn.end()) {
             cout << *it << " ";
            it++;
        }
        cout << "\n" << endl;
    }
}

void deleteWords() {
    double threshold = 0;

    cout << "\nPlease enter a threshold for deletion (for example 1.4 per thousand words)" << endl;
    cin >> threshold;

    freqDict.AVL_ThresDelete(threshold);
}
