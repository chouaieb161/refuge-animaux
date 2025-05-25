package com.refuge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refuge.model.Refuge;

import java.util.List;

@Repository
public interface RefugeRepository extends JpaRepository<Refuge, Long> {

    List<Refuge> findByNomContainingIgnoreCase(String nom);
}
