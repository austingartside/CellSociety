# README

#### Duke CompSci 308 Cell Society Project Team 19

Jordan Frazier (jrf30), Austin Gartside (aeg36), Charles Xu ( ) <br>
Sept 13 - October 2, Hours: 100+

Jordan: Front-end, GUI <br>
Austin: Back-end, Simulations (Note: my branch was actually newstaginglol because the austin branch got messed up at some point)<br>
Charles: XML, Configuration<br>

Jordan:
* Used online JavaFX API Documentation

Austin: 
* Used online Java Documentation

Charles:
* TODO

Instructions on starting project:
* Open the Main.java class and run program. Program should open the main screen that offers a user options to begin simulation, switch 
simulation, and more.

Test:
* ConfigurationLoaderTest.java and ConfigurationTest.java
* TODO: Charles ? 

Resource Files:
* XML files must be in a specific format to run correctly, as shown in each of our data files. 
* Resource files are located in resources package and define string constants used in our program

Interesting Information: 
* The Game Of Life with 40+ rows/cols produces fascinating patterns 

Bugs:
* UI:
    * Hexagon rendering does not always line up hexagons in perfect order, depending on the number of rows/cols the user has specified.
    * Patch cells on Sugar Simulation do not line up when using triangles, or perfectly on hexagons either
    * Comboboxes cannot be selected twice in a row, need to click another button before reselecting
    * Triangle uses a hack to ensure that the number of triangles in a row is odd, so that the next row will flip the triangle correctly
* Back-end:
    * Running the simulation at a very fast speed makes it look like the simulation sin't working.
    * Changing states in the Ant Simulation to the food and home source does not affect pheromone levels
    * Sharks win a lot in Wat-or because they get to move first
* XML
    * Save only works for rectangular shapes ? TODO

Extra Features:
* 

Impressions: 
* Jordan : 
I enjoyed working on this project and am happy with the final result. I wish I had more time to spend on it to get it bug-free. I liked how
it was up to us to decide how the work was split up, but disappointing that we did not all get to learn the same topics and collaborate more.
It would be interesting to meet with other groups to discuss how they implemented the features. 
* Austin:
I enjoyed working on the project as well, and I'm proud of what we were able to do. I wish that it was more spread out so we could add more of
the features. It was great getting to work in a group on the large scale project, but I think it would have been mroe enjoyable if we had a little more time. I agree
with Jordan in that I wish we had some time to talk to other groups about what they did and what they were able to acomplish.