/*
 * Copyright (C) 2017 owoye001
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Generla Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package jarvis.utilities;

import Behaviors.RoutinePersonalityBehavior;
import Conversation.SessionManager;
import datastructures.Question;
import jarvis.ProjectJarvis;
import jarvis.inpustream.ImageSlider;
import jarvis.inpustream.MainListener;
import jarvis.inpustream.TextEntry;
import java.util.Random;
import outputstream.BlackBoard;
import outputstream.MainPanel;
import outputstream.SystemSounds;

/**
 *
 * @author owoye001
 */
public class ScriptReader extends Thread {

    //public static boolean CommandExecuted = false; //command has not been executed
    ScriptReader subScriptReader = null; //calling script from a script.
    int currentQuestion = 0; //current question being answered. 
    Random random = new Random(); //random number generator.
    String[] regularLinesStringArray; //array of initial possible questions. 
    int numberOfLines; //total number of lines in the car text file 
    int numberOfPossibleQuestions; //number of possible questions 
    public Boolean ScriptStillRunning = false; //used to be static 
    public String scriptLocation = "";
    Question[] findCommonTopicArray; //common topics array.
    int numberOfPossibleCT; //number of possible common topics
    
    //data Location and object name
    String dataLocation = "";
    String nameLocation = "";

    public ScriptReader(String scriptLocation) {
        this.scriptLocation = scriptLocation; //location of script for this instance
    }

    //for B portion of the script reader.
    public ScriptReader(String scriptLocation, String findCommonTopicScriptLocation) {

        this(scriptLocation); //initialize script location.

        String[] rawData = Utilities.loadStrings(findCommonTopicScriptLocation);
        int countOfLines = rawData.length;
        numberOfPossibleCT = countOfLines / 6; //Common topics question

        //load questions into questions array
        findCommonTopicArray = new Question[numberOfPossibleCT];

        //loading possible questions into array.
        for (int i = 0; i < countOfLines;) {
            if (i % 6 == 0) {
                findCommonTopicArray[i / 6] = new Question(rawData[i++],
                        rawData[i++], rawData[i++],
                        rawData[i++], rawData[i++], rawData[i++]);
            }
        }
    }

    //default constructor.
    public ScriptReader() {

    }

