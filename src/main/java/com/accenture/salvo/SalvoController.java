package com.accenture.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository repo;

    @RequestMapping("/games")
    public List<Object> getAll() {
        List<Object> games=repo.findAll().stream()
                .map(game -> game.gameDto())
                .collect(Collectors.toList());
        return games;
    }


}
