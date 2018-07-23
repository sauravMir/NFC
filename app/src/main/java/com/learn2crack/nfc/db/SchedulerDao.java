package com.learn2crack.nfc.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SCHEDULER".
*/
public class SchedulerDao extends AbstractDao<Scheduler, Long> {

    public static final String TABLENAME = "SCHEDULER";

    /**
     * Properties of entity Scheduler.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nfc_id = new Property(1, String.class, "nfc_id", false, "NFC_ID");
        public final static Property Phone_number = new Property(2, String.class, "phone_number", false, "PHONE_NUMBER");
        public final static Property Progress = new Property(3, Integer.class, "progress", false, "PROGRESS");
        public final static Property Type = new Property(4, Integer.class, "type", false, "TYPE");
        public final static Property CreatedAt = new Property(5, java.util.Date.class, "createdAt", false, "CREATED_AT");
    }


    public SchedulerDao(DaoConfig config) {
        super(config);
    }
    
    public SchedulerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SCHEDULER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NFC_ID\" TEXT NOT NULL ," + // 1: nfc_id
                "\"PHONE_NUMBER\" TEXT," + // 2: phone_number
                "\"PROGRESS\" INTEGER," + // 3: progress
                "\"TYPE\" INTEGER," + // 4: type
                "\"CREATED_AT\" INTEGER NOT NULL );"); // 5: createdAt
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SCHEDULER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Scheduler entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNfc_id());
 
        String phone_number = entity.getPhone_number();
        if (phone_number != null) {
            stmt.bindString(3, phone_number);
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(4, progress);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, type);
        }
        stmt.bindLong(6, entity.getCreatedAt().getTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Scheduler entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNfc_id());
 
        String phone_number = entity.getPhone_number();
        if (phone_number != null) {
            stmt.bindString(3, phone_number);
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(4, progress);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, type);
        }
        stmt.bindLong(6, entity.getCreatedAt().getTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Scheduler readEntity(Cursor cursor, int offset) {
        Scheduler entity = new Scheduler( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // nfc_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone_number
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // progress
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // type
            new java.util.Date(cursor.getLong(offset + 5)) // createdAt
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Scheduler entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNfc_id(cursor.getString(offset + 1));
        entity.setPhone_number(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProgress(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setCreatedAt(new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Scheduler entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Scheduler entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Scheduler entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
