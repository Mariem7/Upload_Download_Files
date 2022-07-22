package com.example.uploaddownloadfiles;

import com.example.uploaddownloadfiles.entities.Document;
import com.example.uploaddownloadfiles.repositories.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UploadDownloadFilesApplicationTests {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Rollback(false)
    void testInsertDocument() throws IOException {
        File file = new File("/home/lenovo/Téléchargements/TP3_VF.pdf");
        Document document = new Document();
        document.setName(file.getName());
        byte[] bytes = Files.readAllBytes(file.toPath());
        document.setContent(bytes);
        long fileSize = bytes.length;
        document.setSize(fileSize);
        document.setUploadTime(new Date());
        Document savedDoc = documentRepository.save(document);
        Document existDoc = entityManager.find(Document.class,savedDoc.getId());

        assertThat(existDoc.getSize()).isEqualTo(fileSize);

    }



}
