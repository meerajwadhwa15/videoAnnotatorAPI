package com.videoannotator.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<SubCategory> subCategoryList;
}
