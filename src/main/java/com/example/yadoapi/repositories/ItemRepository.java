package com.example.yadoapi.repositories;

import com.example.yadoapi.models.SystemItemImport;
import net.minidev.json.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<SystemItemImport, String> {

//    void deleteFolder(SystemItemImport entity);
//    JSONArray getInfoForFOlder(SystemItemImport entity);
}
