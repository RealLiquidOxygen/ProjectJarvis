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
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package datastructures;

import java.util.Date;

/**
 *
 * @author owoye001
 * regular conversation question. 
 */
public class Question {
    private String question; //questions possible answers
    private String[] possibleAnswers = new String[5]; //questions possible answers
    private String userAnswer; //answer given by user
    private final String TimeStamp = new Date().toString(); 

    /**
     * 
     * @param string question
     * @param string0 possible answer 1
     * @param string1 possible answer 2
     * @param string2 possible answer 3
     * @param string3 possible answer 4
     * @param string4 possible answer 5
     */
    public Question(String string, String string0, String string1, String string2, String string3, String string4) {
       question = string;
       possibleAnswers[0] = string0;
       possibleAnswers[1] = string1;
       possibleAnswers[2] = string2;
       possibleAnswers[3] = string3;
       possibleAnswers[4] = string4;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the possibleAnswers
     */
    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }

    /**
     * @param possibleAnswers the possibleAnswers to set
     */
    public void setPossibleAnswers(String[] possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    /**
     * @return the userAnswers
     */
    public String getUserAnswer() {
        return userAnswer;
    }

    /**
     * @param userAnswers the userAnswers to set
     */
    public void setUserAnswer(String userAnswers) {
        this.userAnswer = userAnswers;
    }

    /**
     * @return the TimeStamp
     */
    public String getTimeStamp() {
        return TimeStamp;
    }
}