    @Override
    @SuppressWarnings("empty-statement")
    public void run() {

        String nextInstructionLine; //next instruction line

        ScriptStillRunning = true;

        regularLinesStringArray = Utilities.loadStrings(scriptLocation);

        numberOfLines = regularLinesStringArray.length;

        numberOfPossibleQuestions = Utilities.countNumberOfActions(numberOfLines, regularLinesStringArray);

        //load questions into questions array
        Question[] regularQuestion = new Question[numberOfPossibleQuestions];

        int counter = 0; //counter to go through instruction
        String[] splitLines;

        //every line is a different command
        while (counter < numberOfLines && ScriptStillRunning == true) {

            //if out of line of script, AI should stop talking. 
            if (counter == numberOfLines) {
                ScriptStillRunning = false;
                continue; //go out of loop
            }

            nextInstructionLine = regularLinesStringArray[counter];

            switch (nextInstructionLine.substring(0, 1)) {
                case "A": //stands for excitment
                    //expecting four possible excitment statements
                    //separated by dots. 
                    splitLines = nextInstructionLine.substring(2).split("\\.");
                    String toSay = splitLines[random.nextInt(4)];
                    if (-1 != toSay.indexOf("[LAUGH]")) {
                        playLaughFile(); //laugh
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        }
                    } else {
                        BlackBoard.mainPanel.mouth.sayThisOnce(toSay + "!"); //say resposne.
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        } //time to catch up 

                    }

                    break;
                case "Q":
                    //stands for question
                    //add question to questions array
                    //Question 0, Possible Answer 1, Possible Answer 2, Possible Answer 3
                    //Possible Answer 4, Possible Answer 5
                    splitLines = nextInstructionLine.substring(2).split("\\.");
                    regularQuestion[currentQuestion] = new Question(splitLines[0],
                            splitLines[1], splitLines[2],
                            splitLines[3], splitLines[4], splitLines[5]);
                    
                    
                    //don't ask question on things we already know.
                    if (regularQuestion[currentQuestion].getQuestion().contains("your name")
                            && !"".equals(MainPanel.LastFacialRecognitionUsername)){
                    break;
                   
                   }


                    if (-1 != regularQuestion[currentQuestion].getQuestion().indexOf("[LAUGH]")) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        }
                        playLaughFile(); //laugh

                    }
                    
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true; //always a text entry, unless it is something. 
                    
                    BlackBoard.mainPanel.mouth.askQuestion(regularQuestion[currentQuestion].getQuestion().replace("[LAUGH]", "").replace("[AINAME]", SessionManager.mainListenerTextEars.AINAME)
                            .replace("[USER]", MainPanel.LastFacialRecognitionUsername)
                            + "? " + (random.nextBoolean() ? (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername) : ""));

                    break;
                case "U": //stands for user answer
                    //also takes a decision whether to take new script based on
                    //user new direction of topic
                   
                   if (regularQuestion[currentQuestion].getQuestion().contains("your name")
                           && !"".equals(MainPanel.LastFacialRecognitionUsername)){
                       currentQuestion++;
                       break; //we already have the name
                   }
                   
                   if (regularQuestion[currentQuestion].getQuestion().contains("your name")){
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.nameEntry = true; //enter texts.
                   }else{
                       SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                       BlackBoard.mainPanel.textEntry = true;
                   }

                    SessionManager.WaitForInputAndProcessIt();
                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }
                    
                    String commandOrTextToBeProcessed = BlackBoard.mainPanel.StringEntry;
                    
                     if ((commandOrTextToBeProcessed.equalsIgnoreCase("na")
                    || commandOrTextToBeProcessed.equalsIgnoreCase("no")
                    || commandOrTextToBeProcessed.equalsIgnoreCase("never")
                    || commandOrTextToBeProcessed.equalsIgnoreCase("definitely not"))
                    || commandOrTextToBeProcessed.equalsIgnoreCase("n")
                    || commandOrTextToBeProcessed.equalsIgnoreCase("no")
                    || commandOrTextToBeProcessed.toLowerCase().contains("no")
                    || commandOrTextToBeProcessed.toLowerCase().contains("nope")
                    || commandOrTextToBeProcessed.toLowerCase().contains("never") && BlackBoard.mainPanel.nameEntry == false) {
                BlackBoard.mainPanel.mouth.sayThisOnce("Okay, cool!");
                currentQuestion++;
                break;
            }

                    //manage user input only if script still running - stop talking to me
                    if (ScriptStillRunning == true) {
                        regularQuestion[currentQuestion].setUserAnswer(BlackBoard.mainPanel.StringEntry);
                        BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

                        //computer response begins here
                        Boolean foundKnowledgeInMemory = false;
                        //make sure script reader is not null - to prevent null pointer exception
                        if (subScriptReader == null) {
                            subScriptReader = new ScriptReader();
                        }

                        //loop through all the scripts in memory and 
                        for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {

                            //if current topic is different from new topic, switch to new topic
                            if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                                    || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptLocation)) {
                                foundKnowledgeInMemory = true;
                                if (subScriptReader.ScriptStillRunning == true) {
                                    subScriptReader.ScriptStillRunning = false; //terminate current sub script
                                }
                                subScriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                                subScriptReader.start();
                                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                                ScriptStillRunning = false; //kill old scripts
                                while (subScriptReader.isAlive()); //busy wait.
                                break;

                            } //end of if statement for checking topics

                        } //end of for loop

                        //if foundKnowledge of new topic in script, skip remaining
                        //part of current script and continue new script
                        if (foundKnowledgeInMemory == true) {
                            currentQuestion++;
                            break;
                        }

                        //if knowlege script cannot be found in memory
                        String response = regularQuestion[currentQuestion].getPossibleAnswers()[random.nextInt(5)];
                        if (-1 != response.indexOf("[LAUGH]")) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                            }
                            playLaughFile(); //laugh

                        } 
                        
                          BlackBoard.mainPanel.mouth.sayThisOnce(response
                                .replace("[LAUGH]", "")
                                .replace("[USER]", random.nextBoolean() ? (MainPanel.LastFacialRecognitionUsername.isEmpty()?"":MainPanel.LastFacialRecognitionUsername): "")
                                .replace("[YOURANSWER]", regularQuestion[currentQuestion].getUserAnswer())); //say resposne.
                        
                        currentQuestion++;
                        //end of managing user input
                    } //ensure that script is still running before 
                    //print nonsense on the screen
                    break;
                    
                case "F": //emergency image display
                    dataLocation = nextInstructionLine.substring(2);
                    ImageSlider go = new ImageSlider(dataLocation, "", 7); 
                    break;
                case "C": //computer response
                    splitLines = nextInstructionLine.substring(2).split("\\.");

                    String actionStatement = splitLines[random.nextInt(3)].replace("[USER]", MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername);

                    //CHECK IF IT A QUESTION, IF IT IS A QUESTION, THEN ASK A QUESTION
                    //AND EXPECT ANSWER
                    if (actionStatement.contains("?")) {

                        //QUESTION
                        if (-1 != actionStatement.indexOf("[LAUGH]")) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                            }
                            playLaughFile(); //laugh

                        }
                        
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true;
                        
                        BlackBoard.mainPanel.mouth.askQuestion(actionStatement.replace("[LAUGH]", "").replace("[AINAME]", SessionManager.mainListenerTextEars.AINAME) + " " + (random.nextBoolean() ? (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername) : ""));

                        //ANSWER
                        
                        SessionManager.WaitForInputAndProcessIt();
                        try {
                            SessionManager.input.join();
                        } catch (InterruptedException ex) {
                        }

                        //manage user input only if script still running - stop talking to me
                        if (ScriptStillRunning == true) {
                            BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

                            //computer response begins here
                            Boolean foundKnowledgeInMemory = false;
                            //make sure script reader is not null - to prevent null pointer exception
                            if (subScriptReader == null) {
                                subScriptReader = new ScriptReader();
                            }

                            //loop through all the scripts in memory and 
                            for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {
                                //if current topic is different from new topic, switch to new topic
                                if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                                        || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptLocation)) {
                                    foundKnowledgeInMemory = true;
                                    if (subScriptReader.ScriptStillRunning == true) {
                                        subScriptReader.ScriptStillRunning = false; //terminate current sub script
                                    }
                                    subScriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                                    subScriptReader.start();
                                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                                    ScriptStillRunning = false; //kill old scripts
                                    while (subScriptReader.isAlive()); //busy wait.
                                    break;

                                } //end of if statement for checking topics

                            } //end of for loop

                            //if foundKnowledge of new topic in script, skip remaining
                            //part of current script and continue new script
                            if (foundKnowledgeInMemory == true) {
                                break;
                            }

                            if (random.nextBoolean() && random.nextBoolean()) { 
                                SessionManager.mainListenerTextEars.PossibleNextQuestionToAsk();
                            }
                            //if knowlege script cannot be found in memory
                            //do nothing
                        } //END IF SCRIPTIS STILL RUNNING

                        break; //break switch
                    }

                    //IF IT A GREETING
                    if (actionStatement.contains("[GREETINGS]")) {
                        //greet the user
                        MainListener.CheckAndGreatAppropriate();

                        //ANSWER
                        if (ProjectJarvis.txtEntry != null) ProjectJarvis.txtEntry.setVisible(false); //kill existing thread
                        ProjectJarvis.txtEntry = new TextEntry();
                        ProjectJarvis.txtEntry.setVisible(true);

                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //enter texts.
                        MainListener.ScriptGreeting = true;
                        SessionManager.WaitForInputAndProcessIt();
                        try {
                            SessionManager.input.join();
                        } catch (InterruptedException ex) {
                        }
                        MainListener.ScriptGreeting = false;
                        //manage user input only if script still running - stop talking to me
                        if (ScriptStillRunning == true) {
                            BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

                            //computer response begins here
                            Boolean foundKnowledgeInMemory = false;
                            //make sure script reader is not null - to prevent null pointer exception
                            if (subScriptReader == null) {
                                subScriptReader = new ScriptReader();
                            }

                            //loop through all the scripts in memory and 
                            for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {

                                //if current topic is different from new topic, switch to new topic
                                if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                                        || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptLocation)) {
                                    foundKnowledgeInMemory = true;
                                    if (subScriptReader.ScriptStillRunning == true) {
                                        subScriptReader.ScriptStillRunning = false; //terminate current sub script
                                    }
                                    subScriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                                    subScriptReader.start();
                                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                                    ScriptStillRunning = false; //kill old scripts
                                    while (subScriptReader.isAlive()); //busy wait.
                                    break;

                                } //end of if statement for checking topics

                            } //end of for loop

                            //if foundKnowledge of new topic in script, skip remaining
                            //part of current script and continue new script
                            if (foundKnowledgeInMemory == true) {
                                continue;
                            }

                            //if knowlege script cannot be found in memory
                            //do nothing
                        } //END IF SCRIPTIS STILL RUNNING

                        break;
                    }

                    String whatToSay = actionStatement.replace("[USER]", "");
                    //IF IT IS NONE OF THE ABOVE
                    BlackBoard.mainPanel.mouth.sayThisOnce(whatToSay); //say resposne. 
                    break;
                case "W": //stand for wait for some time 
                    try {
                        Thread.sleep(Integer.parseInt(nextInstructionLine.substring(2)));
                    } catch (InterruptedException ex) {
                    } //time to catch up 
                    break;
                case "G": //stand for carousel seletion 
                    splitLines = nextInstructionLine.substring(2).split("\\<");
                    dataLocation = splitLines[0];
                    nameLocation = splitLines[1];
                    Thread carouselThread = new Thread() {
                        @Override
                        public void run() {
                            ImageSlider go = new ImageSlider(dataLocation, nameLocation, 1); //call first constructor

                            while (ImageSlider.CarouselFrame != null) {

                            }

                        }
                    };
                    carouselThread.start();

                    try {
                        carouselThread.join();
                    } catch (InterruptedException ex) {
                    }

                    break;
                case "M": //music feed 
                    splitLines = nextInstructionLine.substring(2).split("\\<");
                    dataLocation = splitLines[0];
                    nameLocation = splitLines[1];
                    Thread carouselThreadMusic = new Thread() {
                        @Override
                        public void run() {
                            ImageSlider go = new ImageSlider(dataLocation, nameLocation, 2); //call first constructor

                            while (ImageSlider.CarouselFrame != null) {
                            }
                        }
                    };
                    carouselThreadMusic.start();

                    try {
                        carouselThreadMusic.join();
                    } catch (InterruptedException ex) {
                    }
                    break;
                case "V": //video feed 
                    splitLines = nextInstructionLine.substring(2).split("\\<");
                    dataLocation = splitLines[0];
                    nameLocation = splitLines[1];
                    Thread carouselThreadVideo = new Thread() {
                        @Override
                        public void run() {
                            ImageSlider go = new ImageSlider(dataLocation, nameLocation, 3); //call first constructor

                            while (ImageSlider.CarouselFrame != null) {
                            }
                        }
                    };
                    carouselThreadVideo.start();

                    try {
                        carouselThreadVideo.join();
                    } catch (InterruptedException ex) {
                    }
                    break;
                case "B": //find a common topic from a txt file with thousands 
                    //of common topic - hahaha.
                    //find a common topic 
                    int selectRandomCommonTopic = random.nextInt(numberOfPossibleCT);
                    if (-1 != findCommonTopicArray[selectRandomCommonTopic].getQuestion().indexOf("[LAUGH]")) {
                        playLaughFile(); //laugh
                    }

                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true; //enter texts.
                    BlackBoard.mainPanel.mouth.askQuestion(findCommonTopicArray[selectRandomCommonTopic].getQuestion().replace("[LAUGH]", "") + "? " + (random.nextBoolean() ? (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername) : ""));

                    //EXPECT USER ANSER
                    //ANSWER
                    SessionManager.WaitForInputAndProcessIt();
                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }

                    //manage user input only if script still running - stop talking to me
                    if (ScriptStillRunning == true) {
                        findCommonTopicArray[selectRandomCommonTopic].setUserAnswer(BlackBoard.mainPanel.StringEntry);
                        BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

                        //computer response begins here
                        Boolean foundKnowledgeInMemory = false;
                        //make sure script reader is not null - to prevent null pointer exception
                        if (subScriptReader == null) {
                            subScriptReader = new ScriptReader();
                        }

                        //loop through all the scripts in memory and 
                        for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {

                            //if current topic is different from new topic, switch to new topic
                            if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                                    || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptLocation)) {
                                foundKnowledgeInMemory = true;
                                if (subScriptReader.ScriptStillRunning == true) {
                                    subScriptReader.ScriptStillRunning = false; //terminate current sub script
                                }
                                subScriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                                subScriptReader.start();
                                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                                ScriptStillRunning = false; //kill old scripts
                                while (subScriptReader.isAlive()); //busy wait.
                                break;

                            } //end of if statement for checking topics

                        } //end of for loop

                        //if foundKnowledge of new topic in script, skip remaining
                        //part of current script and continue new script
                        if (foundKnowledgeInMemory == true) {
                            continue;
                        }

                        //if knowlege script cannot be found in memory
                        BlackBoard.mainPanel.mouth.sayThisOnce(findCommonTopicArray[selectRandomCommonTopic].getPossibleAnswers()[random.nextInt(5)]);
                        //try to about about something else random 
                        
                          if (random.nextBoolean()) { 
                                SessionManager.mainListenerTextEars.PossibleNextQuestionToAsk();
                            }
                    } //END IF SCRIPTIS STILL RUNNING

                    break;
                case "T": //terminate script
                    break;
                case "K": //keyboard typing
                        //not yet done
                    break;
                    
                case "X": //stay behavior script 
                    splitLines = nextInstructionLine.substring(2).split("\\<");
                    dataLocation = splitLines[0];
                    nameLocation = splitLines[1];
                    String timeOfEvent = dataLocation; String command = nameLocation; //will accept runtime commands too
                    new RoutinePersonalityBehavior (command,timeOfEvent).startBehavior();
                    break;
                case "J": //tell a joke, then laugh. Ask user if they want to tell you a joke
                    
                    SessionManager.mainListenerTextEars.tellJoke(ProjectJarvis.HumourLevel, new Random().nextBoolean(), false);
                    
                    break;
                case "S": //script inside a script
                    if (subScriptReader != null) subScriptReader.ScriptStillRunning = false;
                    subScriptReader = new ScriptReader(nextInstructionLine.substring(2));
                    subScriptReader.start();

                    while (subScriptReader.isAlive()); //busy wait

                    break;
            }
            counter++; //move to next line

        }
        //counter = 0; //counter reset
        currentQuestion = 0;
        ScriptStillRunning = false;

        //BlackBoard.mainPanel.mouth.sayThisOnce("End of Conversation!");
        //try {Thread.sleep(2000);} catch (InterruptedException ex) {}
    }

    SystemSounds laugh; //sound initialization

    private void playLaughFile() {

        new Thread() {
            @Override
            public void run() {
                laugh = new SystemSounds("laugh");
                laugh.playsound();
            }
        }.start();

    }

    public static String likedPictureObjectName;

    /**
     *
     * @param toString the name of the file that was selected.
     */
    public static void updateLikedPictureObject(String toString) {
        likedPictureObjectName = toString;
        //BlackBoard.mainPanel.mouth.sayThisOnce("I see you have selected the " + likedPictureObjectName);
        //try {Thread.sleep(2000);} catch (InterruptedException ex) {}
    }

}
