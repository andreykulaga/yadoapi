package com.example.yadoapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SystemItemImportRequest {

    List<SystemItemImport> items;
    String updateDate;
}
