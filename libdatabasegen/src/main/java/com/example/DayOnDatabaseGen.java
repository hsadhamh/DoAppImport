package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DayOnDatabaseGen {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "factor.labs.indiancalendar.database");
        schema.enableKeepSectionsByDefault();

        addEvents(schema);

        try {
            /* Use forward slashes if you're on macOS or Unix, i.e. "/app/src/main/java"  */
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Entity addEvents(final Schema schema) {
        Entity events = schema.addEntity("Events");
        events.addIdProperty().primaryKey().autoincrement();
        events.addStringProperty("GUID");
        events.addStringProperty("name");
        events.addStringProperty("property");
        events.addLongProperty("category");
        events.addLongProperty("sub_category");
        events.addStringProperty("locations");
        events.addLongProperty("flags");
        events.addStringProperty("tags");

        return events;
    }

}
