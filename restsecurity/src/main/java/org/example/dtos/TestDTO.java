package org.example.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestDTO implements DTO<Integer> {
    Integer id;
    String name;

    public TestDTO(String name) {
        this.name = name;
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
