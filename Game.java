/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author MURAT KARATAŞ
 */
class Game {
  
    PlayerTurnList<Explorer> explorers; 
    DoubleNode<Explorer> currentPlayerNode; 
    int currentRoomNumber;
    boolean isTurnOrderReversed;
    int nextEchoStoneToFind;
    int nextEchoStoneToUse;
    int winConditionStoneCount = 3;
    
    boolean isGameOver;
    boolean isGameWon;
    
   
    MyLinkedList<Timeline> allTimelines; 
    Timeline currentTimeline; 
    int nextTimelineId = 0;

    Scanner scanner;
    Random random;

    public Game() {
        this.explorers = new PlayerTurnList<>(); 
        this.allTimelines = new MyLinkedList<>(); 
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.isGameOver = false;
        this.isGameWon = false;
    }

    
    public void setupGame() {
        System.out.print("How many explorers do you want to play with?");
        int n = Integer.parseInt(scanner.nextLine());
        if (n <= 0) n = 3;

        for (int i = 1; i <= n; i++) {
            explorers.add(new Explorer("Explorer " + i));
        }
        
        this.currentPlayerNode = explorers.head;
        this.currentRoomNumber = 1;
        this.isTurnOrderReversed = false;
        this.nextEchoStoneToFind = 1;
        this.nextEchoStoneToUse = 1;
        
        Timeline initialTimeline = new Timeline(nextTimelineId++);
        GameState initialState = createSnapshot(); 
        
        initialTimeline.addLog(new ActionLog("(Start) - Game Start", initialState, 0));
        
        this.allTimelines.add(initialTimeline); 
        this.currentTimeline = initialTimeline; 
        
        System.out.println("The adventure begins with " + n + " explorers...");
    }


    public void runGameLoop() {
        while (!isGameOver && !isGameWon) {
       
            Explorer currentPlayer = currentPlayerNode.data;

            if (!currentPlayer.isAlive) {
                System.out.println(currentPlayer.name + " is dead and skips their turn.");
                nextTurn();
                checkAllDead();
                continue;
            }

            displayCurrentState(currentPlayer);
            getPlayerAction(currentPlayer);
            
            checkAllDead();
            
            if (isGameWon) {
                System.out.println("\n*** CONGRATULATIONS! ***");
                System.out.println("You used the Echo Stones in the correct order from 1 to " + (nextEchoStoneToUse - 1) + "!");
                System.out.println("You broke the time loop and escaped the ruins!");
            }
            if (isGameOver) {
                System.out.println("\n--- GAME OVER ---");
                System.out.println("All explorers are dead. You are lost in the time loop forever.");
            }
        }
        scanner.close();
    }
    

    private void displayCurrentState(Explorer currentPlayer) {
        System.out.println("\n----------------------------------------------------");
        System.out.println("Room: " + currentRoomNumber + " | Turn: " + currentPlayer.name);
        System.out.println("Active Timeline ID: " + currentTimeline.timelineId);
        System.out.println(currentPlayer);
        System.out.print("   Turn Order: "); explorers.printList();
        System.out.println("   Turn Direction: " + (isTurnOrderReversed ? "Reverse <--" : "Forward -->"));
        System.out.println("   Win Condition: Must use stone " + nextEchoStoneToUse + ".");
        System.out.println("----------------------------------------------------");
    }

   
    private void getPlayerAction(Explorer currentPlayer) {
        System.out.println("Choose action (" + currentPlayer.name + "):");
        System.out.println("1. Move to the next room");
        System.out.println("2. Use Echo Stone");
        System.out.println("3. Use Paradox Stone (Create new timeline)");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine();
        
        GameState stateBeforeAction = createSnapshot();

        switch (choice) {
            case "1":
                actionMove(currentPlayer, stateBeforeAction);
                break;
            case "2":
                actionUseEchoStone(currentPlayer, stateBeforeAction);
                break;
            case "3":
                actionUseParadoxStone(currentPlayer, stateBeforeAction);
                break;
            default:
                System.out.println("Invalid choice. You lose your turn.");
                logAction(currentPlayer.name + " hesitated", 0, stateBeforeAction);
                nextTurn();
        }
    }

 
    
