package com.example.yadoapi;

import com.example.yadoapi.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SpringConfig {

    ItemRepository itemRepository;

}
