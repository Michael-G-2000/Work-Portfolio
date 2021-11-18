//
//  main.cpp
//  S-DES
//
//  Created by Michael Giglio on 1/9/19.
//  Copyright © 2019 Michael Giglio. All rights reserved.
//

#include <iostream>

using namespace std;

/*
 * The following Arrays contain the permutations
 * and S-boxes that are used throughout the program.
 */
int IP [8] = {2,6,3,1,4,8,5,7};
int IPR [8] = {4,1,3,5,7,2,8,6};
int P10 [10] = {3,5,2,7,4,10,1,9,8,6};
int P8 [8] = {6,3,7,4,8,5,10,9};
int EP [8] = {4,1,2,3,2,3,4,1};
int P4 [4] = {2,4,3,1};

int S0 [4][4] = {
    {1,0,3,2},
    {3,2,1,0},
    {0,2,1,3},
    {3,1,3,2}
};

int S1 [4][4] = {
    {0,1,2,3},
    {2,0,1,3},
    {3,0,1,0},
    {2,1,0,3}
};
// End permutations and S-boxes

/*
 * The following are the fucntions uses throughout the program.
 * Explained in moer detail at the beginning of each functions.
 */
void calcKey(string key, string& K1, string& K2);
void SDES(string input, string K1, string K2);
string perm(string number, int perm[], int size);
string LS(string key, int shiftNum);
void split(string key, string& H1, string& H2, int size);
string XOR(string input1, string input2);
string Sbox(string input, int (*box)[4]);
string funcK(string input, string key);
string SW(string input);
bool isZeroOrOne(string input);
// End function list

// Main function
int main(int argc, const char * argv[]) {
    // Title message.
    cout << "S-DES Encryption/Decryption" << endl << endl;
    
    /*
     * Get the input that is going to be encrypted or decrypted.
     * Checks that the input is 8 bits long and are only 0's and 1's
     */
    string givenText = "";
    while (givenText.length() != 8 || !isZeroOrOne(givenText)) {
        cout << "Input: " << endl;
        cin >> givenText;
        
        if (givenText.length() != 8)
            cout << "ERROR: The input length must be 8" << endl;
    }
    
    /*
     * Get the key that is used.
     * K1 and K2 are created to hold the two separate keys needed during the process.
     * Checks that the key is 10 bits long and are only 0's and 1's
     */
    string key = "", K1, K2;
    while (key.length() != 10 || !isZeroOrOne(key)) {
        cout << "Key: " << endl;
        cin >> key;
        
        if (key.length() != 10)
            cout << "ERROR: The key length must be 10" << endl;
    }
    
    // Calculate the two keys from the original.
    calcKey(key, K1, K2);
    
    /*
     * Ask the user if they want to encrypt or decrypt.
     * Makes sure that the user enters a valid choice
     */
    int choice = 0;
    while (choice < 1 || choice > 2) {
        cout << "Do you want to: " << endl;
        cout << "1. Encrypt" << endl;
        cout << "2. Decrypt" << endl;
        
        // Get the users choice.
        cin >> choice;
        
        if (choice < 1 || choice > 2) {
            cout << "ERROR: Not a valid choice" << endl;
        }
    }

    // Display the input parameters.
    cout << "\nInput: " << givenText << endl;
    
    /* If else statement to encrypt or decrypt based on user selection.
     * Note that the only difference between encryption and decryption
     * is that K1 and K2 are switched. This is so that they are used in
     * the reverse order to encryption.
     */
    if (choice == 1) {
        // Display the instruction selected and encrypt.
        cout << "K1: " << K1 << endl;
        cout << "K2: " << K2 << endl;
        cout << "Instruction: Encrypt" << endl;
        SDES(givenText, K1, K2);
    } else {
        // Display the instruction selected and decrypt.
        cout << "K1: " << K2 << endl;
        cout << "K2: " << K1 << endl;
        cout << "Instruction: Decrypt" << endl;
        SDES(givenText, K2, K1);
    }
    
}

/*
 * ----- calcKey() -----
 *
 * -- Description
 * This function is used to calculate the two keys that will be used
 * in the encryption and decryption process. Using the method outlined
 * in the pdf.
 *
 * -- Inputs
 * Three inputs are taken by this function:
 * $ The key that was inputted by the user in the main() function.
 * $ The address to the K1 variable created in the main() function.
 * $ The address to the K2 variable created in the main() function.
 *
 * -- Outputs
 * This function outputs two keys that are stored in the K1 and K2
 * variables passed in by referance.
 */
void calcKey(string key, string& K1, string& K2) {
    // Permute all numbers of the key using the P10 array outlined above.
    key = perm(key, P10, 10);
    
    // Leftshift the key by one.
    key = LS(key, 1);
    
    /*
     * Select only 8 elements of the key using the P8 array outlined above.
     * The result is stored in the K1 varaible as the first key.
     */
    K1 = perm(key, P8, 8);
    
    // Leftshift the key by 2.
    key = LS(key, 2);
    
    /*
     * Select only 8 elements of the key using the P8 array outlined above.
     * The result is stored in the K2 varaible as the second key.
     */
    K2 = perm(key, P8, 8);
}
// end calcKey()

