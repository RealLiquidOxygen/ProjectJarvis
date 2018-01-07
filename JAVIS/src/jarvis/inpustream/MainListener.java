package jarvis.inpustream;

import Conversation.Regular;
import Conversation.SessionManager;
import EmotionDisplayAndFaceRecognition.FaceDetection;
import ErrorMessages.ErrorMessages;
import InternetSearch.GoogleSearch;
import LittleMeGame.LittleMeGameForGUI;
import Mathematics.Calculator;
import NewsPlugIn.MSNNews;
import PowerSupply.BatteryStatusUpdate;
import PowerSupply.PowerStatus;
import ReminderObjects.RemindMe;
import TextCommunication.TextCommunication;
import datastructures.MoodState;
import datastructures.Question;
import googlespeechapi.GoogleSpeechApi;
import jarvis.ProjectJarvis;
import jarvis.VideoCap;
import jarvis.utilities.EnglishNumberToWords;
import jarvis.utilities.ScriptReader;
import jarvis.utilities.Utilities;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import outputstream.BlackBoard;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import outputstream.DisplayTrayIcon;
import outputstream.JarvisMediaPlayer;
import outputstream.MainPanel;
import outputstream.Memes;
import outputstream.SystemSounds;
import splash.ProgressPanelOverlay;

/**
 * text listener for the AI simulator
 */
public class MainListener {

    public static volatile MSNNews msnnews;

    //this is a remind me object collector
    public static List<RemindMe> remindMeObjectsList = new ArrayList<>();

    //SMS response is false
    public static boolean SMSResponse = false;
    public static String SMSResponseText = null;
    public static boolean KeepSMSTextingUser = false; //true;

    //Declaration and initialize a new script reader
    public static PowerStatus.SYSTEM_POWER_STATUS batteryStatus = new PowerStatus.SYSTEM_POWER_STATUS(); //for laptop battery status
    public static ScriptReader scriptReader = new ScriptReader();
    public static boolean ScriptGreeting = false;
    public static String LinkOfCurrentlyReadNews;
    public String[] KnowledgeBaseInScripts = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/knowledgeBaseLocation.txt");
    public String[] inspirationQuotes = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/quotes.txt");
    public String[][] KnowledgeBaseIdentifiers = new String[KnowledgeBaseInScripts.length][2]; //row for each knowlege base, 2 for columns for singular and plural
    public String[] KnowledgeBaseInScriptsLocation = new String[KnowledgeBaseInScripts.length];
    //gregorian calendar for determining the day
    GregorianCalendar gcal = new GregorianCalendar();
    public static LittleMeGameForGUI littleme = null;

    //text message commands
    public static final String QUERY = "SELECT * FROM JarvisCommand WHERE Type = 'USER' ORDER BY id DESC LIMIT 1";
    public static String LastCommandProcessed = ""; //last command jarvis processed
    public static String LastCommandEnteredOnline = ""; //last command entered online.
    public static String LastPhoneNumberReceived = ""; //last phone number received.

    public static final String MYPHONENUMBER = ProjectJarvis.MYPHONENUMBER;

    //ai abuse protection hahaha
    String[] abusePrompt = loadStrings("dialogs/abuse/abusePrompt.txt");
    String[] abuseReplies = loadStrings("dialogs/abuse/abuseReplies.txt");

    //male vanities
    String[] malePrompt = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/doHandsome.txt");
    String[] maleAnswer = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/answerHandsome.txt");

    //female vanities
    String[] femalePrompt = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/doPretty.txt");
    String[] femaleAnswer = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/answerPretty.txt");

    //love
    String[] lovePrompt = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/lovePrompt.txt");
    String[] loveAnswer = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/loveAnswer.txt");

    //marry
    String[] marryPrompt = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/marryPrompt.txt");
    String[] marryAnswer = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/vanities/marryAnswer.txt");

    //calculator
    String[] calculatorLaughs = loadStrings("jokes/calculatorLaughs.txt");
    String[] calPrompt = loadStrings("jokes/calPrompt.txt");

    //netflix and chill mode
    String[] didYouSeeThatAnswer = loadStrings("dialogs/netflix/netflixAnswer.txt");
    String[] didYouSeeThatPrompt = loadStrings("dialogs/netflix/netflixPrompt.txt");

    //load social setting and replies
    String[] backStory = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/backstory.txt");

    //load core functionality prompts and replies
    String[] coreFunctionalities = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/coreFunction.txt");

    //will initialize arrays in constructor.
    String[] socialPrompt = new String[backStory.length];
    String[] socialReply = new String[backStory.length];

    //Core functionality
    String[] coreFunctionalityPrompt = new String[coreFunctionalities.length];
    String[] coreFunctionalityAnswer = new String[coreFunctionalities.length];

    //string of things to check to prevent i dont know
    String[] preventIDontKnow = loadStrings("dialogs/vanities/preventIDontKnow.txt");

    //A string for holding things to say
    String message = "";

    //url variable needed for whether data collection
    public URL url = null;

    //variable to monitor if anthem song is currently playing
    public boolean AnthemIsCurrentlyPlaying = false;

    //name of the artificial intelligence simulator 
    public String AINAME = backStory[0].split("\\.")[1];

    //for google searches
    String searchCriteria = "";

    //detecting chromes location
    final String CHROMELOCATION = "\"Program Files (x86)\"\\Google\\Chrome\\Application\\chrome.exe";
    final String CHROMEREALLOCATION = System.getProperty("user.dir").substring(0, 3) + CHROMELOCATION;

    //to MeetSomeoneNew whether command has been executed
    public boolean CommandExecuted = false;
    public static boolean NameCollectionForRecognition = false;
    private String mathematicalExpression;
    private volatile boolean NetFlixAndWatchMode = false;

    //video player 
    private volatile JarvisMediaPlayer fullScreen = new JarvisMediaPlayer();
    private boolean SpecialTextEntry = false;
    private Memes newMemes = new Memes();
    int numberOfPossibleCT;
    Question[] findCommonTopicArray;

    //SMS TextListener 
    TextCommunication SMSListener = null;

    //default Constructor 
    public MainListener() {

        //initialize with prompt and answer.
        //social commands.
        for (int i = 0; i < backStory.length; i++) {
            String[] tokens = backStory[i].split("\\.");
            socialPrompt[i] = tokens[0];
            if (backStory[i].contains("[LINK]") || backStory[i].contains("[QLINK]")) {
                socialReply[i] = backStory[i].substring(backStory[i].indexOf(".") + 1, backStory[i].length());
            } else {
                socialReply[i] = tokens[1];
            }
        }

        //upload knowledge sources
        for (int j = 0; j < KnowledgeBaseInScripts.length; j++) {
            KnowledgeBaseIdentifiers[j][0] = KnowledgeBaseInScripts[j].split("\\<")[0].split("\\s")[0];
            KnowledgeBaseIdentifiers[j][1] = KnowledgeBaseInScripts[j].split("\\<")[0].split("\\s")[1];
            KnowledgeBaseInScriptsLocation[j] = KnowledgeBaseInScripts[j].split("\\<")[1];
        }

        //upload core functionalities 
        for (int k = 0; k < coreFunctionalities.length; k++) {
            coreFunctionalityPrompt[k] = coreFunctionalities[k].split("\\.")[0];
            coreFunctionalityAnswer[k] = coreFunctionalities[k].split("\\.")[1];
        }

        String findCommonTopicScriptLocation = "assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/mainAILogic/commonDiscussionTopic.txt";

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

        if (!ProjectJarvis.QUERYURLFORNEWMESSAGE.equalsIgnoreCase("nothing")) 
        {
            StartSMSMessageListener();
        } else {
            DisplayTrayIcon.trayIcon.displayMessage("Unable to connect to the service that checks for new SMS messages", "Project Jarvis", TrayIcon.MessageType.INFO);
        }
        BatteryStatusUpdate talkModuleByBatteryPercent = new BatteryStatusUpdate();

    }

    //start a new SMSMessageListener
    private void StartSMSMessageListener() {
        //start text message listener
        SMSListener = new TextCommunication();
    }

