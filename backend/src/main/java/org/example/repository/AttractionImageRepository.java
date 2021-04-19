package org.example.repository;

import org.example.entity.AttractionImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionImageRepository extends JpaRepository<AttractionImageEntity, Integer> {
}
