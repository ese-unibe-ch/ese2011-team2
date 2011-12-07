# Simple Calendaring application
Requirement specified at: http://ese.unibe.ch/exercises/simple-ui

## Compiling
This is a play framework project, you don't need to compile it

## Running

You need to have [play](http://www.playframework.org/) installed, in the folder where his README is execute:

This Application has dependencies besides play!, install them with
	
	play dependencies --sync
	
After that you can start the Application with:

    play run
    
    and go to the login page: http://localhost:9000
    
Or start the test mode with

	play test
	
	and go to the test page: http://localhost:9000

For running the application with assertions on, use 

    play run -ea
    
## Using with eclipse
To build the files needed to import the project into eclipse run (in the directory where this README file is)

    play eclipsify

## Available users for testing
    erwann, pw: ese
    judith, pw: ese
    aaron, pw ese
