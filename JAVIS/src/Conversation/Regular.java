/*
 * Copyright (C) 2016 owoye001
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
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package Conversation;

import datastructures.MoodState;
import datastructures.Question;
import jarvis.inpustream.MainListener;
import jarvis.utilities.Utilities;
import java.util.Random;
import outputstream.BlackBoard;
import outputstream.MainPanel;

/**
 *
 * @author owoye001
 */
public class Regular {
    public static boolean CommandExecuted = false; //command has not been executed
    int currentQuestion = 0;
    Random random = new Random();
    public Regular() {
        InitiateBasicConveration();
    }

    private void InitiateBasicConveration() {
        new Thread() {
            @Override
            public void run() {
                //activate script still running
                MainListener.scriptReader.ScriptStillRunning = true;
                //detecting number of possible questions
                String[] regularLinesStringArray = Utilities.loadStrings("dialogs/generalconversation/generalconvo.txt");
                int numberOfLines = regularLinesStringArray.length;
                int numberOfPossibleQuestions = numberOfLines / 6;

                //load questions into questions array
                Question[] regularQuestion = new Question[numberOfPossibleQuestions];

                //loading possible questions into array.
                for (int i = 0; i < numberOfLines;) {
                    if (i % 6 == 0) {
                        regularQuestion[i / 6] = new Question(regularLinesStringArray[i++],
                                regularLinesStringArray[i++], regularLinesStringArray[i++],
                                regularLinesStringArray[i++], regularLinesStringArray[i++], regularLinesStringArray[i++]);
                    }
                }
                  
                
                while (currentQuestion < regularQuestion.length && MainListener.scriptReader.ScriptStillRunning == true) {

                    if (currentQuestion == regularQuestion.length){
                        MainListener.scriptReader.ScriptStillRunning = false;
                        continue;
                    }
                    
                    Thread wait = new Thread() {
                        @Override
                        public void run() {
                            
                            if (random.nextBoolean() && random.nextBoolean()){
                            BlackBoard.mainPanel.mouth.askQuestion(regularQuestion[currentQuestion].getQuestion() + ", " + MainPanel.LastFacialRecognitionUsername);
                            } else
                            {
                                BlackBoard.mainPanel.mouth.askQuestion(regularQuestion[currentQuestion].getQuestion());
           
                            }
                            resetTextEntryVariableTypesScreen();
                            BlackBoard.mainPanel.textEntry = true; //enter texts.
                            SessionManager.WaitForInputAndProcessIt();
                            try {SessionManager.input.join();} catch (InterruptedException ex) { }
                            regularQuestion[currentQuestion].setUserAnswer(BlackBoard.mainPanel.StringEntry);
                            BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);
                            String response = regularQuestion[currentQuestion].getPossibleAnswers()[random.nextInt(5)];
                            BlackBoard.mainPanel.mouth.sayThisOnce("For me ," + response); //say resposne.
                            try {Thread.sleep(2000);} catch (InterruptedException ex) {} //time to catch up 
                            //while (!CommandExecuted) {}
                            currentQuestion++;
                        }
                    };
                    wait.start();

                    try {
                        wait.join();
                    } catch (InterruptedException ex) {
                    }
                    
                    
                    //try {Thread.sleep(4000);} catch (InterruptedException ex) {} //time to catch up 
                    resetTextEntryVariableTypesScreen();
                }
                MainListener.scriptReader.ScriptStillRunning = false;
                BlackBoard.mainPanel.sessionManager.regular = null;
                //BlackBoard.mainPanel.mouth.sayThisOnce("End of Conversation!");
                BlackBoard.emotions.setMood(MoodState.TIRED);
                SessionManager.session.getHumanJarvis().setHumanmood(MoodState.TIRED);
                SessionManager.ChangeMoodBackToHappy();
            }

        }.start();
        /**
         * reset all variables to default value
         */

    }

    /**
     * reset text entry screen
     */
    void resetTextEntryVariableTypesScreen() {
        Utilities.resetTextCollectorScreen();
        CommandExecuted = true;
    }
}
