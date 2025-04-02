package org.chudinova.sofia.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "statuses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    //@JsonBackReference
    private List<Task> tasks = new ArrayList<>();

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name=" + name + '\'' +
                ", code=" + code + '\'' +
                '}';
    }
}
