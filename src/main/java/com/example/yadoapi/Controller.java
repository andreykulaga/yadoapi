package com.example.yadoapi;


import com.example.yadoapi.models.*;
import com.example.yadoapi.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.example.yadoapi.models.SystemItemType.FILE;

@RestController
@AllArgsConstructor
public class Controller {

    ItemRepository itemRepository;

    @PostMapping
            (value = "/imports",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> importAll(@RequestBody SystemItemImportRequest systemItemImportRequest){
        List<SystemItemImport> list = systemItemImportRequest.getItems();
        String updateDate = systemItemImportRequest.getUpdateDate();

        // проверка, что дата в нужном формате
        Instant.parse(updateDate);

        HashMap<String, SystemItemImport> hashMap = new HashMap<>();
        for (SystemItemImport sii: list) {
            hashMap.put(sii.getId(), sii);
            System.out.println(sii.getId() + " added to hashmap");
        }

        //дополняем hashmap родителями, которых не было в импорте
        HashMap<String, SystemItemImport> hashMap1 = new HashMap<>();
        for (String id: hashMap.keySet()) {
            String parentId = hashMap.get(id).getParentId();
            System.out.println("нашли родителя для " + id);
            if (parentId != null &&  !hashMap.containsKey(parentId)) {
                while (parentId != null) {
                    SystemItemImport parent = itemRepository.getReferenceById(parentId);
                    hashMap1.put(parentId, parent);
                    parentId = parent.getParentId();
                }
            }
        }
        hashMap.putAll(hashMap1);

        //ставим всем новое время обновления
        for (String id: hashMap.keySet()) {
            hashMap.get(id).setDate(updateDate);
        }

        //обновляем у всех размер:
        //если элемент файл, то увеличиваем размер всех его родителей
        for (String id: hashMap.keySet()) {
            SystemItemType type = hashMap.get(id).getType();
            if (type.equals(FILE)) {
                String parentId = hashMap.get(id).getParentId();
                while (parentId != null ) {
                    long newSize = hashMap.get(parentId).getSize() + hashMap.get(id).getSize();
                    hashMap.get(parentId).setSize(newSize);
                    parentId = hashMap.get(parentId).getParentId();
                }
            }
        }

        //сохраняем данные в базу
        itemRepository.saveAllAndFlush(hashMap.values());
        return ResponseEntity.status(HttpStatus.OK).body("{\"code\": 200,\n" +
                "  \"message\": \"Success\"}");
    }


    @DeleteMapping
            (value = "/delete/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id, @RequestParam String date) {
        Instant.parse(date);
        SystemItemImport sii = itemRepository.getReferenceById(id);
        //составляем сет на удаление
        Set<String> idToDelete = markToDelete(id);
        //изменяем размер и дату изменения всех родителей
        if (sii.getParentId() != null) {
            SystemItemImport parent = itemRepository.getReferenceById(sii.getParentId());
            long newSize = parent.getSize() - sii.getSize();
            parent.setSize(newSize);
            parent.setDate(date);
            itemRepository.saveAndFlush(parent);
        }
        idToDelete.forEach(s -> itemRepository.deleteById(s));

    }

    //метод по id составляет сет всех детей для их удаления
    Set<String> markToDelete(String string){
        Set<String> result = new HashSet<>();
        result.add(string);

        Set<SystemItemImport> systemItemImports = itemRepository.getReferenceById(string).getChildren();
        systemItemImports.forEach(s -> {
            SystemItemType type = s.getType();
            if (type.equals(FILE)) {
                result.add(s.getId());
            } else {
                result.addAll(markToDelete(s.getId()));
            }
        });
        return result;
    }


    @GetMapping
            (value = "/nodes/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public FileOrFolder getInfo(@PathVariable String id) {
        SystemItemImport sii = itemRepository.getReferenceById(id);
        if (sii.getType().equals(FILE)) {
            return new File(sii);
        } else {
            Folder folder = new Folder(sii);
            collectAllChildren(folder);
            return folder;
        }
    }


    //наполняет сет обьекта папка детьми
    void collectAllChildren(Folder folder) {
        SystemItemImport systemItemImport = itemRepository.getReferenceById(folder.getId());
        Set<SystemItemImport> set = systemItemImport.getChildren();
        for (SystemItemImport s: set) {
            System.out.println(s.getId());
        }
        Set<FileOrFolder> children = new HashSet<>();
        for (SystemItemImport s: set) {
            if (s.getType().equals(FILE)) {
                File file = new File(s);
                children.add(file);
            } else {
                Folder nestedFolder = new Folder(s);
                collectAllChildren(nestedFolder);
                children.add(nestedFolder);
            }
        }
        folder.setChildren(children);
    }
}
