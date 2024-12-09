package com.example.pr7;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudRepository extends JpaRepository<Crud, Integer> {
    Crud save(Crud crud);
}
