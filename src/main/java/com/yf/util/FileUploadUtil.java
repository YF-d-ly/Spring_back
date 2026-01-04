package com.yf.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {
    // 文件在Web服务器上的访问前缀
    private static final String WEB_ACCESS_PREFIX = "/images/";

    public static String uploadFile(MultipartFile file, String uploadSubDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        // 1. 上传到 src/main/resources/static/ 目录
        String staticResourceBase = "src/main/resources/static/";
        Path uploadPath = Paths.get(staticResourceBase, uploadSubDir).toAbsolutePath().normalize();
        
        // 创建目标目录
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("创建上传目录: " + uploadPath);
        }

        // 检查文件是否已存在（虽然UUID几乎不可能重复，但这是良好实践）
        Path filePath = uploadPath.resolve(fileName);
        if (Files.exists(filePath)) {
            throw new IOException("文件已存在: " + fileName);
        }

        // 保存文件到 src/main/resources/static/ 目录
        file.transferTo(filePath.toFile());
        System.out.println("文件已保存至: " + filePath);

        // 2. 同步到 target/classes/static/ 目录
        String staticBasePath = System.getProperty("user.dir") + "/target/classes/static/";
        Path uploadTargetPath = Paths.get(staticBasePath, uploadSubDir).toAbsolutePath().normalize();

        // 创建目标目录
        if (!Files.exists(uploadTargetPath)) {
            Files.createDirectories(uploadTargetPath);
            System.out.println("创建目标目录: " + uploadTargetPath);
        }

        // 检查文件是否已存在（虽然UUID几乎不可能重复，但这是良好实践）
        Path targetPath = uploadTargetPath.resolve(fileName);
        if (Files.exists(targetPath)) {
            throw new IOException("文件已存在: " + fileName);
        }

        // 复制文件到 target/classes/static/ 目录
        Files.copy(filePath, targetPath);
        System.out.println("文件已同步至: " + targetPath);

        // 返回Web访问路径，而非物理路径或纯文件名
        return WEB_ACCESS_PREFIX + fileName;
    }
}