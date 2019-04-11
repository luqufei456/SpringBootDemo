package com.yiran.demo.pojo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.math.BigDecimal;


public class Book {

    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.yiran.demo.pojo.Book");
        Method method = clazz.getMethod("setAnime", Class.forName("com.yiran.demo.pojo.Anime"));
        Anime anime = new Anime();
        anime.setId(1);
        anime.setLength(4);
        anime.setName("约会大作战");
        Gson gson = new Gson();
        String json = gson.toJson(anime);
        System.out.println(json);
        Object obj = clazz.newInstance();
        method.invoke(obj, gson.fromJson(json, Class.forName("com.yiran.demo.pojo.Anime")));

        Method method2 = clazz.getMethod("getAnime", null);
        String rsJson = gson.toJson(method2.invoke(obj, null));
        System.out.println(rsJson);
    }

    private Anime anime;

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    private Integer id;
    @NotBlank(message = "name 不允许为空")
    // 类似于Size
    @Length(min = 2, max = 10, message = "name 长度必须在 {min} - {max} 之间")
    private String name;
    @NotNull(message = "price 不允许为空")
    @DecimalMin(value = "0.1", message = "价格不能低于 {value}")
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
