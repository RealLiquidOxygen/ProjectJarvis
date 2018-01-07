/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LittleMeGame;

import Conversation.SessionManager;
import jarvis.inpustream.MainListener;
import jarvis.utilities.Utilities;
import java.awt.AWTException;
import java.security.SecureRandom;

import java.awt.Robot; //to simulate key press events
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;
import outputstream.BlackBoard;
import outputstream.MainPanel;
import outputstream.SystemSounds;

/**
 *
 * @author owoye001
 */
//this is littleme the game
public class LittleMeGameForGUI {

    String currentWord;

    final String[] operators = {"+", "-", "x", "/", "R"}; //stores the various types of math operators

    SecureRandom random = new SecureRandom(); //generates random secured numbers

    int opType; //stores the random operator type generated

    int firstNumber; //first number used in a mathematical operation

    int secondNumber; //second number used in a mathematical operation

    int answerNumber; //this stores the answer for the question 

    int MAX_QUESTIONS = 100000; // maximum number of questions to be generated 

    int i = 0; // counter for question generator

    int right = 0, wrong = 0, skipped = 0, count = 0; // right answer and wrong answer counter

    int rightC = 0, wrongC = 0, skippedC = 0, countC = 0; // right answer and wrong answer counter for computer 

    int RandomQuestionNumber; // store random question number

    int Number_of_question = 0; // number of question user wants to answer.

    Question[] questions = new Question[MAX_QUESTIONS]; // arrays of questions 

    Question currentQuestion; // this is the current question.

    String current_Question_String; //this is the current question string.

    int currentAnswer; //this is my current Answer

    int usersAnswer; //this is users answer to the question 

    String Win_Lose_Status; // win or lose status

    //this is charlies section
    int charliesAnswer; //this stores charlies answers.

    Timer charlie; //control the charlie AI

    boolean charlieAnswered = false; //charlie did not answer yet. 

    boolean joeAnswered = false; //charlie little brother. A robot AI

    int charliesTime; //how long charlie wait before answer the question

    //SETTING GAME DIFFICULTY LEVEL HERE. CLS
    int easy = 15000; // time before the AI interfere

    int medium = 10000; // time before the AI interfere

    int hard = 6000; // time before the AI interfere

    //I AM GOING TO COUNT HOW MANY QUESTION EASY AI ANSWER'S USING THE INTEGERS BELOW
    int joeCount = 0, charlieCount = 0; //at the begging it is zero

    int joeCountR = 0, charlieCountR = 0; //count how many question the AI's get right

    String defValue; //for setting game difficult level
    
    public boolean TerminateAction = false; // to terminate the program.
    
    File rightPic = new File(Utilities.getJarParentDir() + "images/correct.PNG");
    
    File wrongPic = new File(Utilities.getJarParentDir() + "images/wrong.PNG");

