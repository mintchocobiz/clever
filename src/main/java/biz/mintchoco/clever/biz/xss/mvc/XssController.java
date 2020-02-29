package biz.mintchoco.clever.biz.xss.mvc;

import biz.mintchoco.clever.biz.xss.entity.XssRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/xss")
public class XssController {

    private String BASE_REPOSITORY = "/tmp/dev/upload/filter";

    @PostConstruct
    public void makeRepository() {
        File f = new File(BASE_REPOSITORY);
        if(!f.exists()) {
            log.debug("create repository.. " + f.getAbsolutePath());
            f.mkdirs();
        }
    }

    @PostMapping("json")
    public ResponseEntity<String> json(@RequestBody XssRequestParam param) {
        return new ResponseEntity<>(param.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> multipart(@RequestPart("file") MultipartFile file) {
        saveFile(file);
        return new ResponseEntity<>(file.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> multipart(@RequestPart("json") XssRequestParam json,
                                            @RequestPart("file") MultipartFile file) {
        saveFile(file);

        Map<String, String> item = new HashMap<>();
        item.put("json", json.toString());
        item.put("file", file.toString());
        return new ResponseEntity<>(item.toString(), HttpStatus.OK);
    }

    @PostMapping("param")
    public ResponseEntity<String> param(@RequestParam String aa) {
        return new ResponseEntity<>(aa, HttpStatus.OK);
    }

    @PostMapping("skip1")
    public ResponseEntity<String> skip(@RequestBody XssRequestParam param) {
        return new ResponseEntity<>(param.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "multipart-array", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> files(@RequestPart("images") List<MultipartFile> images) {
        for(MultipartFile file : images) {
            saveFile(file);
        }
        return new ResponseEntity<>(images.toString(), HttpStatus.OK);
    }

    private void saveFile(MultipartFile file) {
        try {
            File f = new File(createDirectory(), file.getOriginalFilename());
            Files.copy(file.getInputStream(), Paths.get(f.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            log.debug("file write error", e);
        }
    }

    private File createDirectory() {
        File f = new File(BASE_REPOSITORY, UUID.randomUUID().toString());
        f.mkdirs();
        return f;
    }
}
