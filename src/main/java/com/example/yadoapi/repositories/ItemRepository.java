package com.example.yadoapi.repositories;

import com.example.yadoapi.models.SystemItemImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<SystemItemImport, String> {

}
