 Jordan Frazier (jrf30)
 Refactoring 9/29
 
 The main design issue that I had on the UI was duplicated code in MainView.java. The main problem was that in each event handler, I was calling several methods.
 For example, if Reset was pressed, I would call methods to clear the grid and populate it with the current parameters from the UI. If simulation was
 pressed, I needed to clear everything from the root/scene except for the generic buttons, then create the grid, custom sliders/buttons, and
 display everything. This meant that reset and simulation event handlers were very similar, but in some cases I was creating All buttons instead of
 just Custom buttons. I refactored this duplicate code into one method, initSimulation, that removes everything from the root and repopulates
 everything with new grid and custom buttons. The readability of the code greatly increased, and the code is now more concise. However, the trade off
 is that I am now unneccessarily deleting generic buttons and creating them again. 
 Another issue I had was with my SimulationSlider, which was using a lot of duplicate code that has been refactored.
 
 Our main issue, that is still in progress, in changing our Configuration. It is now a global variable, which is unneccessary, and makes it hard to trace
 the path of where the parameters are coming from or being set. Creating a local Configuration that is passed into only methods that require it is
 better design. This commit has added some of the base code for the coming change, that Charles is working on currently, so I will be able to implement
 it when it is ready. This is a major refactoring throughout our code, since we depended on being able to pass the Configuration around between
 front and backend, so our simulations can get the updated values the UI sliders change. With the local Configuration, we will have to pass
 pointers of our local configurations into the necessary simulations and sliders, so when the slider updates a value in the configuration, the slider
 will be able to get that new value and change the rules of the simulation accordingly. This new design will be better in that it will be easier to debug, 
 because you will be able to trace the path back to where the Configuration was created and what parameters it is passing that might have caused the error. 
 
 There are still changes that need to be done. I am moving all of the constants into a resource file currently. Magic values will be placed
 either as constants or inside the resource file as well. And the main change will be the refactoring for the Configuration. 
 
 The commit is 105c6b21 on branch jfrazier.