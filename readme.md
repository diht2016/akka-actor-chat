# Akka Actor Chat

[![Build Status](https://travis-ci.com/diht2016/akka-actor-chat.svg?branch=main)](https://travis-ci.com/diht2016/akka-actor-chat)

This project is a [homework task](task.md). The aim is to create a backend of web chat application using akka actors as a state storage.

## How to run the app

This app requires [Scala Build Tool](https://www.scala-sbt.org/) to build and run. Run `sbt` command in terminal. If SBT is installed, it will start the SBT console (might take some time), which has its own set of commands.

When the SBT console is loaded, enter `run` command to run the server, the compiler will build the project if not compiled yet.

The application server will be available at `http://localhost:8080`.

You can use [requests.js](requests.js) (in DevTools Console) to send requests from the browser.

To stop the server, simply press Enter in the command line.

Enter `test` command to launch all tests. Enter `exit` command to leave SBT console.