    private void actionMove(Explorer player, GameState snapshot) {
        currentRoomNumber++;
        String actionDesc = player.name + " moved to room " + currentRoomNumber + ".";
        System.out.println(actionDesc);
        
        triggerRandomEvent(player); 
        
        logAction(actionDesc, 0, snapshot); 
        nextTurn();
    }

    private void actionUseEchoStone(Explorer player, GameState snapshot) {
        if (player.echoStones.size() == 0) {
            System.out.println(player.name + " has no Echo Stones! Action failed.");
            logAction(player.name + " tried to use an Echo Stone (none available)", 0, snapshot);
            nextTurn();
            return;
        }

        System.out.print("Which stone do you want to use? (e.g., 1): ");
        int stoneValue = Integer.parseInt(scanner.nextLine());
        
       
        if (!player.echoStones.contains(stoneValue)) {
            System.out.println(player.name + " doesn't have that stone.");
            logAction(player.name + " tried to use stone " + stoneValue + " (not owned)", 0, snapshot);
            nextTurn();
            return;
        }
        
        player.useEchoStone(stoneValue);

        if (stoneValue == nextEchoStoneToUse) {
            System.out.println("...and it was the correct stone! The seal weakens!");
            String desc = player.name + " used Echo Stone " + stoneValue + " (Correct)";
            nextEchoStoneToUse++;
            logAction(desc, stoneValue, snapshot);
            
            if (nextEchoStoneToUse > winConditionStoneCount) {
                isGameWon = true;
            }
        } else {
            System.out.println("...but it was the wrong stone. The loop strengthens.");
            String desc = player.name + " used Echo Stone " + stoneValue + " (Wrong)";
            logAction(desc, 0, snapshot);
        }
        nextTurn();
    }

    private void actionUseParadoxStone(Explorer player, GameState snapshot) {
        if (player.paradoxStones <= 0) {
            System.out.println(player.name + " has no Paradox Stones! Action failed.");
            logAction(player.name + " tried to use a Paradox Stone (none available)", 0, snapshot);
            nextTurn();
            return;
        }
        
        player.paradoxStones--;
        System.out.println(player.name + " used a Paradox Stone.");

        displayTimelines();
        
        System.out.print("\nWhich Timeline ID do you want to jump to?: ");
        int targetTimelineId = Integer.parseInt(scanner.nextLine());
        System.out.print("Which Action ID within that timeline?: ");
        int targetActionId = Integer.parseInt(scanner.nextLine());

        Timeline oldTimeline = findTimelineById(targetTimelineId);
        if (oldTimeline == null) {
            System.out.println("Invalid Timeline ID. Action failed.");
            logAction(player.name + " used Paradox Stone but got lost", 0, snapshot);
            nextTurn();
            return;
        }
        
       
        ActionLog jumpActionLog = oldTimeline.actionLogs.get(targetActionId);
        if (jumpActionLog == null) {
            System.out.println("Invalid Action ID. Action failed.");
            logAction(player.name + " used Paradox Stone but got lost", 0, snapshot);
            nextTurn();
            return;
        }

        Timeline newTimeline = oldTimeline.copyAsNewTimeline(nextTimelineId++, targetActionId); 
        
        this.allTimelines.add(newTimeline);
        this.currentTimeline = newTimeline;
        
        System.out.println("\n...TIME IS REWINDING... Returned to the moment: " + jumpActionLog.actionDescription);
        revertToState(jumpActionLog.snapshot);
        
        GameState stateAfterJump = createSnapshot();
        logAction(player.name + " used a Paradox Stone to jump to this moment", 0, stateAfterJump);
    }
    
  
    
    private void nextTurn() {
        if (isTurnOrderReversed) {
            currentPlayerNode = currentPlayerNode.previous;
        } else {
            currentPlayerNode = currentPlayerNode.next;
        }
    }
    
