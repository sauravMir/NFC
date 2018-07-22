package com.learn2crack.nfc.db;

import org.greenrobot.greendao.annotation.*;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "USER".
 */
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String nfc_id;
    private String phone_number;
    private Integer ride_left;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    @Generated
    public User(Long id, String nfc_id, String phone_number, Integer ride_left) {
        this.id = id;
        this.nfc_id = nfc_id;
        this.phone_number = phone_number;
        this.ride_left = ride_left;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getNfc_id() {
        return nfc_id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNfc_id(@NotNull String nfc_id) {
        this.nfc_id = nfc_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getRide_left() {
        return ride_left;
    }

    public void setRide_left(Integer ride_left) {
        this.ride_left = ride_left;
    }

    // KEEP METHODS - put your custom methods here

    public void updateItem(User u, DaoSession daoSession){
        UserDao groceryDao = daoSession.getUserDao();

        groceryDao.update(u);
    }

    //called from register activity
    public void insertItem(String nfc_id ,String phone, int ride_left, DaoSession daoSession){
        UserDao groceryDao = daoSession.getUserDao();
        User grocery = new User();
        grocery.setNfc_id(nfc_id);
        grocery.setPhone_number(phone);
        grocery.setRide_left(ride_left);
        groceryDao.insert(grocery);
    }

    public User isValidNFC(String nfcId, DaoSession sessionDao){
        UserDao ud=sessionDao.getUserDao();
        List<User> items = ud.queryRaw("where nfc_id=?",nfcId);
        if(items!=null && items.size()>0)
            return items.get(0);

        return null;

    }

    // KEEP METHODS END

}
