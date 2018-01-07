/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LittleMeGame;

/**
 *
 * @author owoye001
 */
//this is the meaning of a question 
class Question {

    private final int firstNumber; // the first number

    private final int secondNumber; //the second number

    private final int opType; // the operation type

    private final int answerNumber; //the answer for the question

    final String[] operators = {"+", "-", "x", "/", "R"}; //stores the various types of math operators

    private final int charliesAnswer; //this stores charlies answers. The mad AI

    //constructor 
    public Question(int firstNumber, int secondNumber, int opType, int answerNumber, int charliesAnswer) {

        this.firstNumber = firstNumber;

        this.secondNumber = secondNumber;

        this.opType = opType;

        this.answerNumber = answerNumber;

        this.charliesAnswer = charliesAnswer; //bad AI's answer

    }

    //this returns the question 
    public String getQuestion() {
        return String.format(" %d    %s    %d    =    ", firstNumber, operators[opType], secondNumber);
    }

    //this returns the answer to the question
    public int getAnswer() {
        return answerNumber;
    }

    //this returns charlie's answer to the question
    public int getCharliesAnswer() {
        return charliesAnswer;
    }

}