    private void checkAllDead() {
        if (explorers.head == null) {
            isGameOver = true;
            return;
        }
        
       
        DoubleNode<Explorer> current = explorers.head;
        int aliveCount = 0;
        do {
            
            if (current.data.isAlive) {
                aliveCount++;
            }
            current = current.next;
        } while (current != explorers.head);
        
        if (aliveCount == 0) {
            isGameOver = true;
        }
    }
    
    private void triggerRandomEvent(Explorer player) {
        int roll = random.nextInt(11); 
        
        switch(roll) {
            case 0:
                System.out.println("  > Event: You found an Echo Stone! (Stone " + nextEchoStoneToFind + ")");
               
                player.echoStones.add(nextEchoStoneToFind);
                nextEchoStoneToFind++;
                break;
            case 1:
                System.out.println("  > Event: You found a Paradox Stone!");
                player.paradoxStones++;
                break;
            case 2:
                System.out.println("  > Event: Time twisted! Turn order REVERSED!");
                isTurnOrderReversed = !isTurnOrderReversed;
                break;
            case 3:
                System.out.println("  > Event: (Harmful 1) Trap! You stepped on a hidden trap. Lost 1 HP.");
                player.takeDamage(1);
                break;
            case 4:
                System.out.println("  > Event: (Harmful 2) Poisonous vines! They attacked you. Lost 1 HP.");
                player.takeDamage(1);
                break;
            case 5:
                System.out.println("  > Event: (Harmful 3) Collapsing ceiling! A rock hit you. Lost 1 HP.");
                player.takeDamage(1);
                break;
            case 6:
                System.out.println("  > Event: (Harmful 4) Creepy whisper! The voices confuse your mind. Lost 1 HP.");
                player.takeDamage(1);
                break;
            case 7:
                System.out.println("  > Event: (Helpful 1) Magic spring! You regained 1 HP.");
                player.heal(1);
                break;
            case 8:
                System.out.println("  > Event: (Helpful 2) Treasure chest! You gained 10 Treasure Points.");
                player.addTreasure(10);
                break;
            case 9:
                System.out.println("  > Event: (Helpful 3) Rest area! You safely rested and healed 1 HP.");
                player.heal(1);
                break;
            case 10:
                System.out.println("  > Event: (Helpful 4) Ancient relic! You found a valuable gem and gained 5 Treasure Points.");
                player.addTreasure(5);
                break;
        }
    }

 
    
    private GameState createSnapshot() {
        return new GameState(
            currentRoomNumber, 
            isTurnOrderReversed, 
            nextEchoStoneToFind, 
            nextEchoStoneToUse, 
            currentPlayerNode, 
            explorers
        );
    }
    
    private void logAction(String actionDesc, int echoValue, GameState snapshot) {
        ActionLog log = new ActionLog(actionDesc, snapshot, echoValue);
        currentTimeline.addLog(log);
    }
    
    private void revertToState(GameState snapshot) {
        this.currentRoomNumber = snapshot.currentRoomNumber;
        this.isTurnOrderReversed = snapshot.isTurnOrderReversed;
        this.nextEchoStoneToFind = snapshot.nextEchoStoneToFind;
        this.nextEchoStoneToUse = snapshot.nextEchoStoneToUse;
        
    
        this.explorers = snapshot.explorersSnapshot;
        
     
        this.currentPlayerNode = this.explorers.findNode(
            new Explorer(snapshot.currentPlayerName) 
        );
    }
    
    private void displayTimelines() {
        System.out.println("\n====== ALL EXISTING TIMELINES ======");
        for (int i = 0; i < allTimelines.size(); i++) {
            Timeline t = allTimelines.get(i);
            System.out.println("\n--- TIMELINE ID: " + t.timelineId + " ---");
            
            for (int j = 0; j < t.actionLogs.size(); j++) {
                ActionLog log = t.actionLogs.get(j);
                System.out.println("  [Action ID: " + j + "] " + log.actionDescription);
            }
        }
        System.out.println("==========================================");
    }
    
    private Timeline findTimelineById(int targetId) {
        for (int i = 0; i < allTimelines.size(); i++) {
            Timeline t = allTimelines.get(i);
            if (t.timelineId == targetId) {
                return t;
            }
        }
        return null;
    }
}
