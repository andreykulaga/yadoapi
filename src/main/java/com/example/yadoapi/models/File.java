package com.example.yadoapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class File extends FileOrFolder{
    private String id;
    private final String type = "FILE";
    private String date;
    private String url;
    private String parentId;
    private long size;
    String children;

    public File(SystemItemImport systemItemImport) {
        this.id = systemItemImport.getId();
        this.date = systemItemImport.getDate();
        this.url = systemItemImport.getUrl();
        this.parentId = systemItemImport.getParentId();
        this.size = systemItemImport.getSize();
    }
}