    /**
     * process the command entered.
     */
    public void ProcessCommand() {

        CommandExecuted = false;

        String commandOrTextToBeProcessed = ""; //hold the unparsed user input text;
        try {
            url = new URL(ProjectJarvis.BASEWEATHERURLDONOTCHANGE + ProjectJarvis.WEATHERIDENTIFIER);
        } catch (MalformedURLException ex) {
        }

        //MAIN LOGIC CODE BEGINS HERE
        {

            //TEXT ENTRY FROM INPUTBOX IS PROCESSED HERE
            if (BlackBoard.mainPanel.textEntry) {
                commandOrTextToBeProcessed = BlackBoard.mainPanel.StringEntry.toLowerCase();
            } else if (BlackBoard.mainPanel.numberEntry) {
                commandOrTextToBeProcessed = String.valueOf(BlackBoard.mainPanel.NumberEntry);
            } else if (BlackBoard.mainPanel.imageEntry) {
                //will develop image entry in version 10 or so
            }

            if (-1 != commandOrTextToBeProcessed.indexOf("got to go")
                    || -1 != commandOrTextToBeProcessed.indexOf("see you later")
                    || -1 != commandOrTextToBeProcessed.indexOf("talk to you later")) {

                scriptReader.ScriptStillRunning = false;

                if (BlackBoard.mainPanel.sessionManager.regular != null) {

                    BlackBoard.mainPanel.sessionManager.regular = null;
                }

                BlackBoard.mainPanel.mouth.sayThisOnce("See you later. Cheerio!");
                resetTextEntryVariableTypesScreen();
                return;
            }

            random = new Random();

            //Core functionalities
            for (int k = 0; k < coreFunctionalityPrompt.length; k++) {

                if (commandOrTextToBeProcessed.toLowerCase().contains(coreFunctionalityPrompt[k].toLowerCase())) {
                    switch (coreFunctionalityAnswer[k]) {
                        case "[LITTLEME]":
                            AttemptToRunTheLittleMeGame();
                            return;
                        case "[GAMEOFTHRONES]":
                            if (fullScreen.isRunning == true) {
                                return;
                            }
                            PlayGameOfThroneVideo();
                            return;
                        case "[DISABLEFACIALRECOGNITION]":
                            DisableFacialRecognition();
                            return;
                        case "[RESIZE]":
                            ResizeAllWindows();
                            return;
                        case "[LISTEN]":
                            EngageTheGoogleSpeechAPI();
                            return;
                        case "[POWERLEVEL]": //returns the current power level
                            getCurrentPowerLevel();
                            return;
                        case "[ENABLEFACIALRECOGNITION]":
                            EnableFacialRecognition();
                            return;
                        case "[STARTSMSSERVICE]":
                            StartSMSMessagingService();
                            return;
                        case "[STOPSMSLISTENINGSERVICE]":
                            StopSMSMessagingService();
                            return;
                        case "[RELOADBACKSTORY]": //this function reloads the backstory
                            ReloadBackStory();
                            return;
                        case "[MORNINGSTATS]":
                            GreetUserJokesNewsWeatherAdvice();
                            return;
                        case "[READNEWS]":
                            ReadNews();
                            return;
                        case "[SHOWTHEACTUALARTICLE]":
                            ShowActualArticle();
                            return;
                        case "[READNEWSANDDESCRIPTION]":
                            ReadNewArticlesAndDescription();
                            return;
                        case "[WHATCANISAY]":
                            PrintHelpOnScreen();
                            return;
                        case "[STOPREADINGNEWS]":
                            StopReadingTheNews();
                            return;
                        case "[WESTWORLD]":
                            if (fullScreen.isRunning == true) {
                                return;
                            }
                            StartPlayingWestWorldVideo();
                            return;
                        case "[TIME]":
                            SayCurrentTime();
                            return;
                        case "[DRWHO]":
                            if (fullScreen.isRunning == true) {
                                return;
                            }
                            PlayDrWhoVideo();
                            return;
                        case "[STOPVIDEO]":
                            if (StopCurrentSpecialVideo()) {
                                return;
                            }
                            return;
                        case "[CLOSEPROGRAM]":
                            CloseTheProgram();
                            return;
                        case "[DAYOFWEEK]":
                            SayTheDayOfTheWeek();
                            return;
                        case "[WEATHER]":
                            SayTheWeather();
                            return;
                        case "[HELLO]":
                            SayHelloToUser();
                            return;
                        case "[CHANGEBUILD]":
                            ChangeCurrentBuild();

                            return;
                        case "[SETNUMBEROFMEMES]":
                            SetNumberOfMemesInFolder();
                            return;
                        case "[SETWEATHERIDENFIFIERCODE]":
                            ChangeWeatherIdenficationCode();
                            return;
                        case "[SETSALUTATION]":
                            SetSalutationSirOrMa();
                            return;
                        case "[SETWEATHERCODELOCATION]":
                            SetWeatherStateAndCity();
                            return;
                        case "[SETMEMESFREQUENCY]":
                            SetFrequencyOfMemeDisplay();
                            return;
                        case "[DISABLESOUND]":
                            DisableSound();
                            return;
                        case "[SAYTHIS]":
                            SayThisWords(commandOrTextToBeProcessed, k);
                            return;
                        case "[ENABLESOUND]":
                            EnableSounds();
                            return;
                        case "[DISABLESPEECHSOUND]":
                            DisableSpeechSounds();
                            return;
                        case "[MUTESOUNDS]":
                            MuteSounds();
                            return;
                        case "[LOCKSCREEN]":
                            LockScreen();
                            return;
                        case "[UNLOCKSCREEN]":
                            UnlockScreen();
                            return;
                        case "[ENABLESPEECHSOUND]":
                            EnableSpeechSounds();
                            return;
                        case "[CHANGEWALLPAPER]":

                            ChangeWallpaper();
                            return;
                        case "[HIDESMALLPANEL]":
                            HideSmallPanel();
                            return;

                        case "[SHOWSMALLPANEL]":
                            HideSmallScreen();
                            return;

                        case "[HIDEBIGPANEL]":
                            HideMainScreen();
                            return;

                        case "[SHOWBIGPANEL]":
                            ShowMainDisplayScreen();
                            return;

                        case "[HIDECOMPLETELY]":
                            HideAllScreens();
                            return;

                        case "[SHOWCOMPLETELY]":
                            ShowAllScreens();
                            return;

                        case "[ACTIVATENETFLIX]":
                            ActivateNetflixMode();
                            return;
                        case "[DEACTIVATENETFLIX]":
                            if (DeactivateNetflixMode()) {
                                return;
                            }
                            return;
                        case "[REMINDME]":
                            RemindMeToDoStuff(commandOrTextToBeProcessed);

                            return;
                        case "[SHOWMEREMINDERLIST]":
                            ShowMeCurrentReminderList();
                            return;
                        case "[SENDMETHIS]":
                            SendMeThisTextMessage(commandOrTextToBeProcessed, k);

                            return;
                        case "[SEARCHCUE]":
                            LookForStuffOnline(commandOrTextToBeProcessed, k);
                            return;

                        case "[MATHCUE]":
                            SolveMathProblem(commandOrTextToBeProcessed, k);

                            return;
                        case "[ONLINECUE]":
                            CheckForInternConnection();

                            return;
                        case "[CLEARSCREEN]":
                            ClearScreen();
                            return;
                        case "[TAKESNAPSHOT]":
                            TakeASnapShot();

                            return;
                        case "[STOPCONVERSATION]":
                            TerminateCurrentConversation();

                            return;
                        case "[ADJUSTHUMORSETTINGS]":
                            AdjustHumorSettings();
                            return;
                        case "[KNOCKKNOCKJOKES]":
                            TellKnockKnockJokes();
                            return;
                        case "[ACTIVATECAMERA]":
                            ActivateCamera();
                            return;
                        case "[ACTIVATEMEMES]":
                            StartShowingMemesIfPossible();
                            return;
                        case "[DEACTIVATEMEMES]":
                            DeactivateMemes();
                            return;
                        case "[DEACTIVATECAMERA]":
                            DeactivateCamera();
                            return;
                        case "[INCREASEVOLUME]":
                            IncreaseVolume();
                            return;
                        case "[DECREASEVOLUME]":
                            DecreaseVolume();
                            return;
                        case "[STOPLINKMUSIC]":
                            StopMusicGeneratedByBrowser();
                            return;
                        case "[MYNAME]": //say users name
                            BlackBoard.mainPanel.mouth.sayThisOnce("Your name is " + MainPanel.LastFacialRecognitionUsername);
                            resetTextEntryVariableTypesScreen();
                            return;
                        case "[TERMINATE]": //instantly terminate the application
                            BlackBoard.exitRoutine();
                            System.exit(0);
                        case "[AILOCATION]": //present location of the ai
                            String[] response = loadStrings("dialogs/location/locationReplies.txt");
                            BlackBoard.mainPanel.mouth.sayThisOnce(response[new Random().nextInt(4)]);
                            resetTextEntryVariableTypesScreen();
                            return;
                        case "[JOKE]":
                            tellJoke(ProjectJarvis.HumourLevel, new Random().nextBoolean(), false);
                            resetTextEntryVariableTypesScreen();
                            return;
                        case "[INSPIRATION]": //tell inspiration quote
                            tellInspirationQuote();
                            return;
                        case "[QUESTION]": //ask about what and throw an input screen
                            BlackBoard.mainPanel.mouth.askQuestion("Please be more specific");
                            SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                            BlackBoard.mainPanel.textEntry = true;
                            SessionManager.WaitForInputAndProcessIt();

                            try {
                                SessionManager.input.join();
                            } catch (InterruptedException ex) {
                            }

                            //don't use the input. Just let roam free. 
                            return;
                        case "[PLAYNATIONALANTHEM]":
                            if (PlayNationalAnthem()) {
                                return;
                            }
                            return;

                        case "[STOPNATIONALANTHEM]":
                            StopPlayingAnthem();
                            return;
                        default: //no match
                        //return;
                    }
                }

            }

            //Social prompts and replies
            for (int i = 0; i < socialPrompt.length; i++) {
                if (ExecuteSocialCommands(commandOrTextToBeProcessed, i)) {
                    return;
                }
            }

            if (ListenToCheckIfNetflixModeActivate(commandOrTextToBeProcessed)) {
                return;
            }

            //check for abuse
            for (String abusePrompt1 : abusePrompt) {
                if (CheckForAbuseStatements(commandOrTextToBeProcessed, abusePrompt1)) {
                    return;
                }
            }

            //vanities
            for (String malePrompt1 : malePrompt) {
                if (ListenForVanityStatementsMale(commandOrTextToBeProcessed, malePrompt1)) {
                    return;
                }
            }
            //check for vanities female
            for (String femalePrompt1 : femalePrompt) {
                if (ListenForVanitiesFemale(commandOrTextToBeProcessed, femalePrompt1)) {
                    return;
                }
            }

            //check for vanities love
            for (String lovePrompt1 : lovePrompt) {
                if (CheckForLoveStatements(commandOrTextToBeProcessed, lovePrompt1)) {
                    return;
                }
            }

            //check for vanities marry
            for (String marryPrompt1 : marryPrompt) {
                if (CheckForMarryVanityStatements(commandOrTextToBeProcessed, marryPrompt1)) {
                    return;
                }
            }

            if (CheckForGeneralVanityStatements(commandOrTextToBeProcessed)) {
                return;
            }

            if (FacialRecognitionTrainingCode(commandOrTextToBeProcessed)) {
                return;
            }

            if (GreetingMorningAfternoonAndEveningWithCorrections(commandOrTextToBeProcessed)) {
                return;
            }

            if (CheckKnowledgeBasesForPrevisoulyKnownInformation(commandOrTextToBeProcessed)) {
                return;
            }

            PreventItFromSayingItDoesNotKnow(commandOrTextToBeProcessed) ;

        } //end of fake bracket

    }

