package com.example.lab4;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudRepository extends JpaRepository<Crud, Integer> {

    Crud save(Crud crud);
}
