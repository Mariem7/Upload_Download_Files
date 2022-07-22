package com.example.uploaddownloadfiles.controllers;

import com.example.uploaddownloadfiles.entities.Document;
import com.example.uploaddownloadfiles.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class AppController {

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/")
    public String viewHomePage(Model model){
        List<Document> listDocs = documentRepository.findAll();
        model.addAttribute("listDocs",listDocs);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document")MultipartFile multipartFile, RedirectAttributes ra) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document = new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUploadTime(new Date());

        documentRepository.save(document);
        ra.addFlashAttribute("message","The file has been uploaded");
        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
        Optional<Document> result = documentRepository.findById(id);
        if(result.isEmpty()){
            throw new Exception("could no find document");
        }else{
            Document document = result.get();
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Dispoition";
            String headerValue = "attachment; filename=" +document.getName();
            response.setHeader(headerKey,headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(document.getContent());
            outputStream.close();
        }
    }

}