    private boolean HideSmallScreen() {
        //show camera panel
        if (!BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.fd.setVisible(true);
            MainPanel.jEditorPaneDisplayInfo.removeMouseListener(new ClickEvent());
            BlackBoard.mainPanel.mouth.sayThisOnce("Little display now visible!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Little display was not hidden in the first place");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean HideMainScreen() {
        //hide big panel
        if (ProgressPanelOverlay.blackBoard.isVisible()) {
            ProgressPanelOverlay.blackBoard.setState(Frame.ICONIFIED);
            BlackBoard.mainPanel.mouth.sayThisOnce("Got it!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (!ProgressPanelOverlay.blackBoard.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Big display already minimized!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean ShowAllScreens() {
        //show small and big panel
        if (!ProgressPanelOverlay.blackBoard.isVisible() || !BlackBoard.mainPanel.fd.isVisible()) {
            ProgressPanelOverlay.blackBoard.setVisible(true);
            BlackBoard.mainPanel.fd.setVisible(true);
            BlackBoard.mainPanel.mouth.sayThisOnce("Alright!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (ProgressPanelOverlay.blackBoard.isVisible() && BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I am already maximized!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean HideAllScreens() {
        //hide small and big apnel
        if (ProgressPanelOverlay.blackBoard.isVisible() || BlackBoard.mainPanel.fd.isVisible()) {
            ProgressPanelOverlay.blackBoard.setVisible(false);
            BlackBoard.mainPanel.fd.setVisible(false);
            BlackBoard.mainPanel.mouth.sayThisOnce("Got it!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (!ProgressPanelOverlay.blackBoard.isVisible() && !BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I am already scarce!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean ShowMainDisplayScreen() {
        //show big panel
        if (!ProgressPanelOverlay.blackBoard.isVisible()) {
            ProgressPanelOverlay.blackBoard.setState(Frame.NORMAL);
            BlackBoard.mainPanel.mouth.sayThisOnce("Alright!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (ProgressPanelOverlay.blackBoard.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I am already maximized!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private void RemindMeToDoStuff(String commandOrTextToBeProcessed) {
        //remind me about a certain event

        RemindMe temp = new RemindMe(commandOrTextToBeProcessed);

        if (temp.getRemindObjectStatus() == 2) {
            //mean object creaction was successful
            remindMeObjectsList.add(temp); //REMINDME
            BlackBoard.mainPanel.mouth.sayThisOnce(temp.getImmediateReplyAfterTimerSetup());
            temp.startReminderImmediately(); //start the timer on the object immediately
        }
    }

    private void StartShowingMemesIfPossible() {
        if (ActivateMemes()) {
            return;
        }
    }

    private boolean DeactivateNetflixMode() {
        //deactivate netflix mode
        if (NetFlixAndWatchMode == true) {
            NetFlixAndWatchMode = false; //deactivated.
            BlackBoard.mainPanel.mouth.sayThisOnce("Netflix mode deactivated!" + ", " + MainPanel.LastFacialRecognitionUsername);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private void ActivateNetflixMode() {
        //activate netflix mode
        if (NetFlixAndWatchMode == true) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Netflix mode is already activated!");
            return;
        }
        try {
            //push website data to front end.
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + "www.netflix.com"});
            BlackBoard.mainPanel.mouth.sayThisOnce(random.nextBoolean() ? "I am glad that I could be of assistance!" : "Netflix and Chill! Cool!");
            NetFlixAndWatchMode = true;
        } catch (Exception ex) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry that I could not be of assistance!");
        }
    }

    private void ShowMeCurrentReminderList() {
        //show me the list of reminder object
        //show list of reminder objets to the user.
        if (MainListener.remindMeObjectsList.isEmpty()) {
            BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", you do not have any reminders at the moment!");
        } else {
            BlackBoard.mainPanel.mouth.printToScreen("LISTING OF CURRENT REMINDERS");
            for (int i = 0; i < MainListener.remindMeObjectsList.size(); i++) {
                BlackBoard.mainPanel.mouth.printToScreen(remindMeObjectsList.get(i).getAlarmListing());
            }
        }
    }

    private void SendMeThisTextMessage(String commandOrTextToBeProcessed, int k) {
        //this makes javis send me message that i want

        searchCriteria = commandOrTextToBeProcessed.substring(commandOrTextToBeProcessed.indexOf(coreFunctionalityPrompt[k]) + coreFunctionalityPrompt[k].length() + 1, commandOrTextToBeProcessed.length());
        String textMessageToBeSent = searchCriteria;

        //send the message now
        BlackBoard.mainPanel.mouth.sendMessageToDefaultPhone(textMessageToBeSent);
    }

    private void LookForStuffOnline(String commandOrTextToBeProcessed, int k) {
        //search cue for google search

        searchCriteria = commandOrTextToBeProcessed.substring(commandOrTextToBeProcessed.indexOf(coreFunctionalityPrompt[k]) + coreFunctionalityPrompt[k].length() + 1, commandOrTextToBeProcessed.length()).replace(" ", "+");

        new Thread() {
            @Override
            public void run() {
                BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/smalltalk/searchresponse.txt")[new Random().nextInt(loadStrings("dialogs/smalltalk/searchresponse.txt").length)]);
                GoogleSearch googleSearch = new GoogleSearch(searchCriteria);
                try {
                    googleSearch.waitingForReply.join();
                } catch (InterruptedException ex) {
                }
                BlackBoard.mainPanel.displayInfo.displayImage(googleSearch.imageFromWeb.get(0));

                if (!googleSearch.snippet.isEmpty()) {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException ex) {
                    }
                }
                for (int j = 1; j < googleSearch.imageFromWeb.size(); j++) {

                    BlackBoard.mainPanel.mouth.askQuestion("Does this look like what you were looking for?");
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true; //enter texts.
                    SessionManager.WaitForInputAndProcessIt();
                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }
                    resetTextEntryVariableTypesScreen();
                    if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("Yes")
                            || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("YA")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yeah")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {
                        BlackBoard.mainPanel.mouth.askQuestion("Do you want to find out more information about it?");
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //enter texts.
                        SessionManager.WaitForInputAndProcessIt();
                        try {
                            SessionManager.input.join();
                        } catch (InterruptedException ex) {
                        }
                        //SAYING TO FIND OUT MORE INFORMATION ABOUT SEARCH QUERY
                        if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("Yes")
                                || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("Y")
                                || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("Yeah")
                                || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                                || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {
                            try {
                                //push website data to front end.
                                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + googleSearch.urlForWebPages.get(j)});
                                //BlackBoard.mainPanel.displayInfo.displayRegularHTML(googleSearch.fullHtmlOutput.toString());
                                //try { SessionManager.input.join();} catch (InterruptedException ex) {}
                            } catch (Exception ex) {
                            }
                            BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                        } else {
                            BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                        }
                        break;
                    } else { //no to question
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        }
                        BlackBoard.mainPanel.mouth.sayThisOnce("How about this?");
                        BlackBoard.mainPanel.displayInfo.displayImage(googleSearch.imageFromWeb.get(j));
                    }
                } //end of for loop

            }
        }.start();

        resetTextEntryVariableTypesScreen();
    }

    private boolean PreventItFromSayingItDoesNotKnow(String commandOrTextToBeProcessed) {
        // check to see if the commnand needs to be executed here in the MainListener.
        if (CommandExecuted || SpecialTextEntry) {
            SessionManager.changeCommandExecutionStatus();
            return true;
        }
        //check to make sure it is not suppose to be executed.
        if (scriptReader.ScriptStillRunning == true) {
            resetTextEntryVariableTypesScreen();
            return true;
        }

        for (String items : preventIDontKnow) {
            if (CheckForClearStatements(commandOrTextToBeProcessed, items)) {
                return true;
            }

        }

        if (SpecialTextEntry || BlackBoard.mainPanel.numberEntry || BlackBoard.mainPanel.imageEntry) {
            return true;
        }
        //----------------------------------------------------------------
        BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry, I did not understand the meaning of " + commandOrTextToBeProcessed.toLowerCase() + " !");
        return false;
    }

    private boolean CheckKnowledgeBasesForPrevisoulyKnownInformation(String commandOrTextToBeProcessed) {
        if (BlackBoard.mainPanel.sessionManager != null) {
            if (scriptReader.ScriptStillRunning == false) {
                for (int location = 0; location < KnowledgeBaseIdentifiers.length; location++) {
                    if ((-1 != commandOrTextToBeProcessed.indexOf(KnowledgeBaseIdentifiers[location][0])
                            || -1 != commandOrTextToBeProcessed.indexOf(KnowledgeBaseIdentifiers[location][1])) && scriptReader.ScriptStillRunning == false) {
                        BlackBoard.emotions.setMood(MoodState.HAPPY);
                        SessionManager.session.getHumanJarvis().setHumanmood(MoodState.HOPEFUL);
                        if (scriptReader != null) {
                            scriptReader.ScriptStillRunning = false;
                        }
                        scriptReader = new ScriptReader(KnowledgeBaseInScriptsLocation[location]);
                        scriptReader.start();
                        resetTextEntryVariableTypesScreen();
                        return true;
                    }
                } //end of for loop
                //end of for loop
            } //end of check for script running statement
            //end of check for script running statement
            if ((-1 != commandOrTextToBeProcessed.indexOf("regular")
                    || -1 != commandOrTextToBeProcessed.indexOf("generation conversation")
                    || -1 != commandOrTextToBeProcessed.indexOf("just conversation")
                    || -1 != commandOrTextToBeProcessed.indexOf("just talk"))
                    && BlackBoard.mainPanel.sessionManager.regular == null) {
                //make sure none is running
                if (scriptReader.ScriptStillRunning == true) {
                    resetTextEntryVariableTypesScreen();
                    return true;
                }
                BlackBoard.mainPanel.mouth.sayThisOnce("Alright");
                BlackBoard.emotions.setMood(MoodState.HAPPY);
                BlackBoard.mainPanel.sessionManager.regular = new Regular();
                resetTextEntryVariableTypesScreen();
                return true;
            }
        }
        return false;
    }

    private boolean GreetingMorningAfternoonAndEveningWithCorrections(String commandOrTextToBeProcessed) {
        //greeting section.
        if (-1 != commandOrTextToBeProcessed.indexOf("morning")) {
            if (Utilities.IsMorning()) {
                CheckAndGreatAppropriate();

            } else {
                BlackBoard.mainPanel.mouth.sayThisOnce("It is not morning, " + MainPanel.LastFacialRecognitionUsername);
                CheckAndGreatAppropriate();

            }
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("afternoon")) {
            if (Utilities.IsAfterNoon()) {
                CheckAndGreatAppropriate();

            } else {
                BlackBoard.mainPanel.mouth.sayThisOnce("It is not afternoon, " + MainPanel.LastFacialRecognitionUsername);
                CheckAndGreatAppropriate();

            }
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("evening")) {
            if (Utilities.IsEvening()) {
                CheckAndGreatAppropriate();

            } else {
                BlackBoard.mainPanel.mouth.sayThisOnce("It is not evening, " + MainPanel.LastFacialRecognitionUsername);
                CheckAndGreatAppropriate();

            }
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("night")) {
            if (Utilities.IsNight()) {
                CheckAndGreatAppropriate();

            } else {
                BlackBoard.mainPanel.mouth.sayThisOnce("It is not night time, " + MainPanel.LastFacialRecognitionUsername);
                CheckAndGreatAppropriate();

            }
            resetTextEntryVariableTypesScreen();
            return true;
        }
        //cars, arts, music, even video games
        return false;
    }

    private boolean FacialRecognitionTrainingCode(String commandOrTextToBeProcessed) {
        //FACE recognition - training data accumulation section
        if ((-1 != commandOrTextToBeProcessed.indexOf("i am not " + MainPanel.LastFacialRecognitionUsername)
                || -1 != commandOrTextToBeProcessed.indexOf("this is not " + MainPanel.LastFacialRecognitionUsername)
                || -1 != commandOrTextToBeProcessed.indexOf("my name is not " + MainPanel.LastFacialRecognitionUsername)
                || -1 != commandOrTextToBeProcessed.indexOf("that is not my name"))
                && BlackBoard.mainPanel.sessionManager.regular == null) {
            //open the camera
            BlackBoard.emotions.setMood(null);
            BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry for the confusion.");
            resetTextEntryVariableTypesScreen();
            BlackBoard.mainPanel.nameEntry = true;
            BlackBoard.mainPanel.mouth.askQuestion("what is your name?");
            //NameCollectionForRecognition = true;
            BlackBoard.mainPanel.sessionManager.collectInputValues();
            //SessionManager.WaitForInputAndProcessIt();
            try {
                SessionManager.input.join();
            } catch (InterruptedException ex) {
            }
            NameCollectionForRecognition = true;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
            SessionManager.ShowCameraInTime(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean CheckForGeneralVanityStatements(String commandOrTextToBeProcessed) {
        if (-1 != commandOrTextToBeProcessed.indexOf("you like me")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/likeme/likeme.txt")[new Random().nextInt(loadStrings("dialogs/likeme/likeme.txt").length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("are you conscious") || -1 != commandOrTextToBeProcessed.indexOf("are you alive")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/alive/alive.txt")[new Random().nextInt(loadStrings("dialogs/alive/alive.txt").length)]);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("see me") || -1 != commandOrTextToBeProcessed.indexOf("have eyes")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/smalltalk/seeme.txt")[new Random().nextInt(loadStrings("dialogs/smalltalk/seeme.txt").length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("how is your day going") || -1 != commandOrTextToBeProcessed.indexOf("how is it going") || -1 != commandOrTextToBeProcessed.indexOf("what are you up to")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/taskdonetoday.txt")[new Random().nextInt(loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/taskdonetoday.txt").length)]);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("have a boyfriend") || -1 != commandOrTextToBeProcessed.indexOf("have a girlfriend") || -1 != commandOrTextToBeProcessed.indexOf("seeing someone")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/smalltalk/relationship.txt")[new Random().nextInt(loadStrings("dialogs/smalltalk/relationship.txt").length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("your creator") || -1 != commandOrTextToBeProcessed.indexOf("your maker") || -1 != commandOrTextToBeProcessed.indexOf("designed you") || -1 != commandOrTextToBeProcessed.indexOf("made you")) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loadStrings("dialogs/maker/maker.txt")[new Random().nextInt(loadStrings("dialogs/maker/maker.txt").length)]);
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("i like you")) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I like you too, " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.LAUGHING);
            SessionManager.ShowCameraInTime();
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (-1 != commandOrTextToBeProcessed.indexOf("i love you")) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I love you too, " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.LAUGHING);
            SessionManager.ShowCameraInTime();
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean CheckForMarryVanityStatements(String commandOrTextToBeProcessed, String marryPrompt1) {
        if (commandOrTextToBeProcessed.contains(marryPrompt1)) {
            BlackBoard.mainPanel.mouth.sayThisOnce(marryAnswer[new Random().nextInt(marryAnswer.length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
            SessionManager.ChangeMoodBackToHappy(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean CheckForLoveStatements(String commandOrTextToBeProcessed, String lovePrompt1) {
        if (commandOrTextToBeProcessed.contains(lovePrompt1)) {
            BlackBoard.mainPanel.mouth.sayThisOnce(loveAnswer[new Random().nextInt(loveAnswer.length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            ImageSlider go = new ImageSlider("", "", 5); //love you images\\bird.jpg
            BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
            SessionManager.ChangeMoodBackToHappy(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean ListenForVanitiesFemale(String commandOrTextToBeProcessed, String femalePrompt1) {
        if (commandOrTextToBeProcessed.contains(femalePrompt1)) {
            BlackBoard.mainPanel.mouth.sayThisOnce(femaleAnswer[new Random().nextInt(femaleAnswer.length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
            SessionManager.ChangeMoodBackToHappy(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean ListenForVanityStatementsMale(String commandOrTextToBeProcessed, String malePrompt1) {
        if (commandOrTextToBeProcessed.contains(malePrompt1)) {
            BlackBoard.mainPanel.mouth.sayThisOnce(maleAnswer[new Random().nextInt(maleAnswer.length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
            SessionManager.ChangeMoodBackToHappy(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean CheckForAbuseStatements(String commandOrTextToBeProcessed, String abusePrompt1) {
        if (commandOrTextToBeProcessed.contains(abusePrompt1)) {
            BlackBoard.mainPanel.mouth.sayThisOnce(abuseReplies[new Random().nextInt(abuseReplies.length)] + ", " + MainPanel.LastFacialRecognitionUsername);
            BlackBoard.emotions.setMood(MoodState.ANGRY); //change mood to waving
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.ANGRY);
            ImageSlider go = new ImageSlider("", "", 4); //fuck you
            SessionManager.ChangeMoodBackToHappy(); //show camera in certain time
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private boolean ListenToCheckIfNetflixModeActivate(String commandOrTextToBeProcessed) {
        //if netflix and watch mode is activated, answer movie questiosn
        if (NetFlixAndWatchMode == true) {
            for (String didYouSeeThatPrompt1 : didYouSeeThatPrompt) {
                if (commandOrTextToBeProcessed.contains(didYouSeeThatPrompt1)) {
                    BlackBoard.mainPanel.mouth.sayThisOnce(didYouSeeThatAnswer[new Random().nextInt(didYouSeeThatAnswer.length)]);
                    SessionManager.session.getHumanJarvis().setHumanmood(MoodState.EXCITED);
                    resetTextEntryVariableTypesScreen();
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    private boolean ExecuteSocialCommands(String commandOrTextToBeProcessed, int i) throws NumberFormatException {
        if (commandOrTextToBeProcessed.contains(socialPrompt[i])) {
            if (socialReply[i].contains("[LAUGH]")) {
                //PLAY LAUGH FILE
                playLaughSound();
            }
            if (socialReply[i].contains("[FEELING]")) {
                //determine feeling and tell user how you are feeling

                String[] firstLineOptions;

                PowerStatus.INSTANCE.GetSystemPowerStatus(batteryStatus);

                int batteryPercentLeft = (Integer.valueOf(batteryStatus.getBatteryLifePercent().substring(0, batteryStatus.getBatteryLifePercent().length() - 1)));

                switch (batteryPercentLeft / 10 * 10) {
                    case 0:
                    case 10:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/10.txt");
                        break;
                    case 20:
                    case 30:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/20.txt");
                        break;
                    case 40:
                    case 50:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/30.txt");
                        break;
                    case 60:
                    case 70:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/40.txt");
                        break;
                    case 80:
                    case 90:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/50.txt");
                        break;
                    case 100:
                    default:
                        firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/60.txt");
                        break;
                }

                //check for emotions here.
                //emotions will be changed here.
                sayFeelingAndSetEmotionsVariable(firstLineOptions, random);
                // if (random.nextBoolean()) sayFeelingAndSetEmotionsVariable(firstLineOptions, random); //say feelings and change emotions.
            }
            if (socialReply[i].contains("[FOODQUESTION]")) {

                BlackBoard.mainPanel.mouth.askQuestion("Oh dear! Will you like to order some food?");
                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                BlackBoard.mainPanel.textEntry = true; //enter texts.
                SessionManager.WaitForInputAndProcessIt();
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }

                if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("yes")
                        || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("ya")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("food please")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yes")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yeah")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {
                    BlackBoard.mainPanel.mouth.askQuestion("Which of this places will you like to order from? Dominos, Hongs, or Subway");
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true; //enter texts.
                    SpecialTextEntry = true;
                    SessionManager.WaitForInputAndProcessIt();
                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }
                    //SAYING TO FIND OUT MORE INFORMATION ABOUT SEARCH QUERY

                    if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("dominos")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("domino")) {
                        try {
                            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + "www.dominos.com"});
                        } catch (Exception ex) {
                        }
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                    } else if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("hong")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("hongs")) {
                        try {
                            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + "https://www.yelp.com/biz/hongs-chinese-restaurant-crookston"});
                        } catch (Exception ex) {
                        }
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                    } else if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("subway")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("subways")) {
                        try {
                            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + "https://order.subway.com/"});
                        } catch (Exception ex) {
                        }
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                    } else if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("something else")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("none")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("any of these")) { //user selected something else
                        try {
                            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + "https://www.google.com/#q=order+online+delivery+near+me"});
                        } catch (Exception ex) {
                        }
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am glad that I could be of assistance!");
                    } else { //some else
                        BlackBoard.mainPanel.mouth.sayThisOnce("Maybe another time then!");
                    }

                    SpecialTextEntry = false;

                } else { //not hungry

                    BlackBoard.mainPanel.mouth.sayThisOnce("Alright!");

                }

            } //END OF FOOD QUESTION.
            String removeLink = "";
            //it contains a link, open the link.
            if (socialReply[i].contains("[LINK]")) {

                String link = socialReply[i].substring(socialReply[i].indexOf("[LINK]") + "[LINK]".length() + 1, socialReply[i].length());

                removeLink = socialReply[i].substring(socialReply[i].indexOf("[LINK]"));

                try {
                    //push website data to front end.
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + link});
                } catch (Exception ex) {
                    BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry, I am unable to open the link attached!");
                }
            }
            //Confirmation question before a link.
            //it contains a link, open the link.
            if (socialReply[i].contains("[QLINK]")) {

                String link = socialReply[i].substring(socialReply[i].indexOf("Link=") + "Link=".length(), socialReply[i].length());

                String question = socialReply[i].substring(socialReply[i].indexOf("Question=") + "Question=".length(), socialReply[i].indexOf("Link=") - 1);

                removeLink = socialReply[i].substring(socialReply[i].indexOf("[QLINK]"));

                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                BlackBoard.mainPanel.textEntry = true; //enter texts.

                BlackBoard.mainPanel.mouth.askQuestion(question);

                SessionManager.WaitForInputAndProcessIt();
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }

                if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("yes")
                        || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("ya")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("food please")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yes")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yeah")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {

                    try {
                        //push website data to front end.
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + link});
                    } catch (Exception ex) {
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry, I am unable to open the link attached!");
                    }

                    BlackBoard.mainPanel.mouth.sayThisOnce("Got it!");

                } else {
                    BlackBoard.mainPanel.mouth.sayThisOnce("Alright then!");
                }
            }
            //check for question and if no question. Just say it regularly
            String actionStatement = socialReply[i];
            //CHECK IF IT A QUESTION, IF IT IS A QUESTION, THEN ASK A QUESTION
            //AND EXPECT ANSWER
            if (actionStatement.contains("?") && -1 == actionStatement.indexOf("[QLINK]") && -1 == actionStatement.indexOf("[LINK]")) {
                //QUESTION
                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                BlackBoard.mainPanel.textEntry = true;
                String actionStatementToSay = actionStatement.replace("[USER]", MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername)
                        .replace(removeLink, "")
                        .replace("[LAUGH]", "")
                        .replace("[FOODQUESTION]", "")
                        .replace("[FEELING]", "")
                        .replace("[HAVEFEELING]", "")
                        .replace("[AIFIRSTNAME]", SessionManager.session.getHumanJarvis().getObjectName() == null ? "Jarvis" : SessionManager.session.getHumanJarvis().getObjectName());
                SpecialTextEntry = true;
                BlackBoard.mainPanel.mouth.askQuestion(actionStatementToSay);
                //ANSWER
                SessionManager.WaitForInputAndProcessIt();
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }
                SpecialTextEntry = false;
                BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);
                //computer response begins here
                Boolean foundKnowledgeInMemory = false;
                //if the script reader is running stop it.
                //loop through all the scripts in memory and
                for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {
                    //if current topic is different from new topic, switch to new topic
                    if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                            || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptReader.scriptLocation)) {
                        foundKnowledgeInMemory = true;
                        if (scriptReader.ScriptStillRunning == true) {
                            scriptReader.ScriptStillRunning = false; //terminate current sub script
                        }
                        scriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                        scriptReader.start();
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        //while (scriptReader.isAlive()); //busy wait.
                        break;

                    } //end of if statement for checking topics

                } //end of for loop
                //if foundKnowledge of new topic in script, return, if not return
                resetTextEntryVariableTypesScreen();
                return true;
                //if knowlege script cannot be found in memory
                //do nothing
                // break; //break switch
            }
            String sayThisWords = socialReply[i]
                    .replace("[USER]", MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername)
                    .replace("[LAUGH]", "")
                    .replace("[FOODQUESTION]", "")
                    .replace("[FEELING]", "")
                    .replace("[HAVEFEELING]", "")
                    .replace(removeLink, "")
                    .replace("[AIFIRSTNAME]", SessionManager.session.getHumanJarvis().getObjectName() == null ? "Jarvis" : SessionManager.session.getHumanJarvis().getObjectName());
            if (!sayThisWords.isEmpty()) {
                BlackBoard.mainPanel.mouth.sayThisOnce(sayThisWords);
            }
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private void StopPlayingAnthem() {
        //if anthem sound is playing, stop it.
        if (AnthemIsCurrentlyPlaying == false) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I wasn't singing the national anthem!");
            return;
        } //no anthem track is currently playing.
        //no anthem track is currently playing.
        stopNationalAnthem();
    }

    private boolean PlayNationalAnthem() {
        //play the national anthem
        if (AnthemIsCurrentlyPlaying == true) {
            return true; //one thread of national anthem is still running.
        }
        String responseString;
        if (MainPanel.LastFacialRecognitionUsername != null) {
            responseString = "Okay, " + MainPanel.LastFacialRecognitionUsername;
        } else {
            responseString = "Alright!";
        }
        BlackBoard.mainPanel.mouth.sayThisOnce(responseString);
        MainPanel.displayInfo.displayInformation("Here we go.");
        playNationalAnthem();
        resetTextEntryVariableTypesScreen();
        return false;
    }

    private void StopMusicGeneratedByBrowser() {
        try {
            //terminate chrome
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "taskkill /im chrome.exe"});
            BlackBoard.mainPanel.mouth.printToScreen("Done!");
        } catch (IOException ex) {
            BlackBoard.mainPanel.mouth.printToScreen("Could not complete task!");
        }
    }

    private void DecreaseVolume() {
        try {
            Runtime.getRuntime().exec(Utilities.getJarParentDir() + "nircmd\\nircmd.exe  changesysvolume -1310");
            BlackBoard.mainPanel.mouth.printToScreen("Done!");
            Toolkit.getDefaultToolkit().beep();
        } catch (IOException ex) {
            BlackBoard.mainPanel.mouth.printToScreen("Error changing system volume!");
        }
    }

    private void IncreaseVolume() {
        try {
            Runtime.getRuntime().exec(Utilities.getJarParentDir() + "nircmd\\nircmd.exe  changesysvolume 1310");
            BlackBoard.mainPanel.mouth.printToScreen("Done!");
            Toolkit.getDefaultToolkit().beep();
        } catch (IOException ex) {
            BlackBoard.mainPanel.mouth.printToScreen("Error changing system volume!");
        }
    }

    private void DeactivateCamera() {
        //turn off webcam
        if (FaceDetection.stopLookingAtMe == false) {
            BlackBoard.mainPanel.mouth.sayThisOnce(random.nextBoolean() ? "No problemo" : "Okay, I will stop looking at your beautiful self");
            BlackBoard.emotions.setMood(MoodState.HAPPY);
            FaceDetection.stopLookingAtMe = true;
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("I was not looking at you in the first place, " + MainPanel.LastFacialRecognitionUsername);
            //BlackBoard.emotions.setMood(null);
        }
        resetTextEntryVariableTypesScreen();
    }

    private void DeactivateMemes() {
        //deactivate memes
        if (newMemes == null) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Memes module is not currently active");
        } else {
            newMemes.ThreadStillRunning = false;
            BlackBoard.mainPanel.mouth.sayThisOnce("Memes service deactivated!");
        }
    }

    private boolean ActivateMemes() {
        //activate memes
        if (newMemes.ThreadStillRunning == true) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Memes is active. Expect some memes!");
            return true;
        }
        newMemes = new Memes(true);
        newMemes.start();
        BlackBoard.mainPanel.mouth.sayThisOnce("I got the meme service going!");
        return false;
    }

    private void ActivateCamera() {
        //turn on webcam
        if (FaceDetection.stopLookingAtMe == true) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Sure, I am now looking at you!");
            FaceDetection.stopLookingAtMe = false;
            BlackBoard.emotions.setMood(null);
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("I am already looking at you, " + MainPanel.LastFacialRecognitionUsername);
            //BlackBoard.emotions.setMood(null);
        }
        resetTextEntryVariableTypesScreen();
    }

    private void TellKnockKnockJokes() {
        tellJoke(10, false, false); //loaded with knock knock jokes
        resetTextEntryVariableTypesScreen();
    }

    private void AdjustHumorSettings() {
        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();

        BlackBoard.mainPanel.numberEntry = true; //enter texts.

        BlackBoard.mainPanel.mouth.askQuestion("Enter desired humor settings between 1 and 100");

        SessionManager.WaitForInputAndProcessIt();
        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.NumberEntry >= 0 && BlackBoard.mainPanel.NumberEntry <= 100) {

            ProjectJarvis.HumourLevel = BlackBoard.mainPanel.NumberEntry;

            BlackBoard.mainPanel.mouth.sayThisOnce("Humor level has now been adjusted!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("That was an invalid humor settings! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        }
    }

    private void TerminateCurrentConversation() {
        //cancel correct conversation

        if ("".equalsIgnoreCase(MainPanel.LastFacialRecognitionUsername)) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I don't know your name yet!");

            resetTextEntryVariableTypesScreen();
            BlackBoard.mainPanel.nameEntry = true;

            BlackBoard.mainPanel.mouth.askQuestion("what is your name?");

            SessionManager.WaitForInputAndProcessIt();

            try {
                SessionManager.input.join();
            } catch (InterruptedException ex) {
            }
        }
        if (scriptReader.ScriptStillRunning == true) {
            scriptReader.ScriptStillRunning = false;
        }

        BlackBoard.mainPanel.mouth.askQuestion("Sure, would like to talk about or do something else?");
        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.textEntry = true; //enter texts.
        SessionManager.WaitForInputAndProcessIt();
        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }
        resetTextEntryVariableTypesScreen();
        if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("Yes")
                || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("Y")) {
            BlackBoard.mainPanel.mouth.askQuestion("Sure, what would you like to talk about or do?");
            SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
            BlackBoard.mainPanel.textEntry = true; //enter texts.
            SessionManager.WaitForInputAndProcessIt();
            try {
                SessionManager.input.join();
            } catch (InterruptedException ex) {
            }
        }
        resetTextEntryVariableTypesScreen();
    }