    public LittleMeGameForGUI() {

        try {
        SwingUpdater littleme = new SwingUpdater();
        littleme.execute();
        } catch (Exception ef) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Error starting game!");
        }
    }

    //SWING WORKER CLASS HERE. 
    public class SwingUpdater extends SwingWorker<Void, String> {

        private String kidName = SessionManager.session.getStartSessionHuman().getObjectName();
        private String userOption;
        private String currentWord;
        private SystemSounds sound;
        boolean ActionRestart;

        @Override
        protected Void doInBackground() throws Exception {
          
            ActionRestart = false;

            BlackBoard.mainPanel.mouth.sayThisOnce("Welcome to Little Me");

            Message("Generating Questions", true); //for dramatic pause

            MessageNoName("Minimum numbers of questions allowed is 5 and maximum is " + MAX_QUESTIONS, true);

            MessageNoName("You will be playing against Little Me, The Computer, So good luck.... ", true); //jokes

            println("");

            defValue = QDef("What difficult level do you want (easy, medium, hard, robot)", true); //sets difficult level

            //setting game difficulty level here. 
            if (defValue.equalsIgnoreCase("easy")) {
                charliesTime = easy; //how long it takes before charlie types in the wrong or may right answer. Charlie is robot.
            } else if (defValue.equalsIgnoreCase("medium")) {
                charliesTime = medium; //control how long the robot takes before it interfered
            } else if (defValue.equalsIgnoreCase("hard")) {
                charliesTime = hard; //controls how long the robots takes before it interfered ///hard
            } else if (defValue.equalsIgnoreCase("robot")) //the AI's play against each other
            {
                println(""); // for formatting purpose only

                MessageNoName("Charlie and Joe plays against Little Me ... No input is required from the user ... ", true);

                println(""); // for formatting purpose only

                MessageNoName("The good things is that Little Me is very smart!", true);

                LastSleep(); //for dramatic effect

                charliesTime = 400; //prevent user input. 
            }

            println("");  //for formatting purpose only 

            println("");  //for formatting purpose only 

            if (!defValue.equalsIgnoreCase("robot")) // user does not need to be warned about the AIs/ROBOTS. 
            {

                MessageNoName("Charlie is the robot you want to watch out for because he enters some answer"
                        + "\n\n"  + "if you don't answer on time (" + charliesTime / 1000 + " seconds) ", true); //jokes

                println(""); //for formatting purposely.

                println("");  //for formatting purpose only 

                MessageNoName("Joe is charlie's brother and he is nice. He always enters the right answer."
                        + "\n\n"  + "if you don't answer on time (" + charliesTime / 1000 + " seconds) ", true); //jokes

                println(""); //for formatting purposely.

                println("");  //for formatting purpose only 

                MessageNoName("Sometimes you will get charlie, and other times you will get joe. So, we will never know ... ", true); //jokes

                println(""); //for formatting purposely.
            }

            //this generates 1000 possible questions with their answers
            do {
                opType = random.nextInt(5); //generates four types of operations

                charliesAnswer = random.nextInt(MAX_QUESTIONS); // charlies answers (a wrong answer to the question)

                if (opType == 0 || opType == 1) //addition and subtraction 
                {
                    if (opType == 1) {//subtraction
                        do {
                            firstNumber = 1 + random.nextInt(500); //generates the first number

                            secondNumber = 1 + random.nextInt(500); //generates the second number 

                        } while (firstNumber < secondNumber); //makes sure that first Number is greater than second number
                    } else //addition, not important
                    {
                        firstNumber = 1 + random.nextInt(500); //generates the first number 

                        secondNumber = 1 + random.nextInt(500); //generates the second number 
                    }

                    if (opType == 0) //addition 
                    {
                        answerNumber = firstNumber + secondNumber; //adds the first number to the second number 

                    } else if (opType == 1) //subtraction
                    {
                        answerNumber = firstNumber - secondNumber; //does the required subtraction 
                    }

                    questions[i] = new Question(firstNumber, secondNumber, opType, answerNumber, charliesAnswer); //creates a question object and stores it in an array 

                } else if (opType == 2) //multiplation 
                {

                    firstNumber = 1 + random.nextInt(15); //generates a random number 1-15

                    secondNumber = 1 + random.nextInt(15); //generates another random number 1-15

                    answerNumber = firstNumber * secondNumber; //multiplies the two numbers together and stores it

                    questions[i] = new Question(firstNumber, secondNumber, opType, answerNumber, charliesAnswer); //creates a question object and stores it in an array

                } else if (opType == 3) // division 
                {

                    do {
                        firstNumber = 2 + random.nextInt(5000); //generates a number between 2 and 5000

                        secondNumber = 2 + random.nextInt(50); //generates a number between 2 and 50

                    } while (firstNumber % secondNumber != 0); //ensures that number goes completely...no remainders

                    answerNumber = firstNumber / secondNumber; //calculate the answer for storage 

                    questions[i] = new Question(firstNumber, secondNumber, opType, answerNumber, charliesAnswer); //create a question object and stores it in an array

                } else if (opType == 4) //remainder 
                {

                    firstNumber = 1 + random.nextInt(500); //generates a number between 1 - 500

                    secondNumber = 1 + random.nextInt(15); // generates a number between 1 - 15

                    answerNumber = firstNumber % secondNumber; //caculate the answer

                    questions[i] = new Question(firstNumber, secondNumber, opType, answerNumber, charliesAnswer); //create a question object and stores it in an array 

                }

                i++; //increment i every time

            } while (i < MAX_QUESTIONS); //done generating question...let's play the game itself

            //GAME PLAY BEGINS HERE
            println(""); // for formatting purposes only

            //selecting question based on difficulty pick value
            if (!defValue.equalsIgnoreCase("robot")) {

                Question("Do you want to play the game (yes, no)", true); //asks a question

            } else if (defValue.equalsIgnoreCase("robot")) //robot playing 
            {
                Question("Do you want the robots to play the game (yes, no)", true); //asks a question
            }

            if (userOption.equalsIgnoreCase("yes")) {

                println(""); //for formatting purposes only 

                //selecting question based on difficulty pick value
                if (!defValue.equalsIgnoreCase("robot")) {

                    Number_of_question = NumberQuest(Number_of_question, "how many questions do you want to answer", true); //ask the user for the number of question

                } else if (defValue.equalsIgnoreCase("robot")) //robot playing 
                {
                    Number_of_question = NumberQuest(Number_of_question, "how many questions do you want them to answer", true); //ask the user for the number of question
                }

                             //he or she is interested in answering
                //forces uses to only be allowed maximum number of questions 
                if (Number_of_question > MAX_QUESTIONS) {

                    Number_of_question = MAX_QUESTIONS;

                    println("");

                    Message("Maximum number of questions allowed is " + MAX_QUESTIONS, true); // print out a message to the user

                } else if (Number_of_question < 5) {
                    Number_of_question = 5;

                    println("");

                    Message("Minimum number of question allowed is 5", true); //minimum number of questions allowed
                }

                println(""); //for formmating purpose only 

                Message("Excellent", true); //print out a message to the user 

                println(""); // for formatting purpose only

                BlackBoard.mainPanel.mouth.sayThisOnce("Here we go"); //print out a message to the user

                //THE GAME PLAY BEGINS HERE...THE LOOP FOR THE QUESTIONS 
                while (count < Number_of_question && TerminateAction == false) {
                    NewQuestion(); //generates a new question

                    AskQuestion(); //asks the question AND ACTIVATES CHARLIE

                    println(""); // for formatting purpose only.

                    //int right = 0, wrong = 0, skipped = 0, count = 0; // right answer and wrong answer counter
                    charlie.stop(); //STOPS THE AI'S - CHARLIE, JOE

                    if (charlieAnswered == true) //if the AI charlie answers the question then
                    {
                        println(""); //for formatting purpose only

                        MessageNoName("Charlie answered and he said he's done doing grunt work for you", true); //print out a message without the users name 

                        println(""); //for formatting purpose only

                        charlieAnswered = false; //resets the variable.

                    } else if (joeAnswered == true) {
                        println(""); //for formatting purpose only

                        MessageNoName("Joe answered and he said you are welcome again", true); //print out a message without the users name 

                        println(""); //for formatting purpose only

                        joeAnswered = false; //resets the variable.
                    }

                    println("Checking Answer");

                    startToThink(); //checing answer 1-4secs

                    GradeAnswer(); //grades answers

                    println("Little Me is now answering his own question");

                    startToThink(); //this is the computer's turn

                    GradeAnswerComputer(); //computer answer gets graded here

                    println("");

                    MessageNoName("Number of questions left: " + (Number_of_question - count - 1), true);

                    println(""); //for formatting purpose only

                    count++;  //increase count by 1

                } //end of test question loop
                
                if (TerminateAction == false) {

                //Print out the result of the game
                PrintResults(); //prints result and determines the winner

                //ask user if they want to keep the section open
                println(""); //for formatting purpose only 
                
             }

                ResetGame(); //resets the game

                ActionRestart = (true);

            } //maxium number of questions allowed is 1000
            else {
                println("");

                Message("Okay", true); //user interraction
            }

            ResetGame(); //resets the game


            //------------------------------------------------------------------------
            return null;
        }

        public void startToThink() {
            //-----------------------------------------
            currentWord = "\nThinking .";

            publish(currentWord);
            int number; //how long the timer will run
            do {
                number = random.nextInt(4000);
            } while (number <= 3000);

            SystemSounds calc = playsound("calculating"); //this plays the calculation sound
                
            Timer t = new Timer(3000, (ActionEvent ae) -> {
                currentWord += currentWord + ".";
                publish(currentWord); //update the current world
            });
            t.start(); //starting the timer here 

            //this causes the thread to sleep again
            try {
                Thread.sleep(number);

            } catch (InterruptedException ex) {

            }
            t.stop(); //stop the timer 
            calc.stopsound(); //tSystem.out.prihis stops the sound file
            currentWord = "\n"; //to tell the user that computing is done
            publish(currentWord);
            playsound("beep"); //a sound to say it's done.
        }

        @Override
        protected void process(final List<String> chunks) {
   
            for (final String string : chunks) {
                BlackBoard.mainPanel.mouth.printToScreen(string);

            }
            
            DefaultCaret caret = (DefaultCaret) MainPanel.jEditorPaneDisplayInfo.getCaret();

        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); //scroll down automatically
        }

        public void println(String text) {
            publish(text);

        }

        //This method is for asking questions for difficulty level for the game 
        public String QDef(String string, boolean GUI) {

            playsound("beep"); //plays a sound

            Boolean correct; //to check input

            do {

               // println("Okay " + kidName + ", " + string + " ? ");

                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                BlackBoard.mainPanel.textEntry = true;

                BlackBoard.mainPanel.mouth.askQuestion(0,"Okay " + kidName + ", " + string);

                
                SessionManager.WaitForInputAndProcessIt();
                 
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }
                
                userOption = BlackBoard.mainPanel.StringEntry;

               

                if (userOption.equalsIgnoreCase("robot") || userOption.equalsIgnoreCase("easy") || userOption.equalsIgnoreCase("medium") || userOption.equalsIgnoreCase("hard")) {
                    correct = true;
                } else {
                    correct = false;

                    println(""); //for formatting purposes only 

                    playsound("wrong");

                    InfoS("Invalid Input");

                    println(""); //for formating purposes only
                }
            } while (correct == false);

            println(userOption);  //for formating purposes only

            return userOption; //returns user option

        }

        //the user gets graded here
        private void GradeAnswer() {
            if (usersAnswer == currentAnswer) //checking answers from user
            {
                println(""); //for formmating purpose only
                if (!defValue.equalsIgnoreCase("robot")) //not robot playing 
                {
                    Message("that's correct", true); //that is correct

                } else //robot playing
                {
                    MessageNoName("Charlie and Joe just got another point!", true); //that is correct
                }
                 try {
                    MainPanel.displayInfo.displayImage(rightPic.toURI().toURL());
                } catch (MalformedURLException ex) {
                }
                playsound("beep");
                right++; // one question correct
            } else {

                println(""); //for formatting purpose only
                if (!defValue.equalsIgnoreCase("robot")) //not robots playing 
                {
                    Message("maybe another time", true); //not a robot line miss statement  
                } else {
                    MessageNoName("maybe another time", true); //not a robot line miss statement
                }
                
                try {
                    MainPanel.displayInfo.displayImage(wrongPic.toURI().toURL());
                } catch (MalformedURLException ex) {
                }

                println(""); //for formatting purpose only

                println(""); //for formmating purpose only
                if (!defValue.equalsIgnoreCase("robot")) //not robots playing 
                {
                    Message("Correct Answer: " + currentAnswer, true); //not robot, display statment 
                } else {
                    MessageNoName("Correct Answer: " + currentAnswer, true); //this line will not display the user's name
                }
                playsound("wrong"); //wrong buzzer sound
                wrong++;
            }

            println(""); //for formmatting purpose only. 
        }

        //LittleMe the computer, gets graded here. 
        private void GradeAnswerComputer() {

            boolean rightORwrong; // right or wrong 
            if (!defValue.equalsIgnoreCase("robot")) {
                rightORwrong = random.nextBoolean(); //random generation of booleans
            } else // robot
            {
                rightORwrong = (random.nextBoolean() && random.nextBoolean()) || (random.nextBoolean() && random.nextBoolean()); // to ensure that it does not always win #ROBOT
            }
            if (rightORwrong) //checking answers from computer. Right answer.
            {
                println(""); //for formmating purpose only
                 try {
                    MainPanel.displayInfo.displayImage(rightPic.toURI().toURL());
                } catch (MalformedURLException ex) {
                }
                MessageNoName("Info: Little Me answered his question correctly", true); //that is correct
                playsound("beep");
                rightC++; // one question correct
            } else //little me was wrong 
            {
                println(""); //for formatting purpose only
                 try {
                    MainPanel.displayInfo.displayImage(wrongPic.toURI().toURL());
                } catch (MalformedURLException ex) {
                }
                MessageNoName("Info: Little Me was wrong", true);
                playsound("wrong");
                wrongC++;
            }

            println(""); //for formmatting purpose only. 
        }

        //asks the question 
        private void AskQuestion() {

            //decides which AI GETS TURNED ON
            if (random.nextBoolean()) {
                CharlieAI();
            } //ACTIVATED, BAD CHARLIE
            else {
                JoeAI();
            } //NICE JOE GETS TURNED ON

            println("");

            usersAnswer = NumberQuest(usersAnswer, current_Question_String, count + 1, true); //ask the user the question. The zero does not do anything

        }

        //this method is designed to collect variable and do error checking for question asking
        public final int NumberQuest(Object var, String question, int question_number, boolean gui) {

            playsound("beep"); //plays a sound.

            println(""); //for formating purposes

            //print("Question " + question_number + ": " + question);

            //collect values here..do error checking
            Boolean Correct = false; //it is assuming that the user input is not correct, it has not been entered yet here

            do {
                try {
                    
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.numberEntry = true;

                    BlackBoard.mainPanel.mouth.askQuestion(0,"Okay " + kidName + ", " + "Question " + question_number + ": " + question);

                    SessionManager.WaitForInputAndProcessIt();

                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }

                    var = BlackBoard.mainPanel.NumberEntry;
                    
                    //var = Integer.parseInt(JOptionPane.showInputDialog(null, "Question " + question_number + ": " + question,
                           // "Little Me", JOptionPane.QUESTION_MESSAGE));
                    
                    println("Entered Answer: " + String.valueOf(var)); //print it out

                    Correct = true; //terminates error checking
                } catch (Exception e) {
                    println(""); //for formatting purposes only

                    playsound("wrong");

                    InfoS("Invalid Input", true);

                    println(""); //for formating purposes only

                    //println("Question " + question_number + ": " + question); //asks the question again
                }
            } while (Correct == false);

            return Integer.parseInt(var.toString()); //return the value for the variable as a integer
        }

        //CHARLIE, BAD CHARLIE AI
        public void CharlieAI() {

            //toss few right answer on purpose
            if (defValue.equalsIgnoreCase("robot")) {

                if (random.nextBoolean()) //random truth value
                {
                    charliesAnswer = currentAnswer; //correct
                    charlieCountR++;
                } else {
                    //charlie sticks with charlies answer and it doesn't change its mind.
                }
            }

            //charlie
            int countOfNumber = Integer.toString(charliesAnswer).length(); //length of currentAnswer from the current Question

            int[] numberToType = new int[countOfNumber]; //arrays of number for the robot to push

            try {
                for (int q = 0; q < countOfNumber; q++) {
                    numberToType[q] = Integer.parseInt(Integer.toString(charliesAnswer).substring(q, q + 1)); //obtaining digits
                }
            } catch (NumberFormatException e) {
                //in case of an error 
            }

            //charlies input 
            charlie = new Timer(charliesTime, (ActionEvent ae) -> {

                charlieAnswered = true; // to check if charlie answered. charlie answered the question.
                charlieCount++; //counts the numbers of the questions charlie answered.

                //playsound("suck"); //charlie talking

                try {
                    Robot robot = new Robot(); //CREATING A ROBOT OBJECT

                    for (int t = 0; t < countOfNumber; t++) //robot pushes the keys using for and switch statements
                    {
                        switch (numberToType[t]) {
                            case 1:
                                robot.keyPress(KeyEvent.VK_1);  //presses the keys
                                robot.keyRelease(KeyEvent.VK_1);  //releases the key
                                break;
                            case 2:
                                robot.keyPress(KeyEvent.VK_2); //presses the key
                                robot.keyRelease(KeyEvent.VK_2); //releases the key
                                break;
                            case 3:
                                robot.keyPress(KeyEvent.VK_3); //presses the key
                                robot.keyRelease(KeyEvent.VK_3); //releases the key 
                                break;
                            case 4:
                                robot.keyPress(KeyEvent.VK_4); //presses the key 
                                robot.keyRelease(KeyEvent.VK_4); //releases the key 
                                break;
                            case 5:
                                robot.keyPress(KeyEvent.VK_5); //presses the key 
                                robot.keyRelease(KeyEvent.VK_5); //releases the key 
                                break;
                            case 6:
                                robot.keyPress(KeyEvent.VK_6); //presses the key 
                                robot.keyRelease(KeyEvent.VK_6); //releases the key 
                                break;
                            case 7:
                                robot.keyPress(KeyEvent.VK_7); //presses the key 
                                robot.keyRelease(KeyEvent.VK_7); //releases the key 
                                break;
                            case 8:
                                robot.keyPress(KeyEvent.VK_8); //presses the key 
                                robot.keyRelease(KeyEvent.VK_8); //releases the key 
                                break;
                            case 9:
                                robot.keyPress(KeyEvent.VK_9); //presses the key 
                                robot.keyRelease(KeyEvent.VK_9); //releases the key
                                break;
                            case 0:
                                robot.keyPress(KeyEvent.VK_0); //same here 
                                robot.keyRelease(KeyEvent.VK_0); //same here 
                                break;
                        }
                    } //end of number press

                    //PRESS THE ENTER BUTTON - hehehe
                    robot.keyPress(KeyEvent.VK_ENTER); //presses the enter key 
                    robot.keyRelease(KeyEvent.VK_ENTER); //releases the enter key 

                } catch (AWTException e) {
                    //in case of exception 
                }
            });
            charlie.setRepeats(false); //no repeat
            charlie.start();
        }

        //JOE, BAD CHARLIE'S BROTHER...HE IS NICE. AI
        //I AM USING CHARLIE'S VARIABLES FOR THIS AI BECAUSE IT IS CONVINIENT AND IT SAVES SPACE FOR VARIABLE DECLARATIONS
        public void JoeAI() {

            //make user happy sometimes - toss them few right answer on purpose....tHIS IS JOES WORK
            charliesAnswer = currentAnswer; //not robot mode

            //toss few wrong answer on purpose ---robot mode
            if (defValue.equalsIgnoreCase("robot")) {

                if (random.nextBoolean()) //random truth value
                {
                    charliesAnswer = currentQuestion.getCharliesAnswer(); //incorrect answer

                } else {
                    joeCountR++; //correct answer
                }
            }

            //JOE 
            int countOfNumber = Integer.toString(charliesAnswer).length(); //length of currentAnswer from the current Question

            int[] numberToType = new int[countOfNumber]; //arrays of number for the robot to push

            try {
                for (int q = 0; q < countOfNumber; q++) {
                    numberToType[q] = Integer.parseInt(Integer.toString(charliesAnswer).substring(q, q + 1)); //obtaining digits
                }
            } catch (NumberFormatException e) {
                //in case of an error 
            }

            //JOE'S INPUT input 
            charlie = new Timer(charliesTime, (ActionEvent ae) -> {

                joeAnswered = true; // to check if charlie answered. charlie answered the question.
                joeCount++; //counts the number of questions joe answered

                //playsound("punch"); //JOE'S talking

                try {
                    Robot robot = new Robot(); //CREATING A ROBOT OBJECT

                    for (int t = 0; t < countOfNumber; t++) //robot pushes the keys using for and switch statements
                    {
                        switch (numberToType[t]) {
                            case 1:
                                robot.keyPress(KeyEvent.VK_1);  //presses the keys
                                robot.keyRelease(KeyEvent.VK_1);  //releases the key
                                break;
                            case 2:
                                robot.keyPress(KeyEvent.VK_2); //presses the key
                                robot.keyRelease(KeyEvent.VK_2); //releases the key
                                break;
                            case 3:
                                robot.keyPress(KeyEvent.VK_3); //presses the key
                                robot.keyRelease(KeyEvent.VK_3); //releases the key 
                                break;
                            case 4:
                                robot.keyPress(KeyEvent.VK_4); //presses the key 
                                robot.keyRelease(KeyEvent.VK_4); //releases the key 
                                break;
                            case 5:
                                robot.keyPress(KeyEvent.VK_5); //presses the key 
                                robot.keyRelease(KeyEvent.VK_5); //releases the key 
                                break;
                            case 6:
                                robot.keyPress(KeyEvent.VK_6); //presses the key 
                                robot.keyRelease(KeyEvent.VK_6); //releases the key 
                                break;
                            case 7:
                                robot.keyPress(KeyEvent.VK_7); //presses the key 
                                robot.keyRelease(KeyEvent.VK_7); //releases the key 
                                break;
                            case 8:
                                robot.keyPress(KeyEvent.VK_8); //presses the key 
                                robot.keyRelease(KeyEvent.VK_8); //releases the key 
                                break;
                            case 9:
                                robot.keyPress(KeyEvent.VK_9); //presses the key 
                                robot.keyRelease(KeyEvent.VK_9); //releases the key
                                break;
                            case 0:
                                robot.keyPress(KeyEvent.VK_0); //same here 
                                robot.keyRelease(KeyEvent.VK_0); //same here 
                                break;
                        }
                    } //end of number press

                    //PRESS THE ENTER BUTTON - hehehe
                    robot.keyPress(KeyEvent.VK_ENTER); //presses the enter key 
                    robot.keyRelease(KeyEvent.VK_ENTER); //releases the enter key 

                } catch (AWTException e) {
                    //in case of exception 
                }
            });
            charlie.setRepeats(false); //no repeat
            charlie.start();
        }

        //print out the result and the winner
        private void PrintResults() {
            println(""); // for formatting purposes only

            println("Generating results and determining winner");
            startToThink(); //1-4seconds

            println("");

            println("****************************************************"); //draws a line
            println("");
            println("                    GAME RESULTS");
            println("");
            println("****************************************************"); //draws another line

            println("");

            if (!defValue.equalsIgnoreCase("robot")) {

                println(kidName + " got " + (right - joeCount) + " question(s) right, but answered "
                        + (Number_of_question - (joeCount + charlieCount)) + " questions!"); //printing results

                println("");

                //joeCount=0, charlieCount=0;
                //PRINT OUT HOW MANY QUESTION THE TWO AI'S GOT RIGHT HERE....
                if (joeCount != 0) {
                    println("Joe answered " + joeCount + " question(s)!"); //printing results

                    println("");
                }
                if (charlieCount != 0) {
                    println("Charlie answered " + charlieCount + " question(s)!"); //printing results

                    println("");
                }

                println("Little Me got " + rightC + " question(s) right!"); //printing results

                println("");

                if (charlieCount != 0 || joeCount != 0) //just talking here...that's it. 
                {
                    println("Poor Little Me! He had no help!"); //user information. 

                    println("");
                }

            } else if (defValue.equalsIgnoreCase("robot")) //robot playing 
            {

                if (joeCount != 0) {
                    println("Joe answered " + joeCount + " question(s) and got " + joeCountR + " question(s) correctly!"); //printing results

                    println("");
                }
                if (charlieCount != 0) {
                    println("Charlie answered " + charlieCount + " question(s) and got " + charlieCountR + " question(s) correctly!"); //printing results

                    println("");
                }

                println("Charlie and Joe got " + (charlieCountR + joeCountR) + " question(s) correctly altogether!"); //user information. 

                println("");

                println("Little Me got " + rightC + " question(s) right!"); //printing results

                println("");

            } //END ROBOT IF 

            println("Total numbers of questions answered: " + Number_of_question); //printing results

            println("");

            DetermineWinner(); //determine winner

            println("****************************************************"); //draws a line
            println("                " + Win_Lose_Status);
            println("****************************************************"); //draws another line
        }

        //this method is designed to collect variable and do error checking
        public final int NumberQuest(Object var, String variableName, boolean GUI) {

            playsound("beep"); //plays a sound.

            println(""); //for formating purposes

            //Q(variableName); //asks the question

            //collect values here..do error checking
            Boolean Correct = false; //it is assuming that the user input is not correct, it has not been entered yet here

            do {
                try {
                    
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.numberEntry = true;

                    BlackBoard.mainPanel.mouth.askQuestion(0,"Okay " + kidName + ", " + variableName);

                    SessionManager.WaitForInputAndProcessIt();

                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }

                    var = BlackBoard.mainPanel.NumberEntry;
                    
                    //var = Integer.parseInt(JOptionPane.showInputDialog(null, "Okay " + kidName + ", " + variableName, "Little Me", JOptionPane.QUESTION_MESSAGE));

                    Correct = true; //terminates error checking
                } catch (Exception e) {
                    println(""); //for formatting purposes only

                    playsound("wrong");

                    InfoS("Invalid Input", true);

                    println("");//for formating purposes only

                    Q(variableName, true); //asks the question again
                }
            } while (Correct == false);

            return Integer.parseInt(var.toString()); //return the value for the variable as a integer
        }

        public void print(String text) {
           BlackBoard.mainPanel.mouth.printToScreen(text);
        }

        //this method asks yes or no question, with error checking. 
        public final void Question(String string, boolean GUI) {
            
            Boolean correct; //to check input

            userOption = ""; //resets the variable

            do {

                //Q(string);

                try {
                    
                     SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true;

                    BlackBoard.mainPanel.mouth.askQuestion(0,string);

                    SessionManager.WaitForInputAndProcessIt();

                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }

                    userOption = BlackBoard.mainPanel.StringEntry;

                    //userOption = JOptionPane.showInputDialog(null, string, "Little Me", JOptionPane.QUESTION_MESSAGE);

                } catch (Exception e) {
                    println(""); //for formatting purposes only 

                    playsound("wrong");

                    InfoS("Invalid Input", true);

                    println("");//for formating purposes only
                }

                if (userOption.equalsIgnoreCase("yes") || userOption.equalsIgnoreCase("no")) {
                    correct = true;
                } else {
                    correct = false;

                    println(""); //for formatting purposes only 

                    playsound("wrong");

                    InfoS("Invalid Input", true);

                    println(""); //for formating purposes only
                }
            } while (correct == false);
        }

        public void Message(String string, boolean GUI) {
            FirstSleep(); //for dramatic effect
            playsound("beep");
            println(string + ", " + kidName );
            LastSleep(); //for dramatic effect

        }

        public void Message(String string) {
            FirstSleep(); //for dramatic effect
            playsound("beep");
            println(string);
            //LastSleep(); //for dramatic effect

        }

        //Message to user
        public void MessageNoName(String string, boolean GUI) {
            FirstSleep(); //for dramatic effect
            playsound("beep"); //plays the beep sound 
            println(string );
            LastSleep(); //for dramatic effect

        }

        //This method provide information to the user - Short Information Message
        public void InfoS(String string, boolean GUI) {
            FirstSleep();
            playsound("beep");
            println("Hey " + kidName + ", " + string );
            LastSleep();
        }

        //this method resets the game 
        private void ResetGame() {
            //resetting i here.
            i = 0;
            right = 0;
            wrong = 0;
            skipped = 0;
            count = 0; // resetting counters
            rightC = 0;
            wrongC = 0;
            skippedC = 0;
            countC = 0; // resetting counters
            joeCount = 0;
            charlieCount = 0; //count the number of questions answered by both AI'S
            joeCountR = 0;
            charlieCountR = 0; //count how many question the AI's get right
            MainListener.littleme = null;
        }

        //This method is for asking questions 
        public void Q(String string, boolean GUI) {
            print("Okay " + kidName + ", " + string + " ? ");
        }

        //generates a new question 
        private void NewQuestion() {

            RandomQuestionNumber = random.nextInt(MAX_QUESTIONS); // generates a random question

            currentQuestion = questions[RandomQuestionNumber]; // this is my current question

            currentAnswer = currentQuestion.getAnswer(); //this is my current answer

            current_Question_String = currentQuestion.getQuestion(); // the string of my current question

            charliesAnswer = currentQuestion.getCharliesAnswer();  //return charlies answer
        }

        //determines who wins and who loses
        private void DetermineWinner() {

            if (!defValue.equalsIgnoreCase("robot")) {//NOT ROBOT

                if (right == rightC) {
                    Win_Lose_Status = "        DRAW"; // it is a draw
                } else if (right > rightC) // user got more point that the computer
                {
                    Win_Lose_Status = "        YOU WIN";
                } else if (right < rightC) // user got more point that the computer
                {
                    Win_Lose_Status = "LITTLE ME WINS";
                }

            } else if (defValue.equalsIgnoreCase("robot")) { //ROBOT MODE

                if (charlieCountR + joeCountR == rightC) {
                    Win_Lose_Status = "        DRAW"; // it is a draw
                } else if (charlieCountR + joeCountR > rightC) // charlie joe got more point than littleme 
                {
                    Win_Lose_Status = " CHARLIE AND JOE WINS";
                } else if (charlieCountR + joeCountR < rightC) // charlie joe got more point than littleme 
                {
                    Win_Lose_Status = "    LITTLE ME WINS";
                }

            }

        }

        private void Q(String variableName) {
            print("Okay " + kidName + ", " + variableName + " ? ");
        }

        private void LastSleep() {
             //Slows the the thread to give user time to read message.
        try {
            Thread.sleep(1000); //thread sleeps for 1000milliseconds
        } catch (InterruptedException ex) {
            //Logger.getLogger(Circle.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

        //slow down the thread for dramatic effect

    public  void FirstSleep() {
        //Slows the the thread to give user time to read message.
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            //Logger.getLogger(Circle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        

        private SystemSounds playsound(String soundname) {
           
         SystemSounds temp = new SystemSounds(soundname); //this acutally plays the sound 
          
         temp.playsound();
         
         return temp;

        }

        private void InfoS(String string) {
            FirstSleep();
        playsound("beep");
        BlackBoard.mainPanel.mouth.printToScreen("Hey " + kidName + ", " + string );
        LastSleep();
        }

       
    }
    
}
