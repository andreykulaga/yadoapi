package com.example.yadoapi.models;


import lombok.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class SystemItemImport {


    @Id
    @Getter @Setter
    private String id;
    @Getter
    private SystemItemType type;

    @Getter @Setter
    private String date;

    @Getter @Setter
    private String url;
    @Getter @Setter
    private String parentId;
    @Getter @Setter
    private long size;

    @Getter @Setter
    @OneToMany(mappedBy="parentId")
    private Set<SystemItemImport> children;

}
