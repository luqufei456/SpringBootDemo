package com.fileload.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/uploads")
public class FileUploadController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @GetMapping
    public String index(){
        return "index";
    }

    @PostMapping("/upload1")
    @ResponseBody
    public Map<String, String> upload1(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件名称] - [{}]", file.getOriginalFilename());
        log.info("[文件大小] - [{}]", file.getSize());
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理，此处只是为了演示故将地址写成本地电脑指定目录）
        // 开发时，路径可以是目录中的路径，但是在生产时，是一个jar包在运行，所以我们要获取jar包运行的同级目录
        File upload = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + file.getOriginalFilename());
        // 当生产环境时，jar包中的目录无法被获取，所以 upload不存在
        // 先 new File("").getAbsoluteFile() 获取 jar 包所在目录，然后拼接即可
        if (!upload.exists()) {
            upload = new File(new File("").getAbsoluteFile() + File.separator + file.getOriginalFilename());
        }
        file.transferTo(upload);
        Map<String, String> result = new HashMap<>();
        result.put("contentType", file.getContentType());
        result.put("fileName", file.getOriginalFilename());
        result.put("fileSize", file.getSize() + "");
        return result;
    }

    @PostMapping("/upload2")
    @ResponseBody
    public List<Map<String, String>> upload2(@RequestParam("file") MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return null;
        }
        List<Map<String, String>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            // TODO Spring Mvc 提供的写入方式
            file.transferTo(new File("H:\\GitTest\\GitHubGit\\Spring Boot\\" + file.getOriginalFilename()));
            Map<String, String> map = new HashMap<>(16);
            map.put("contentType", file.getContentType());
            map.put("fileName", file.getOriginalFilename());
            map.put("fileSize", file.getSize() + "");
            results.add(map);
        }
        return results;
    }

    @PostMapping("/upload3")
    @ResponseBody
    public void upload2(String base64) throws IOException {
        // TODO BASE64 方式的 格式和名字需要自己控制（如 png 图片编码后前缀就会是 data:image/png;base64,）
        final File tempFile = new File("H:\\GitTest\\GitHubGit\\Spring Boot\\test.jpg");
        // TODO 防止有的传了 data:image/png;base64, 有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);

    }
}
