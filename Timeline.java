/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author MURAT KARATAŞ
 */
class Timeline {
    final int timelineId;
    MyLinkedList<ActionLog> actionLogs; 

    public Timeline(int id) {
        this.timelineId = id;
        this.actionLogs = new MyLinkedList<>(); 
    }

    public void addLog(ActionLog log) {
        this.actionLogs.add(log);
    }

    public Timeline copyAsNewTimeline(int newId, int actionIndex) {
        Timeline newTimeline = new Timeline(newId);

        newTimeline.actionLogs = this.actionLogs.copyUpTo(actionIndex);
        
        return newTimeline;
    }
}