/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author MURAT KARATAŞ
 */
class ActionLog {
    final String actionDescription;
    final GameState snapshot;
    final int echoStoneUsedThisTurn;

    public ActionLog(String desc, GameState snap, int echoValue) {
        this.actionDescription = desc;
        this.snapshot = snap;
        this.echoStoneUsedThisTurn = echoValue;
    }
}
