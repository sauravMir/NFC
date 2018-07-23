package com.learn2crack.nfc.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "SCHEDULER".
 */
@Entity
public class Scheduler {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String nfc_id;
    private String phone_number;
    private Integer progress;
    private Integer type;

    @NotNull
    private java.util.Date createdAt;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Scheduler() {
    }

    public Scheduler(Long id) {
        this.id = id;
    }

    @Generated
    public Scheduler(Long id, String nfc_id, String phone_number, Integer progress, Integer type, java.util.Date createdAt) {
        this.id = id;
        this.nfc_id = nfc_id;
        this.phone_number = phone_number;
        this.progress = progress;
        this.type = type;
        this.createdAt = createdAt;
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @NotNull
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreatedAt(@NotNull java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}