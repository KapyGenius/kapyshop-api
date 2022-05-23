package com.example.kapyedu.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "name", length = 255, nullable = false)
    @NotNull
    @Size(max = 255)
    private String name;

    //@Column(name = "preis", nullable = false, columnDefinition = "int default 0")
    //@NotNull
    //@Size(max = 255)
    //private Integer preis;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "int default 0")
    protected Integer version;

    @Version
    @Column(name = "versiontest", nullable = false, columnDefinition = "int default 0")
    protected Integer versiontest;
}