    private void TakeASnapShot() {
        //take a picture

        if (FaceDetection.stopLookingAtMe == true) {
            BlackBoard.mainPanel.mouth.sayThisOnce("I cannot see you at the moment, I will attempt to activate the camera");
            //briefly activate camera, if it is off
            FaceDetection.stopLookingAtMe = false;
            BlackBoard.emotions.setMood(null);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
            }

            //deactivate camera after 10 seconds
            Timer deactivateCamera = new Timer(9000, (ActionEvent e) -> {
                BlackBoard.emotions.setMood(MoodState.HAPPY);
                FaceDetection.stopLookingAtMe = true;
                BlackBoard.mainPanel.mouth.sayThisOnce("Camera deactivated!");
            });
            deactivateCamera.setRepeats(false);
            deactivateCamera.start();

//                                resetTextEntryVariableTypesScreen();
//                                return;
        }

        BlackBoard.mainPanel.mouth.sayThisOnce("Here we go! Smile!");
        VideoCap.takeSnapShot();
        try {
            FaceDetection.SnapShotOver = false;
        } catch (Exception e) {
        }
        resetTextEntryVariableTypesScreen();
    }

    private void ClearScreen() {
        //clear screen
        for (int i = 0; i < 40; i++) {
            BlackBoard.mainPanel.mouth.printToScreen("");
        }
        BlackBoard.mainPanel.mouth.sayThisOnce("That action has been carried out!");
    }

    private void CheckForInternConnection() {
        //check online status
        if (checkInternetConnectivity()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("We are online!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("It does not appear that we have internet connection at the moment");
        }
        resetTextEntryVariableTypesScreen();
    }

    private void SolveMathProblem(String commandOrTextToBeProcessed, int k) {
        //cute for math operations //fishy

        mathematicalExpression = commandOrTextToBeProcessed.substring(commandOrTextToBeProcessed.indexOf(coreFunctionalityPrompt[k]) + coreFunctionalityPrompt[k].length() + 1, commandOrTextToBeProcessed.length()).replace("?", "");

        // mathematicalExpression = commandOrTextToBeProcessed.substring(commandOrTextToBeProcessed.indexOf("what is the") + 12, commandOrTextToBeProcessed.length()).replace("?", "");
        new Thread() {
            @Override
            public void run() {

                ErrorMessages err = new ErrorMessages();

                if (MainListener.SMSResponse) {
                    KeepSMSTextingUser = true; //keep texting user.
                }
                BlackBoard.mainPanel.mouth.sayThisOnce(calPrompt[new Random().nextInt(calPrompt.length)]);

                try {
                    Thread.sleep(700);
                } catch (InterruptedException ex) {
                }

                try {
                    Calculator calculator = new Calculator(mathematicalExpression);
                    BlackBoard.mainPanel.mouth.sayThisOnce(mathematicalExpression
                            .replace("-", " minus ")
                            .replace("+", " plus ")
                            .replace("*", " multiplied by ")
                            .replace("/", " divided by ")
                            + " is equal to " + String.valueOf(calculator.eval()));

                    Timer mock = new Timer(700, (ActionEvent e) -> {
                        BlackBoard.mainPanel.mouth.sayThisOnce(calculatorLaughs[new Random().nextInt(calculatorLaughs.length)]);
                        KeepSMSTextingUser = false;
                    });

                    mock.setRepeats(false);
                    mock.start();
                } catch (Exception e) {
                    BlackBoard.mainPanel.mouth.sayThisOnce(err.getRandomErrorMessage() + ", " + (new Random().nextBoolean() ? ProjectJarvis.SALUTATION : MainPanel.LastFacialRecognitionUsername));
                    KeepSMSTextingUser = false;
                }
            }
        }.start();
    }

    private boolean HideSmallPanel() {
        //hide camera panel
        if (!BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Small display is already hidden!");
            resetTextEntryVariableTypesScreen();
            return true;
        }
        if (BlackBoard.mainPanel.fd.isVisible()) {
            BlackBoard.mainPanel.fd.setVisible(false);
            BlackBoard.mainPanel.mouth.sayThisOnce("Alright, small display now hidden");
            MainPanel.jEditorPaneDisplayInfo.addMouseListener(new ClickEvent());
            resetTextEntryVariableTypesScreen();
            return true;
        }
        return false;
    }

    private void ChangeWallpaper() {
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.numberEntry = true;

        BlackBoard.mainPanel.mouth.sayThisOnce("These are the available selections at the moment");

        File selectionBackground = new File(Utilities.getJarParentDir() + "background/wallpaperSelection.PNG");

        try {
            MainPanel.displayInfo.displayImage(selectionBackground.toURI().toURL());
        } catch (MalformedURLException ex) {
        }

        BlackBoard.mainPanel.mouth.askQuestion("Select a number between 1 and 5?");

        SessionManager.WaitForInputAndProcessIt();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.NumberEntry > 0 && BlackBoard.mainPanel.NumberEntry < 6) {

            ProjectJarvis.settings.SetMainWallPaper(BlackBoard.mainPanel.NumberEntry);

            BlackBoard.mainPanel.mouth.sayThisOnce("Main screen wallpaper will be changed to that selection on next restart!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("That was an invalid selection! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        }
    }

    private void EnableSpeechSounds() {
        //sound effects SPEECH
        if (ProjectJarvis.settings.SpeechSoundActive) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Speech sound already enabled " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        } else { //enable sound effect
            ProjectJarvis.settings.SetSpeechSoundActive(true);
            BlackBoard.mainPanel.mouth.sayThisOnce("Speech sound now enabled! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
            TerminateProgramSlowly(); //need to restart the speech engine
        }
    }

    private void UnlockScreen() {
        //unlock the screen

        if (ProjectJarvis.LockedScreen == false) {
            BlackBoard.mainPanel.mouth.sayThisOnce("The screen is already unlocked");
            Toolkit.getDefaultToolkit().beep();
            DisplayTrayIcon.trayIcon.displayMessage("The screen is already unlocked", "Project Jarvis", TrayIcon.MessageType.INFO);
        } else {
            //run unlock robot
            BlackBoard.mainPanel.mouth.sayThisOnce("Attempting to unlock screen!");
            Toolkit.getDefaultToolkit().beep();
            Utilities.TypeThisInRobot(ProjectJarvis.MYACCOUNTPASSWORD);

        }
    }

    private boolean StopCurrentSpecialVideo() {
        //stop the video
        if (fullScreen.isRunning == true) {
            fullScreen.closePlayerAndReleaseResources();
            BlackBoard.mainPanel.mouth.printToScreen("Done!");
            return true;
        } else {
            BlackBoard.mainPanel.mouth.printToScreen("No video was playing!");
        }
        return false;
    }

    private void LockScreen() {
        //lock the screen
        if (ProjectJarvis.LockedScreen == true) {
            BlackBoard.mainPanel.mouth.sayThisOnce("The screen is already locked!");
            return;
        } else {
            try {
                Process proc = Runtime.getRuntime().exec("rundll32.exe user32.dll, LockWorkStation");
            } catch (IOException ex) {
            }
            ProjectJarvis.LockedScreen = true;
            BlackBoard.mainPanel.mouth.sayThisOnce("The screen is now locked!");
        }
    }

    private void MuteSounds() {
        //mute system sounds
        Runtime run = Runtime.getRuntime();
        Process muteSounds;
        try {
            muteSounds = run.exec(Utilities.getJarParentDir() + "nircmd\\nircmd.exe  mutesysvolume 2");
            BlackBoard.mainPanel.mouth.printToScreen("Mute!");
        } catch (IOException ex) {
            muteSounds = null;
            BlackBoard.mainPanel.mouth.printToScreen("Error changing system volume!");
        }
    }

    private void DisableSpeechSounds() {
        //sound effects SPEECH
        if (ProjectJarvis.settings.SpeechSoundActive) {
            ProjectJarvis.settings.SetSpeechSoundActive(false);
            BlackBoard.mainPanel.mouth.sayThisOnce("Speech sound disabled");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Speech sound already disabled");
        }
    }

    private void EnableSounds() {
        //sound effects
        if (ProjectJarvis.settings.RegularSoundActive) {
            BlackBoard.mainPanel.mouth.sayThisOnce("Sound effects already enabled " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));

        } else { //enable sound effect
            ProjectJarvis.settings.SetRegularSoundActive(true);
            BlackBoard.mainPanel.mouth.sayThisOnce("Sound effects now enabled! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
            TerminateProgramSlowly();
        }
    }

    private void SayThisWords(String commandOrTextToBeProcessed, int k) {
        //say this
        String sayThisPhrase = commandOrTextToBeProcessed.substring(commandOrTextToBeProcessed.indexOf(coreFunctionalityPrompt[k]) + coreFunctionalityPrompt[k].length() + 1, commandOrTextToBeProcessed.length());
        BlackBoard.mainPanel.mouth.sayThisOnce(sayThisPhrase);
    }

    private void DisableSound() {
        //sound effects
        if (ProjectJarvis.settings.RegularSoundActive) {
            ProjectJarvis.settings.SetRegularSoundActive(false);
            BlackBoard.mainPanel.mouth.sayThisOnce("Sound effects disabled");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Sound effect already disabled");
        }
    }

    private void SetFrequencyOfMemeDisplay() {
        //how fast memes flip
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.numberEntry = true;

        BlackBoard.mainPanel.mouth.askQuestion("Enter how fast you would like to see a meme in minutes");

        BlackBoard.mainPanel.sessionManager.collectInputValues();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.NumberEntry >= 5 && BlackBoard.mainPanel.NumberEntry <= 30) {
            ProjectJarvis.TIME_SET_FOR_RANDOM_MEMES = BlackBoard.mainPanel.NumberEntry * 60 * 1000;
            BlackBoard.mainPanel.mouth.sayThisOnce("Frequency of meme showing updated! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
            BlackBoard.mainPanel.mouth.printToScreen("This will take effect after you restart the application!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value for dank memes frequency!");
            BlackBoard.mainPanel.mouth.printToScreen("This value should be between 5-30");
        }
    }

    private void SetWeatherStateAndCity() {
        //weather location
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.textEntry = true;
        SpecialTextEntry = true;
        BlackBoard.mainPanel.mouth.askQuestion("Enter the city and state for the new weather identifier code entered");

        BlackBoard.mainPanel.sessionManager.collectInputValues();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.StringEntry.length() > 0) {
            ProjectJarvis.WEATHERLOCATION = BlackBoard.mainPanel.StringEntry;
            BlackBoard.mainPanel.mouth.sayThisOnce("Weather location updated! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value for weather location!");
        }
        SpecialTextEntry = false;
    }

    private void SetSalutationSirOrMa() {
        //set weather identifier code
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.textEntry = true;

        BlackBoard.mainPanel.mouth.askQuestion("How would you like me to formally refer to you");
        SpecialTextEntry = true;
        BlackBoard.mainPanel.sessionManager.collectInputValues();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.StringEntry.length() > 0) {
            ProjectJarvis.SALUTATION = BlackBoard.mainPanel.StringEntry;
            BlackBoard.mainPanel.mouth.sayThisOnce("Value updated!" + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value!");
        }
        SpecialTextEntry = false;
    }

    private void ChangeWeatherIdenficationCode() {
        //set weather identifier code
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.textEntry = true;

        BlackBoard.mainPanel.mouth.askQuestion("Enter the weather location identifier code");
        SpecialTextEntry = true;
        BlackBoard.mainPanel.sessionManager.collectInputValues();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.StringEntry.length() > 0) {
            ProjectJarvis.WEATHERIDENTIFIER = BlackBoard.mainPanel.StringEntry;
            BlackBoard.mainPanel.mouth.sayThisOnce("Weather identifier code updated! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value!");
        }
        SpecialTextEntry = false;
    }

    private void SetNumberOfMemesInFolder() {
        //number of memes in folder
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.numberEntry = true;

        BlackBoard.mainPanel.mouth.askQuestion("Enter the number of dank memes in the memes folder directory");

        BlackBoard.mainPanel.sessionManager.collectInputValues();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.NumberEntry >= 0) {
            ProjectJarvis.NUMBER_OF_MEMES = BlackBoard.mainPanel.NumberEntry;
            BlackBoard.mainPanel.mouth.sayThisOnce("Dank memes count updated! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value for dank memes count!");
        }
    }

    private void ChangeCurrentBuild() {
        //CHANGE BUILD
        resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.numberEntry = true;

        BlackBoard.mainPanel.mouth.askQuestion("Enter build number (for example, 1)");

        SessionManager.WaitForInputAndProcessIt();

        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }

        if (BlackBoard.mainPanel.NumberEntry > 0 && BlackBoard.mainPanel.NumberEntry < 10) {
            ProjectJarvis.BUILDFOLDER = "Build" + String.valueOf(BlackBoard.mainPanel.NumberEntry);
            BlackBoard.mainPanel.mouth.sayThisOnce("Build value updated, a full restart required! " + (MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            BlackBoard.mainPanel.mouth.sayThisOnce("Restart application once fully terminated, " + MainPanel.LastFacialRecognitionUsername + ", Goodbye!");

            BlackBoard.exitRoutine(); //Exit routine here.

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            System.exit(0); //Terminate the JVM here.
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Failed to update value for build version!");
        }
    }

    private void SayHelloToUser() {
        //say hello to the user
        BlackBoard.mainPanel.mouth.sayThisOnce(MainPanel.LastFacialRecognitionUsername != null ? "hello, " + MainPanel.LastFacialRecognitionUsername : "hello");
        resetTextEntryVariableTypesScreen();
    }

    private void SayTheWeather() {
        //tell the weather
        getWeather(ProjectJarvis.WEATHERLOCATION, false);
        resetTextEntryVariableTypesScreen();
    }

    private void SayTheDayOfTheWeek() {
        //tell the day of week
        sayWhatDayOfTheWeekItIs("It is");
        resetTextEntryVariableTypesScreen();
    }

    private void CloseTheProgram() {
        //close the program normally
        BlackBoard.mainPanel.mouth.sayThisOnce("Have a good day, " + MainPanel.LastFacialRecognitionUsername + ", Goodbye!");

        BlackBoard.exitRoutine(); //Exit routine here.

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

        System.exit(0); //Terminate the JVM here.
    }

    private void PlayDrWhoVideo() {
        fullScreen = new JarvisMediaPlayer(Utilities.getJarParentDir() + "videos\\thedoctor.mp4");
        BlackBoard.mainPanel.mouth.sayThisOnce("Yes indeed!");
    }

    private void SayCurrentTime() {
        //show the time
        sayWhatTheTimeIs("It is now");
        resetTextEntryVariableTypesScreen();
    }

    private void StartPlayingWestWorldVideo() {
        fullScreen = new JarvisMediaPlayer(Utilities.getJarParentDir() + "videos\\violentdelights.mp4");
        BlackBoard.mainPanel.mouth.sayThisOnce("Yes indeed!");
    }

    private void StopReadingTheNews() {
        if (MSNNews.KeepReading) {
            MSNNews.KeepReading = false;
            BlackBoard.mainPanel.mouth.sayThisOnce("Alright " + MainPanel.LastFacialRecognitionUsername);
            msnnews = null;
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", I wasn't reading"
                    + " the news in the first place");
        }
    }

    private void PrintHelpOnScreen() {
        for (String item : coreFunctionalityPrompt) {
            BlackBoard.mainPanel.mouth.printToScreen(item);
        }
        BlackBoard.mainPanel.mouth.sayThisOnce("That "
                + "should be more than enough " + MainPanel.LastFacialRecognitionUsername);
    }

    private void ReadNewArticlesAndDescription() {
        new Thread() {
            @Override
            public void run() {
                if (!MSNNews.KeepReading) {
                    msnnews = new MSNNews(true);
                }
            }
        }.start();
    }

    private void ShowActualArticle() {
        //support method for news module

        if (MSNNews.KeepReading && LinkOfCurrentlyReadNews != null) {
            try {
                //push website data to front end.
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + LinkOfCurrentlyReadNews});
                BlackBoard.mainPanel.mouth.sayThisOnce("On it " + MainPanel.LastFacialRecognitionUsername + "!");
            } catch (Exception ex) {
                BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry, I am unable to open the link!");
            }
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("I do not understand, no currently read news article, " + MainPanel.LastFacialRecognitionUsername);
        }
    }

    private void ReadNews() {
        new Thread() {
            @Override
            public void run() {
                if (!MSNNews.KeepReading) {
                    msnnews = new MSNNews(-1);
                }
            }
        }.start();
    }

    private void GreetUserJokesNewsWeatherAdvice() {
        //morning stats

        CheckAndGreatAppropriate();

        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        sayWhatDayOfTheWeekItIs("Today is");
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
        //the time right now
        sayWhatTheTimeIs("The time right now is"); //nanana

        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
        }

        getWeather(ProjectJarvis.WEATHERLOCATION, true);

        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
        }

        tellInspirationQuote();

        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
        }

        tellJoke(100, false, true); //100% humour settings, do not ask user for jokes (boolean)

        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
        }

        new Thread() {

        }.start();

        new Thread() {
            @Override
            public void run() {
                if (!MSNNews.KeepReading) {
                    msnnews = new MSNNews(ProjectJarvis.FIRST_FIVE_NEWS); //5 interesting news
                }
            }
        }.start();
    }

    private void StopSMSMessagingService() {
        //stop sms listening service
        if (ProjectJarvis.SMSMessage == true) {
            ProjectJarvis.SMSMessage = false;
            SMSListener.KeepRunning = false; //stop the service
            BlackBoard.mainPanel.mouth.sayThisOnce("Text messaging service disabled!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Text messaging service is already disabled!");
        }
    }

    private void StartSMSMessagingService() {
        //start sms listening service
        if (ProjectJarvis.SMSMessage == false) {
            ProjectJarvis.SMSMessage = true;
            StartSMSMessageListener(); //START THE SERVICE
            BlackBoard.mainPanel.mouth.sayThisOnce("Text messaging service enabled!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Text messaging service is already enabled!");
        }
    }

    private void EnableFacialRecognition() {
        if (ProjectJarvis.DisableFaceRecog == true) {
            ProjectJarvis.DisableFaceRecog = false;
            BlackBoard.mainPanel.mouth.sayThisOnce("Facial recognition enabled!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Facial recognition is already enabled!");
        }
    }

    private void EngageTheGoogleSpeechAPI() {
        //google api listen
        GoogleSpeechApi gsa = new GoogleSpeechApi();
        BlackBoard.mainPanel.textEntry = true;
        BlackBoard.mainPanel.StringEntry = gsa.getResult().toLowerCase();
        ProcessCommand();
    }

    private void DisableFacialRecognition() {
        if (ProjectJarvis.DisableFaceRecog == false) {
            ProjectJarvis.DisableFaceRecog = true;
            BlackBoard.mainPanel.mouth.sayThisOnce("Facial recognition disabled!");
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Facial recognition is already disabled!");
        }
    }

    private void PlayGameOfThroneVideo() {
        fullScreen = new JarvisMediaPlayer(Utilities.getJarParentDir() + "videos\\got.mp4");
        BlackBoard.mainPanel.mouth.sayThisOnce("I see you are game of throne fan!");
    }

    private void AttemptToRunTheLittleMeGame() {
        if (littleme != null) {
            littleme.TerminateAction = true;
            BlackBoard.mainPanel.mouth.sayThisOnce("The little me game has been closed!");
            littleme = null;
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("Now attempting to start the game!");
            new Thread() {
                @Override
                public void run() {
                    littleme = new LittleMeGameForGUI();
                }
            }.start();
        }
    }

    private void TerminateProgramSlowly() {
        BlackBoard.mainPanel.mouth.printToScreen("Restart Required!");
        BlackBoard.exitRoutine(); //Exit routine here.

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

        System.exit(0); //Terminate the JVM here.
    }

    /**
     * reset all variables to default value
     */
    public void resetTextEntryVariableTypesScreen() {
        Utilities.resetTextCollectorScreen();
        CommandExecuted = true;
    }

    //stop playing the national anthem 
    private void stopNationalAnthem() {
        anthem.stopsound();
    }
    SystemSounds anthem; //sound initialization

    private void playNationalAnthem() {

        new Thread() {
            @Override
            public void run() {
                anthem = new SystemSounds("anthem");
                anthem.playsound();

                AnthemIsCurrentlyPlaying = true;

                //78000 is the length of the national anthem file 
                Timer changeAnthemPlayStatus = new Timer(78000, (ActionEvent e) -> {
                    AnthemIsCurrentlyPlaying = false;
                });

                changeAnthemPlayStatus.setRepeats(false);
                changeAnthemPlayStatus.start();
            }
        }.start();

    }

    /**
     *
     * @param phraseToSayBefore default = It is now
     *
     */
    private void sayWhatTheTimeIs(String phraseToSayBefore) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int m = calendar.get(Calendar.MINUTE);  // Values from 0 - 59
        int h = calendar.get(Calendar.HOUR_OF_DAY);    // Values from 0 - 23
        boolean dn = false;

        String time;
        String daynight;

        if (h > 12) {
            dn = true;
            h = h - 12;
        }
        if (h == 0) {
            h = 12;
        }

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 0 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            daynight = " eh m"; //A.M. is read as a single word with regard to our animation function so we cheat here.
        } else {
            daynight = " pee m";  //P.M. is read as a single word with regard to our animation function so we cheat here.
        }

        if (m < 10) { //if minutes are less than ten, process it to sound natural, we don't say 5 zero one pm
            if (m == 0) {
                time = phraseToSayBefore + " " + h + daynight;      //if minutes are at zero just say 5 pm
            } else {
                time = phraseToSayBefore + " " + h + "! oh " + m + daynight;    // else lets say oh instead of zero

            }
        } else {      //if minutes are greater than ten just say them normal
            time = phraseToSayBefore + " " + h + "! " + EnglishNumberToWords.convertLessThanOneThousand(m) + daynight;
        }
        message = time;
        BlackBoard.mainPanel.displayInfo.displayInformation("It is now " + now.toString());
        BlackBoard.mainPanel.mouth.sayThisOnce(message, true); //,true
    }

    //get the day of the week
    /**
     *
     * @param phraseToSayBefore default = It is
     */
    private void sayWhatDayOfTheWeekItIs(String phraseToSayBefore) {

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayofweek) {
            case 1:
                BlackBoard.mainPanel.mouth.sayThisOnce(phraseToSayBefore + " Sunday. Tomorrow is definitely a monday, no lie");
                break;
            case 2:
                BlackBoard.mainPanel.mouth.sayThisOnce(phraseToSayBefore + " Monday. Monday are cool");
                break;
            case 3:
                BlackBoard.mainPanel.mouth.sayThisOnce(phraseToSayBefore + " Tuesday.");
                break;
            case 4:
                BlackBoard.mainPanel.mouth.sayThisOnce(phraseToSayBefore + " Wednesday. Awesome!");
                break;
            case 5:
                BlackBoard.mainPanel.mouth.sayThisOnce(phraseToSayBefore + " Thursday. Fantastic!");
                break;
            case 6:
                BlackBoard.mainPanel.mouth.sayThisOnce("Awesome saunce! " + phraseToSayBefore + " Friday");
                break;
            case 7:
                BlackBoard.mainPanel.mouth.sayThisOnce("Cool " + phraseToSayBefore + " Saturday");
                break;
        }
    }

    String unParsedWeatherString;
    String currentWeather;

    private void getWeather(String location, boolean advice) {

        if (!ProjectJarvis.BASEWEATHERURLDONOTCHANGE.equalsIgnoreCase("http://rss.theweathernetwork.com/weather/")){
            DisplayTrayIcon.trayIcon.displayMessage("Weather service not available. Weather link should be theweathernetwork", "Project Jarvis Weather", TrayIcon.MessageType.INFO);
            return;
        }
        
        try {
            File fXmlFile = new File("weather.xml");
            FileUtils.copyURLToFile(url, fXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("description");

            Node nNode = nList.item(2);

            //System.out.println("\nCurrent Element :" + nNode.getNodeName());
            unParsedWeatherString = nNode.getFirstChild().getTextContent();
            //System.out.println("Content : " + ruff);

            String weather = unParsedWeatherString;
            int index = weather.indexOf(",");
            currentWeather = weather.substring(0, index);
            index = weather.indexOf("&");
            String temp = weather.substring(index - 2, index);
            int minus = temp.indexOf("-");

            currentWeather = "The weather in " + location + " is " + currentWeather.toLowerCase() + " with a temperature of " + ConvertToFahrenheit(temp) + " degrees fahrenheits";

            //say what is the current is like
            message = currentWeather;
            BlackBoard.mainPanel.mouth.sayThisOnce(message);

            if (advice) {
                //give advice
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 90) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is hot outside. Stay hydrated!");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend staying in the shades");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 75 && Integer.parseInt(ConvertToFahrenheit(temp)) < 90) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is a little bit hot outside. Seems to be a good weather to go out!");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend some cold drinks");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 65 && Integer.parseInt(ConvertToFahrenheit(temp)) < 75) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is really nice outside!");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend you wear a light jacket today");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 50 && Integer.parseInt(ConvertToFahrenheit(temp)) < 65) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is a little bit chilly outside");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend you wear a light jacket today");
                    BlackBoard.mainPanel.mouth.sayThisOnce("We don't want you catching a little cold");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 40 && Integer.parseInt(ConvertToFahrenheit(temp)) < 65) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is cold outside");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend you wear a proper jacket today");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) >= 0 && Integer.parseInt(ConvertToFahrenheit(temp)) < 40) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is cold outside");
                    BlackBoard.mainPanel.mouth.sayThisOnce("This is only a suggestion, but I would recommend you wear a proper jacket today");

                } else if (Integer.parseInt(ConvertToFahrenheit(temp)) < 0) {

                    BlackBoard.mainPanel.mouth.sayThisOnce("It is bitching cold out, grab multiple jackets and stay alive!");

                }
            } //end of advice 

            return;

        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
        }

        message = "Hello " + MainPanel.LastFacialRecognitionUsername + ", something went wrong and I don't know what it is";

        BlackBoard.mainPanel.mouth.sayThisOnce(message);

    }

    public void tellInspirationQuote() {
        int index = (int) (random.nextInt(inspirationQuotes.length));  // retrieve a random quote from the file
        BlackBoard.mainPanel.mouth.sayThisOnce(inspirationQuotes[index]);
    }

    public void tellJoke(int variable, boolean AskUserForJokes, boolean AvoidKnockKnockJokes) {

        if (MainListener.SMSResponse) {
            KeepSMSTextingUser = true; //keep texting user.
        }

        random = new Random(); //a random variable

        String lines[]; //array to hold all joke files. 

        int humorLevel = variable / 5 * 5; //humor levels 5,10,15,20,25,30

        lines = loadStrings("jokes/" + String.valueOf(humorLevel) + ".txt"); //load a particular humar file in batches of five.

        int index = (int) (random.nextInt(lines.length));  // retrieve a random joke from the file

        //it is a knock knock joke
        if (lines[index].contains("[Knock]") && !AvoidKnockKnockJokes) {

            //next reply to tell user after they say who is there.
            String nextReply = lines[index].substring(lines[index]
                    .indexOf("NextReply=") + "NextReply=".length(), lines[index].indexOf("TextLink=") - 1);

            String textReply = lines[index].substring(lines[index]
                    .indexOf("TextLink=") + "TextLink=".length(), lines[index].indexOf("PictureLink=") - 1);

            String pictureLink = lines[index].substring(lines[index].indexOf("PictureLink=") + "PictureLink=".length(), lines[index].length());

            //wait for user to enter input 
            if (lines[index].contains("[AwaitReply]")) {
                //bring up the prompt - expecting who is there.

                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();

                BlackBoard.mainPanel.textEntry = true; //enter texts.

                SpecialTextEntry = true;

                BlackBoard.mainPanel.mouth.askQuestion("Knock! Knock!");

                SessionManager.WaitForInputAndProcessIt();
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }

                if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who's there")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("whos there?")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who there?")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yes")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who is there")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who there")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("whos their")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who's their?")) {

                    //after next reply, tell em the next cue. which is nextReply
                    //Bring up the prompt again and expect the nextReply + "who" or "who?"
                    SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                    BlackBoard.mainPanel.textEntry = true; //enter texts.

                    BlackBoard.mainPanel.mouth.askQuestion(nextReply);

                    SessionManager.WaitForInputAndProcessIt();
                    try {
                        SessionManager.input.join();
                    } catch (InterruptedException ex) {
                    }

                    //the reply containing the who statement. 
                    if (BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who")
                            || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("who?")) {

                        if (!pictureLink.contains("assets")) { //picture cannot be located in the assets folder 
                            //JOptionPane.showMessageDialog(null, "Does not contain assets, trying to retrieve from an online loation!", AINAME, JOptionPane.INFORMATION_MESSAGE);
                            MainPanel.displayInfo.displayImage(pictureLink);
                        } else { //picture can be located in the assets folder.

                            File guess = new File(Utilities.getJarParentDir() + pictureLink);

                            try {
                                MainPanel.displayInfo.displayImage(guess.toURI().toURL());
                            } catch (MalformedURLException ex) {
                            }
                        }

                        BlackBoard.mainPanel.mouth.sayThisOnce(textReply);

                    }

                } else { //give them attitude
                    BlackBoard.mainPanel.mouth.sayThisOnce("I can understand if you don't want to play!");
                }

            } //end of knock knock jokes.

        } else if (lines[index].contains("[Knock]") && AvoidKnockKnockJokes) {

            BlackBoard.mainPanel.mouth.sayThisOnce("I have got no jokes for you right now!");

        } else { //if it is a knock knock joke
            BlackBoard.mainPanel.mouth.sayThisOnce(lines[index]);
        }

        //ask user for a joke
        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();

        //only if request
        if (AskUserForJokes) {
            BlackBoard.mainPanel.textEntry = true;
            BlackBoard.mainPanel.mouth.askQuestion("Do you feel like telling me a joke?");

            SessionManager.WaitForInputAndProcessIt();

            try {
                SessionManager.input.join();
            } catch (InterruptedException ex) {
            }

            if ((BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("yes")
                    || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("yeah")
                    || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("ya"))
                    || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("y")
                    || BlackBoard.mainPanel.StringEntry.contains("yes")
                    || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                    || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {
                BlackBoard.mainPanel.mouth.askQuestion("Tell me the joke please");

                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                BlackBoard.mainPanel.textEntry = true;
                BlackBoard.mainPanel.sessionManager.collectInputValues();

                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }

                BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

                BlackBoard.mainPanel.mouth.sayThisOnce(random.nextBoolean() ? "That was funny!" : "That was hilarious!");
                playLaughSound();

            } else { //if user says yes

                BlackBoard.mainPanel.mouth.sayThisOnce("Alright then, maybe next time");
            }

        } //ask user for jokes

        KeepSMSTextingUser = false; //stop texting user. 
        SpecialTextEntry = false;
        //if yes, collect joke
        //if no, move on 
    }

    private String[] loadStrings(String jokesjokes1txt) {
        Path filePath = new File(jokesjokes1txt).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = null;
        try {
            stringList = Files.readAllLines(filePath.toAbsolutePath(), charset);
            return stringList.toArray(new String[]{});
        } catch (IOException ex) {
        }
        return null;
    }

    public static void CheckAndGreatAppropriate() {

        if (ScriptGreeting == true) {
            return;
        }

        switch (Utilities.IsMorningAfterNoonOrNight()) {
            case 1:
                BlackBoard.mainPanel.mouth.sayThisOnce("Good morning");
                break;
            case 2:
                BlackBoard.mainPanel.mouth.sayThisOnce("Good afternoon");
                break;
            case 3:
                BlackBoard.mainPanel.mouth.sayThisOnce("Good evening");
                break;
            case 4:
                BlackBoard.mainPanel.mouth.sayThisOnce("Good evening, it is starting to get dark outside"); //even though it is night
                break;

        }
    }

    /**
     *
     * @return check Internet connection status
     */
    public boolean checkInternetConnectivity() {

        BlackBoard.mainPanel.mouth.printToScreen("Attempting to verify internet connection status!");
        try {
            final URL googleURL = new URL(HTTPWWWGOOGLECOM);
            final URLConnection conn = googleURL.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

    }

    private static final String HTTPWWWGOOGLECOM = "http://www.google.com";

    SystemSounds laugh; //sound initialization

    private void playLaughSound() {

        new Thread() {
            @Override
            public void run() {
                laugh = new SystemSounds("laugh");
                laugh.playsound();
            }
        }.start();

    }

    public void sayFeelingAndSetEmotionsVariable(String[] firstLineOptions, Random random) {

        String contentAndFeelings = firstLineOptions[random.nextInt(firstLineOptions.length)];

        if (contentAndFeelings.contains("[AMAZING]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.AMAZING);
        } else if (contentAndFeelings.contains("[LOVE]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.LOVE);
        } else if (contentAndFeelings.contains("[LOW]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.LOW);
        } else if (contentAndFeelings.contains("[TIRED]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.TIRED);
        } else if (contentAndFeelings.contains("[FOODQUESTION]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.FOODQUESTION);
        } else if (contentAndFeelings.contains("[HOPEFUL]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.HOPEFUL);
        } else if (contentAndFeelings.contains("[EXCITED]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.EXCITED);
        } else if (contentAndFeelings.contains("[ANGRY]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.ANGRY);
        } else if (contentAndFeelings.contains("[BLESSED]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.BLESSED);
        } else if (contentAndFeelings.contains("[GUILT]")) {
            SessionManager.session.getHumanJarvis().setHumanmood(MoodState.GUILT);
        }

        BlackBoard.mainPanel.mouth.sayThisOnce(contentAndFeelings
                .replace("[AMAZING]", "")
                .replace("[LOVE]", "")
                .replace("[LOW]", "")
                .replace("[USER]", (random.nextBoolean() ? (MainPanel.LastFacialRecognitionUsername.isEmpty() ? ProjectJarvis.SALUTATION : MainPanel.LastFacialRecognitionUsername) : ""))
                .replace("[TIRED]", "")
                .replace("[FOODQUESTION]", "")
                .replace("[FEELING]", "")
                .replace("[HOPEFUL]", "")
                .replace("[EXCITED]", "")
                .replace("[ANGRY]", "")
                .replace("[BLESSED]", "")
                .replace("[GUILT]", "")
        );

    }

    private String ConvertToFahrenheit(String temp) {
        Double celsius = Double.parseDouble(temp);
        return String.valueOf((int) (celsius * 9 / 5.0) + 32);
    }

    /**
     * reload the core function and the social function of the application.
     */
    private void ReloadBackStory() {
        //load social setting and replies
        BlackBoard.mainPanel.mouth.printToScreen("Attempting to reload backstory");
        backStory = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/backstory.txt");

        //load core functionality prompts and replies
        coreFunctionalities = loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/coreFunction.txt");

        //will initialize arrays in constructor.
        socialPrompt = new String[backStory.length];
        socialReply = new String[backStory.length];

        //Core functionality
        coreFunctionalityPrompt = new String[coreFunctionalities.length];
        coreFunctionalityAnswer = new String[coreFunctionalities.length];

        AINAME = backStory[0].split("\\.")[1];

        //initialize with prompt and answer.
        //social commands.
        for (int i = 0; i < backStory.length; i++) {
            String[] tokens = backStory[i].split("\\.");
            //System.out.println(tokens[0]); //DEBUG ASSIST
            socialPrompt[i] = tokens[0];
            if (backStory[i].contains("[LINK]") || backStory[i].contains("[QLINK]")) {
                socialReply[i] = backStory[i].substring(backStory[i].indexOf(".") + 1, backStory[i].length());
            } else {
                socialReply[i] = tokens[1];
            }
        }

        //upload core functionalities 
        for (int k = 0; k < coreFunctionalities.length; k++) {
            coreFunctionalityPrompt[k] = coreFunctionalities[k].split("\\.")[0];
            coreFunctionalityAnswer[k] = coreFunctionalities[k].split("\\.")[1];
        }
        BlackBoard.mainPanel.mouth.sayThisOnce("Backstory reload complete!");
    }

    /**
     * this method try to find content in which user is talking and find the
     * next possible question to ask
     */
    Random random = new Random();

    public void PossibleNextQuestionToAsk() {
        int selectRandomCommonTopic = random.nextInt(numberOfPossibleCT);
        if (-1 != findCommonTopicArray[selectRandomCommonTopic].getQuestion().indexOf("[LAUGH]")) {
            playLaughSound(); //laugh
        }
        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
        BlackBoard.mainPanel.textEntry = true; //enter texts.

        String actionStatement = findCommonTopicArray[selectRandomCommonTopic].getQuestion();

        String actionStatementToSay = actionStatement.replace("[USER]", MainPanel.LastFacialRecognitionUsername.isEmpty() ? "" : MainPanel.LastFacialRecognitionUsername)
                .replace("[LAUGH]", "")
                .replace("[FOODQUESTION]", "")
                .replace("[FEELING]", "")
                .replace("[HAVEFEELING]", "")
                .replace("[AIFIRSTNAME]", SessionManager.session.getHumanJarvis().getObjectName() == null ? "Jarvis" : SessionManager.session.getHumanJarvis().getObjectName());

        BlackBoard.mainPanel.mouth.askQuestion(actionStatementToSay);
        SpecialTextEntry = true;
        //EXPECT USER ANSER
        //ANSWER
        SessionManager.WaitForInputAndProcessIt();
        try {
            SessionManager.input.join();
        } catch (InterruptedException ex) {
        }
        SpecialTextEntry = false;

        BlackBoard.mainPanel.mouth.printToScreen(BlackBoard.mainPanel.StringEntry);

        //computer response begins here
        Boolean foundKnowledgeInMemory = false;
        //if the script reader is running stop it.

        //loop through all the scripts in memory and 
        for (int location = 0; location < SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers.length; location++) {
            //if current topic is different from new topic, switch to new topic
            if ((-1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][0])
                    || -1 != BlackBoard.mainPanel.StringEntry.indexOf(SessionManager.mainListenerTextEars.KnowledgeBaseIdentifiers[location][1])) && !SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location].equalsIgnoreCase(scriptReader.scriptLocation)) {
                foundKnowledgeInMemory = true;
                if (scriptReader.ScriptStillRunning == true) {
                    scriptReader.ScriptStillRunning = false; //terminate current sub script
                }
                scriptReader = new ScriptReader(SessionManager.mainListenerTextEars.KnowledgeBaseInScriptsLocation[location]);
                scriptReader.start();
                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                //while (scriptReader.isAlive()); //busy wait.
                break;

            } //end of if statement for checking topics

        } //end of for loop

        //if foundKnowledge of new topic in script, return, if not return
        resetTextEntryVariableTypesScreen();

        if (foundKnowledgeInMemory) {
            return;
        }

        if (random.nextBoolean() && random.nextBoolean()) {
            PossibleNextQuestionToAsk();
        }
    }

    /**
     * returns the current power level for the computer
     */
    private void getCurrentPowerLevel() {
        PowerStatus.INSTANCE.GetSystemPowerStatus(batteryStatus);

        int batteryPercentLeft = (Integer.valueOf(batteryStatus.getBatteryLifePercent().substring(0, batteryStatus.getBatteryLifePercent().length() - 1)));

        BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", power level at " + String.valueOf(batteryPercentLeft) + "%");

    }

    private boolean CheckForClearStatements(String commandOrTextToBeProcessed, String ignoreItem) {
        return commandOrTextToBeProcessed.toLowerCase().contains(ignoreItem.toLowerCase());
    }

    private void ResizeAllWindows() {
        BlackBoard.mainPanel.mouth.sayThisOnce("Resize is impossible in this version");
        DisplayTrayIcon.trayIcon.displayMessage("Resize is impossible in this version", 
                "Project Jarvis", TrayIcon.MessageType.INFO);
    }

}