/*
 * ----- SDES() -----
 *
 * -- Description
 * This function is used to carry out the process of SDES as outlined
 * in the pdf. The output of the SDES will then be displayed to the screen.
 *
 * -- Inputs
 * Three inputs are taken by this function:
 * $ The input value to be encrypted or decrypted that was entered
 * by the user in the main() function.
 * $ The first key created by the calcKey() function.
 * $ The second key created by the calcKey() function.
 *
 * -- Outputs
 * The output of this function will be the encrypted ciphertext or
 * decrypted plaintext outputted to the screen.
 */
void SDES(string input, string K1, string K2) {
    
    // Permute the input using the IP array outlined above.
    input = perm(input, IP, 8);
    
    // Perform the k function on the input using the first key, K1.
    input = funcK(input, K1);
    
    // Switch the two halves of the input.
    input = SW(input);
    
    // Perform the k function on the input again using the second key, K2.
    input = funcK(input, K2);
    
    // Permute the input using the IPR array outlined above.
    input = perm(input, IPR, 8);
    
    // Outpute the result to the console.
    cout << "Output: " << input << endl;
}
// end SDES()

/*
 * ----- funcK() -----
 *
 * -- Description
 * This function is used to carry out the process of function k as outlined
 * in the pdf.
 *
 * -- Inputs
 * Two inputs are taken by this function:
 * $ The input value to be that function k will be performed on.
 * $ The key that will be used in a XOR function during the process.
 *
 * -- Outputs
 * The output of this function will be the an ecrypted first half of the
 * input that will be returned to the SDES() function.
 */
string funcK(string input, string key) {
    //Create variables to hold the different halfs of the original input.
    string half1, half2, half2Saved;
    
    //Splite the input and store each half in different varaibles.
    split(input, half1, half2, 8);
    
    /*
     * Copy half2 variable into the half2Saved variable so that its
     * orignal value can be preserved for later
     */
    half2Saved = half2;
    
    // Permute the and extend half2 using the EP array outlined above
    half2 = perm(half2, EP, 8);
    
    // XOR half2 with the key value.
    half2 = XOR(half2, key);
    
    // Create another two variables for when half2 is split again
    string half21, half22;
    
    // Splite half2 again and store the two values in seperate varaibles
    split(half2, half21, half22, 8);
    
    // Perform the Sbox() function on both halfs using the Sboxes outlined above
    half21 = Sbox(half21, S0);
    half22 = Sbox(half22, S1);
    
    // Clear half2 to receive new input
    half2 = "";
    
    // Combine half21 and half22 and store it in half2
    half2 += half21;
    half2 += half22;
    
    // Permute half2 using the P4 array outlined above
    half2 = perm(half2, P4, 4);
    
    // XOR half1 and half2 together
    half2 = XOR(half1, half2);
    
    // Clear the input variable to receive a new value
    input = "";
    
    // Combine half2 and half2Saved and store it in the input variable
    input += half2;
    input += half2Saved;
    
    // Return the input
    return input;
}
// end funcK()

/*
 * ----- XOR() -----
 *
 * -- Description
 * This function is used to perfrom a XOR on two binary strings of the
 * SAME length.
 *
 * -- Inputs
 * Two inputs are taken by this function:
 * $ The first string to be XORed.
 * $ The second string to be XORed.
 *
 * -- Outputs
 * The output of this function will be the the result of the XOR operation.
 */
string XOR(string input1, string input2) {
    // Create a string to hold the result.
    string temp;
    
    /*
     * For loop that loops through both strings and compares
     * the value of each string at element i.  If they are
     * the same then a 0 is added to the temp variable.  Otherwise
     * a 1 is added to the variable
     */
    for (int i = 0; i < input1.size(); i++) {
        if (input1[i] == input2[i]) {
            temp += "0";
        } else {
            temp += "1";
        }
    }
    
    // Return the result
    return temp;
}
// end XOR()

/*
 * ----- perm() -----
 *
 * -- Description
 * This function is used to permute a given string using a given a
 * permutation order.
 *
 * -- Inputs
 * Three inputs are taken by this function:
 * $ The input value to be permuted.
 * $ The array that contains the permutation order.
 * $ The size of the permutation array.
 *
 * -- Outputs
 * The output of this function will be the permuted input string.
 */
string perm(string number, int perm[], int size) {
    // Create a temporary value to hold the result
    string temp;
    
    /*
     * A for loop that takes the number at the element specified
     * by the permutation order.  -1 as added to the end because
     * array indexing starts at 0 whereas the permutation array
     * values start at 1.  Therefore, 1 has to be taken away from
     * the permutation number to stop the program taking the wrong
     * bit.
     */
    for (int i = 0; i < size; i++) {
        temp += number[perm[i]-1];
    }
    
    // Return the result.
    return temp;
}
// end perm()

