package com.spring.jpadata.dto.lamda;

import java.security.cert.CollectionCertStoreParameters;
import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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

            List<Student> totalList = Arrays.asList(
                    new Student("홍길동", 10, Student.Sex.MALE),
                    new Student("홍길순", 12, Student.Sex.FEMALE),
                    new Student("김남", 10, Student.Sex.MALE),
                    new Student("김여", 8, Student.Sex.FEMALE),
                    new Student("김여", 8, Student.Sex.FEMALE)
            );

        List<Student> maleList = totalList.stream()
                    .filter(s -> s.getSex() == Student.Sex.MALE)
                    .collect(Collectors.toList());

        maleList.forEach(s-> System.out.println(s.getName()));

        System.out.println("---------------위(남자)/아래(여자)---------------");

        Set<Student> femaleSet = totalList.stream()
                    .filter(s -> s.getSex() == Student.Sex.FEMALE)
                    .collect(Collectors.toSet());
            femaleSet.forEach(s -> System.out.println(s.getName()));
        }
    }





