//package org.example;
public interface StaffInterface {
    void addProductionSystem(String name);
    void addActorSystem(String name);
    void removeProductionSystem(Production p);
    void removeActorSystem(Actor a);
    void updateProduction(Production p);
    void updateActor(Actor a);
}
