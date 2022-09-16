package com.example.yadoapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

import static com.example.yadoapi.models.SystemItemType.FILE;

@Getter
@Setter
@NoArgsConstructor
public class Folder {

    private String id;
    private final String type = "FOLDER";
    private String date;
    private String url;
    private String parentId;
    private long size;
    private  Set<Object> children;


    public Folder (SystemItemImport systemItemImport) {
        this.id = systemItemImport.getId();
        this.date = systemItemImport.getDate();
        this.url = systemItemImport.getUrl();
        this.parentId = systemItemImport.getParentId();
        this.size = systemItemImport.getSize();
    }

}
