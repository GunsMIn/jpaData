package com.spring.jpadata.dto.lamda;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class Dish {

    private  String name;

    private boolean vegetarian;

    private int calories;

    private Type type;

    @Builder
    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public enum Type{ MEAT, FISH , OTHER}
}
