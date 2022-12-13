package com.spring.jpadata.dto.lamda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter @AllArgsConstructor @Data
public class Student implements Comparable<Student>{

    private String name;
    private Integer score;
    private Sex sex;

    @Override
    public int compareTo(Student o) {
        return Integer.compare(score, o.score);
    }

    public enum Sex{
        MALE,FEMALE
    }

}
