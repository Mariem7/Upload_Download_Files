package com.example.uploaddownloadfiles.repositories;

import com.example.uploaddownloadfiles.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query("select new Document(d.id, d.name, d.size) from Document d ORDER BY d.uploadTime DESC")
    List<Document> findAll();
}
