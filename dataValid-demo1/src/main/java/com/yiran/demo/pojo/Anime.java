package com.yiran.demo.pojo;

import com.yiran.demo.groups.Groups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
* 动漫
* */
public class Anime {

    @NotNull(message = "id 不能为空", groups = Groups.Update.class)
    private Integer id;

    @NotBlank(message = "name 不允许为空", groups = Groups.Default.class)
    private String name;

    @NotNull(message = "price 不允许为空", groups = Groups.Default.class)
    private Integer length;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
