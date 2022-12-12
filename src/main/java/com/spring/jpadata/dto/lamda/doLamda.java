package com.spring.jpadata.dto.lamda;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class doLamda {

    public void filterDish(String... s) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french", true, 530, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        Optional<Integer> sumOfCalories = menu.stream()
                .map(Dish::getCalories)
                .reduce((a, b) -> a + b);

        System.out.println("sumOfCalories = " + sumOfCalories);


       /* List<String> threeHighCalDish = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(toList());

        System.out.println(threeHighCalDish);*/
    }


    public static void main(String[] args) {


            List<Dish> menu = Arrays.asList(
                    new Dish("pork", false, 800, Dish.Type.MEAT),
                    new Dish("beef", false, 700, Dish.Type.MEAT),
                    new Dish("chicken", false, 400, Dish.Type.MEAT),
                    new Dish("french", true, 530, Dish.Type.OTHER),
                    new Dish("pizza", true, 550, Dish.Type.OTHER),
                    new Dish("salmon", false, 450, Dish.Type.FISH)
            );

        long count = menu.stream().count();
        System.out.println("count = " + count);


    }
}
