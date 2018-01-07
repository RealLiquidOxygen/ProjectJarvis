/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import Conversation.SessionManager;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.swing.Timer;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
public class RoutinePersonalityBehavior {

    private int year=0; 
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String commandToBeProcessed;

    /**
     * 
    * @param commandToBeProcessed the command for this trigger to execute
     * @param year this the year the event will trigger
     * @param month this is the month the event will trigger
     * @param day this is the day the event will trigger
     * @param hour this is the hour the event will trigger
     * @param minute this the minute the event will trigger
     * @param second this is the second the event will trigger
     */
    public RoutinePersonalityBehavior(String commandToBeProcessed, int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.commandToBeProcessed = commandToBeProcessed;
    }

    /**
     *
     * @param commandToBeProcessed the command for this trigger to execute
     * @param hour this is the hour the event will trigger
     * @param minute this the minute the event will trigger
     */
    public RoutinePersonalityBehavior(String commandToBeProcessed ,int hour, int minute) {
        this(commandToBeProcessed, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), hour, minute, 0);
    }
    
    public RoutinePersonalityBehavior(String commandToBeProcessed, String timeInput){
        NextAlarmTime = LocalDateTime.parse(timeInput);
         this.commandToBeProcessed = commandToBeProcessed;
    }
    
    boolean RoutineBehavior = true;

    private LocalDateTime NextAlarmTime;
 
    
    public void startBehavior() {

       
            //year, month, day, hour, minute, second
            if (year != 0) NextAlarmTime = LocalDateTime.of(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond());
            
            //if at the time the trigger start, the time already passed, wait till tomorrow.
            if (LocalDateTime.now().compareTo(getNextAlarmTime()) == 1) {
                NextAlarmTime = getNextAlarmTime().plus(1, ChronoUnit.DAYS);
            }
            
            //ySstem.out.println("Alarm Time: " + getNxetAlarmTime());
            Timer checkEvents = new Timer(120000, (ActionEvent e) -> {
                
                while (LocalDateTime.now().compareTo(getNextAlarmTime()) == 1) {
                    
                    NextAlarmTime = getNextAlarmTime().plus(1, ChronoUnit.DAYS);
                    
                    if (commandToBeProcessed.contains("cmd")) {
                        
                        try {
                            Runtime.getRuntime().exec(commandToBeProcessed);
                        } catch (IOException ex) {
                        }

                    } else { //process command
                        
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //needed to process command type
                        BlackBoard.mainPanel.StringEntry = commandToBeProcessed.toLowerCase(); //text message
                        new Thread() {
                            @Override
                            public void run() {
                                SessionManager.mainListenerTextEars.ProcessCommand(); //processes the command.
                            }
                        }.start();
                    }
                    break;
                }
            });
            checkEvents.setCoalesce(false);
            checkEvents.start();
            
         // while (isRoutineBehavior()) {
         //        }
       
            System.out.println ("INFO: BEHAVIOR THREAD INITIATED");
         
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * @return the hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * @return the minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * @return the second
     */
    public int getSecond() {
        return second;
    }

    /**
     * @return the RoutineBehavior
     */
    public boolean isRoutineBehavior() {
        return RoutineBehavior;
    }

    /**
     * @return the NextAlarmTime
     */
    public LocalDateTime getNextAlarmTime() {
        return NextAlarmTime;
    }

    /**
     * @return the commandToBeProcessed
     */
    public String getCommandToBeProcessed() {
        return commandToBeProcessed;
    }
}