/*
 * ----- Sbox() -----
 *
 * -- Description
 * This function is used to carry out the process of the Sbox in the SDES
 * process.
 *
 * -- Inputs
 * Two inputs are taken by this function:
 * $ The input value to be passed through the Sbox.
 * $ The Sbox to be used.
 *
 * -- Outputs
 * The output of this function will be two bits returned to funcK().
 */
string Sbox(string input, int (*box)[4]) {
    // Create two strings to hold the column and row values
    string row, col;
    
    // Take the first and fourth bit and store them in the row variable
    row += input[0];
    row += input[3];
    
    // Take the second and third bit and store them in the col variabl
    col = input.substr(1,2);
    
    // Create two int variables to hold the row and column values
    int r , c;
    
    /*
     * If statements used to store the index of the row value to access
     * the Sbox value.
     */
    if (row == "00")
        r = 0;
    else if (row == "01")
        r = 1;
    else if (row == "10")
        r = 2;
    else
        r = 3;
    
    /*
     * If statements used to store the index of the column value to access
     * the Sbox value
     */
    if (col == "00")
        c = 0;
    else if (col == "01")
        c = 1;
    else if (col == "10")
        c = 2;
    else
        c = 3;
    
    // Get the value stored in the Sbox located at the specified cell
    int result = box[r][c];
    
    // Turns the number retrieved back into binary form and returns it
    switch (result) {
        case 0:
            return "00";
            break;
            
        case 1:
            return "01";
            break;
            
        case 2:
            return "10";
            break;
            
        case 3:
            return "11";
            break;
        default:
            return "00";
            break;
    }
}
// end Sbox()

/*
 * ----- SW() -----
 *
 * -- Description
 * This function is used to switch two halves of a given input around.
 *
 * -- Inputs
 * One input is taken by this function:
 * $ The input value to be switched.
 *
 * -- Outputs
 * The output of this function will be the input value with its halves switched.
 */
string SW(string input) {
    // Create two temp variables to hold the halves
    string temp1, temp2;
    
    // Split the input and store them in the temp variables
    split(input, temp1, temp2, 8);
    
    // Clear the input to hold the result
    input = "";
    
    // Store the two halves in the reverse order in the input variable.
    input += temp2;
    input += temp1;
    
    // Return the switched input
    return input;
}
//end SW()

/*
 * ----- LS() -----
 *
 * -- Description
 * This function is used to carry out the Left shift process.
 *
 * -- Inputs
 * Two inputs are taken by this function:
 * $ The key value to be shifted.
 * $ The amount of times to be shifted.
 *
 * -- Outputs
 * The output of this function will be a single string
 * with both halves shifted.
 */
string LS(string key, int shiftNum) {
    // Create two variables to hold the split input
    string half1, half2;
    
    // Split the key into its halves and store them in the varaibles
    split(key, half1, half2, 10);
    
    /*
     * For loop to shift the value to the left.
     * Repeats the amount of times specified by the shiftNum variable
     */
    for (int i = 0; i < shiftNum; i++) {
        rotate(half1.begin(), half1.begin()+1, half1.end());
        rotate(half2.begin(), half2.begin()+1, half2.end());
    }
    
    // Clear key to hold the new values
    key = "";
    
    // Combine the new halfs back into a single string
    key += half1;
    key += half2;
    
    // Return the shifted key.
    return key;
}
// end LS()

/*
 * ----- split() -----
 *
 * -- Description
 * This function is used to split a given string in half.
 *
 * -- Inputs
 * Four inputs are taken by this function:
 * $ The input value to be split.
 * $ A variable to store the first half of the string.
 * $ A variable to store the second hald of the string.
 * $ The size of the string to be split.
 *
 * -- Outputs
 * The output of this function will be two variables each containing
 * a half of the orignal string.
 */
void split(string key, string& H1, string& H2, int size) {
    
    /*
     * A for loop that repeats for the size of the orignal string.
     * If statement is used to check which half of the input i is
     * in and stores the i element of the input in either H1 or H2
     * variable.
     */
    for (int i = 0; i < size; i++) {
        if (i > ((size/2)-1)) {
            H2 += key[i];
        } else {
            H1 += key[i];
        }
    }
}
// end spit()

/*
 * ----- isZeroOrOne() -----
 *
 * -- Description
 * This function is used to check that the inputs entered are made up
 * of only 0's and 1's
 *
 * -- Inputs
 * One input is taken by this function:
 * $ The string that needs to be checked.
 *
 * -- Outputs
 * This function will return false if any bits are neither a 0 or 1 or
 * will return true if all bits are either 0's or 1's.
 */
bool isZeroOrOne(string input) {
    
    /*
     * A for loop that looks at every bit of input to check that it is
     * a 0 or 1.
     */
    for (int i = 0; i < input.length(); i++) {
        if (!(input[i] == '0' || input[i] == '1')) {
            cout << "ERROR: Can only contain 0's or 1's" << endl;
            return false;
        }
    }
    
    return true;
}
//end isZeroOrOne()
