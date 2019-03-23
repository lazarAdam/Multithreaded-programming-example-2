# Multithreaded-programming-example-2
A program that uses the concept of thread pools, callabls, futures <br/>

Implemented a program that demonstrates the principle of multi-threaded programming, and concurrent objects. The program processes real weather data from 1,200 files each file contains data form different weather stations in the United State for one month days in each station. The program creates 10 threads each thread processes a random number of files in parallel and finds 5 absolute lowest or highest temperatures recorded in a given range of years and months. About 80% of the processing is done in parallel.
# notes:
This repository contains only 9 out of 1200 weather data files for testing the program functionality.<br/>
Go to the following link: ftp://ftp.ncdc.noaa.gov/pub/data/ghcn/daily/ and download "File:ghcnd_hcn.tar.gz  " to accesses the files of all the weather stations.<br/> weather station files must be in "Data files" folder in order for the program to get the correct path
<br/> this rep
