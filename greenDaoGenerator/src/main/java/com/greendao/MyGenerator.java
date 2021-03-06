package com.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.learn2crack.nfc.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addUserEntities(schema);
        addSyncEntities(schema);
        // addPhonesEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addUserEntities(final Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("nfc_id").notNull();
        user.addStringProperty("phone_number");
        user.addIntProperty("ride_left");

        return user;
    }

    // This is use to describe the colums of your table
    private static Entity addSyncEntities(final Schema schema) {
        Entity Scheduler = schema.addEntity("Scheduler");
        Scheduler.addIdProperty().primaryKey().autoincrement();
        Scheduler.addStringProperty("nfc_id").notNull();
        Scheduler.addStringProperty("phone_number");
        //0=in que, 1=progeressing, 2=done
        Scheduler.addIntProperty("progress");
        Scheduler.addIntProperty("type");
        //Scheduler.addDateProperty("createdAt").notNull();

        return Scheduler;
    }


}
