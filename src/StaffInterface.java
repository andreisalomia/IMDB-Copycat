//package org.example;
public interface StaffInterface {
    void addProductionSystem(String name);
    void addActorSystem(String name);
    void removeProductionSystem(Production p);
    void removeActorSystem(Actor a);
    void updateProduction(Staff user, String title);
    void updateActor(Staff user, String title);
}
