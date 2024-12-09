package com.example.pr7;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table(name = "crud")
@AllArgsConstructor
@NoArgsConstructor
public class Crud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String value;
}