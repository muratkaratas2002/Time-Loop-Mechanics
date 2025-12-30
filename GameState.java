/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

class GameState {
    int currentRoomNumber;
    boolean isTurnOrderReversed;
    int nextEchoStoneToFind;
    int nextEchoStoneToUse;
    String currentPlayerName;
    
   
    PlayerTurnList<Explorer> explorersSnapshot; 

    public GameState(int room, boolean reversed, int find, int use, 
                       
                         DoubleNode<Explorer> currentPlayerNode,
                         PlayerTurnList<Explorer> originalExplorers)
    {
        this.currentRoomNumber = room;
        this.isTurnOrderReversed = reversed;
        this.nextEchoStoneToFind = find;
        this.nextEchoStoneToUse = use;
        this.currentPlayerName = currentPlayerNode.data.name; 
        
    
        this.explorersSnapshot = new PlayerTurnList<>(); 
        if (originalExplorers.head != null) {
            
            DoubleNode<Explorer> current = originalExplorers.head; 
            do {
              
                this.explorersSnapshot.add(new Explorer(current.data));
                current = current.next;
            } while (current != originalExplorers.head);
        }
    }
}