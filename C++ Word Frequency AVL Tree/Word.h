//
//  Word.h
//
//  Created by Michael Giglio on 10/5/19.
//  Copyright © 2019 Michael Giglio. All rights reserved.
//

#ifndef Word_h
#define Word_h

#include <iostream>
#include <string>

using namespace std;

struct Word {
    string word;
    double freq;

    Word(string w, double f): word(w), freq(f) {}

    bool operator<(const Word& w) const {
        return freq < w.freq;
    }
};
#endif /* Word_h */
