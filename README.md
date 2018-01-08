# ProjectJarvis
This is an artificial intelligence simulation program. It is a home assistant. It is a simulation of my favorite doctor who character (Amy Pond). It works just like Siri or Cortana, it is written in Java and PHP.

Summary:
This application can/has:

Significant future is more developers work on the application. 
Behave like it has its own mind, but it really doesn't yet.
Has a cool interface.
Tell Jokes (Humor level can also be adjusted. Ranges from 0-100)
Do customized things at certain times (Like telling time, date and weather like the movie Iron-Man)
Replies back to scripted Question (Can also show pictures)
Reply back to curse words (Scripted Of Course - even displays images)
Behave like Siri or Cortana in some ways
Do basic math operation with sarcasm.
Can be voice activated (not working right now - needs to be updated to IBM Speech-To-Text instead of Google Speech-To-Text)
Speak (If enabled)
Can search online for certain things and return possible results (Uses Google Search - Would like to replace this) - NOT CURRENTLY WORKING
Can communicate with owner using SMS Messages (Has its own phone number if setup with nexmo.com)
Can do basic automation around the house (Using https://ifttt.com/)
Provides status updates on computer power level (Laptops)
Do basic facial recognition of its user if trained (Not enabled by default) - NOT CURRENTLY WORKING PROPERLY (uses eugene faces) - HELP IMPROVE PLEASE
Can engage in basic interaction with a person while showing emotion, pictures and video to express itself. (Emotion section not working properly - Only shows happy)
Can suggest things to the user based on what has been typed into the application as input.

To USE JARVIS:
Download Sources Code, Install Java (32 bit) Version, Install VLC Video Player (32-bit edition), Set up PHP files on a domain, Set up SMS Account, Set up Database, Set IBM Speech Account, Run just like any other Java program once the config file has been updated. It is better if ran through the command prompt so that you will have access to debug information (the BAT file in this case).

To talk to JARVIS through written words, run jarvis and wait for Jarvis prompt (OR press the tilde symbol on your keyboard). Type what you want to say, then press enter. Basic commands for jarvis can be found in the core functionality. Jarvis will run a particular command if it finds all of the words pre-scripted in the coreFunctionality.txt/backstory.txt files or other files that behaves in such manner. 

This is a fun project that I started last year (Dec 2016). It has built-in social and core functionalities. The social functionalities involve Amy opening up web links and telling the time and weather (This can easily be changed in "ProjectJarvis\JAVIS\dist\assets\backstory\Build1backstory.txt"). It can also tell jokes (Can also be edited quite easily. Jokes located in the "jokes" folder). Humor level in the application can also be adjusted - ranges from 0 to 100. Amy Pond personality can also be prompted with cues and it gives appropriate replies (relies on application backstory in the build folder).

For example, if you ask Amy "What is your mother's name?", amy would replies and say whatever you program it to say.

A person can also communicate with Jarvis (any personality) using SMS text. This make Jarvis accessible everywhere in the world once online. Speech is also possible by calling Jarvis, but I have not designed this yet.  

This is a netbean Project (Written using the Netbeans IDE). Dependencies already included in the GitHub Repo. The "dist" folder contains the exact dependencies that the application needs at the current moment. You might have to setup the dependencies in netbeans in order for the application to work. (Haven't learned how to use Ant yet)

Certain things can also be easily programmed to happen at certain times by editing the file in ("ProjectJarvis\JAVIS\dist\assets\backstory\Build1mainAILogic\script.txt") using the scripting rules. The "Amy Pond Personality" is determined by what is in this Folder ("ProjectJarvisJAVISdistassetsbackstoryBuild1"). In other words, if another folder were created in the same location and the Build updated in the configuration file, you can always have another complete mind for your AI.

Inside the ("ProjectJarvis\JAVIS\dist\assets\backstory\Build1\buildKnowledge"), information that the personality knows about will be contained here. Jarvis is able to switch from one topic to the other quite easily. This happens when it gets cued that you would like to talk about another topic. Of course, everything is scripted. 

Under the build folder, the emotions folder determines what the personality would say depending on the Power Level of the machine. I could not come up with any other factors from a computer, feel free to upgrade. Memes folder just contain memes because I like memes, good ones. Vanities folder refers to question that pertain to emotion, something jarvis cannot do yet. Possible replies are scripted in the Vanities folder. 

In ("ProjectJarvis\JAVIS\dist\assets\backstory\Build1\coreFunction.txt"), jarvis core functionalities are made available here. If a new functionality were to be added in the source code, it would also need to be added here in other for it to work. This works like the back story except for the fact that code here actually does something, while the backstory is more like the personalities histories and relationship with others (Got the idea from WestWorld Series, Scripted).

For example, you can say "what is the time" or any sentence that has the phrase "What is the time" in it and it will tell you the time.

The KnowledgeBaseLocation text file helps the program identify all the knowledge bases in the application. It needs to be updated once a new knowledge base is added in the buildKnowledge folder.

It also has built-in home assistance functionality that can be triggered by voice and SMS-Text messages(once setup and enabled). Voice recognition functionality of the program not currently working (Currently using Google Speech Engine - Was Working, Stopped Working). I would like to replace that section of the code with IBM watson speech-to-text and then parse the text before running through the command processing center. This can easily be done by replacing the "GoogleSpeechApi" class. 

The application can also search for things online (Uses the Google Api - not currently working). This is nothing serious, wrote this code to see if I could do it. It can also be easily replaced. This application has been written component by component. More components can easily be added/remove from/to the application. 

It has an in-built calculator that can easily be operated by speech (Provided speech engine is working, enabled)

The version 102 or version 2 of the program has less bugs. The code runs better and it is faster. Jarvis now has a configuration file where the voice of speech engine can easily be changed (Following Speech Engine Name in IBM Watson Speech Engine Name). At the same time, weather information can now be updated easily. Application developers can easily switch to another speech engine by changing certain information in PHP file - information provided on GitHub Repo. The PHP files need to be hosted on a domain for it to work, and the link to those files needs to be configured in the "config" file of the AI simulation program.

Anyone can host JARVIS application for themselves by creating certain accounts with some websites and updating the configuration file. In the case of Jarvis, that would be application initialization.

This application requires internet connection.

Account with these website are needed in other to run Jarvis:

Nexmo SMS Service (A phone number is required, YOUR AI's phone number)
IBM Watson Text-to-Speech Engine


Other things that needs to be done involves hosting the PHP files on a website. PHP files needs to updated before it is hosted. A mySQL database is needed to host SMS communication between Jarvis and Nexmo SMS Service. Database structure sample also included in the GitHub Repo for the application.

Below is sample weather code in the config file: usmn0252 (theweathernetwork.com, RSS Service)

Jarvis can be reached from anywhere in the world through the SMS Service (needs to be setup through nexmo (PHP files, config files) and enabled).

The other speech engines that I have been using with this program were not reliable on the long run. IBM Watson speech engine is a better choice when it comes to speech engines. The voice of JARVIS is much more different these days in comparison to the video on youtube (Big-Me blog post, link provided). At the same time, it is now changeable very easily.

JARVIS can really do a lot of things. Especially if integrated with the If This Then That Website

In other words, jarvis can initiate web links at certain times automatically if this is built into the script.txt (This is script that determine what Jarvis needs to do at certain date-time). Jarvis events are programmed to occur once a day by default. 
