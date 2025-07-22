package com.example.ss8.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    @Autowired
    private final Cloudinary cloudinary;

    /**
     * Upload ảnh lên Cloudinary
     * @param file MultipartFile cần upload
     * @return URL của ảnh đã upload
     * @throws IOException nếu có lỗi khi upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Kiểm tra định dạng file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File không phải là ảnh");
        }

        // Upload lên Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "dishes", // Thư mục lưu trữ
                        "resource_type", "image",
                        "transformation", ObjectUtils.asMap(
                                "width", 800,
                                "height", 600,
                                "crop", "fill",
                                "quality", "auto"
                        )
                )
        );

        return uploadResult.get("secure_url").toString();
    }

    /**
     * Xóa ảnh trên Cloudinary
     * @param publicId public ID của ảnh cần xóa
     * @throws IOException nếu có lỗi khi xóa
     */
    public void deleteImage(String publicId) throws IOException {
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    /**
     * Lấy public ID từ URL Cloudinary
     * @param url URL của ảnh
     * @return public ID
     */
    public String extractPublicId(String url) {
        if (url == null || !url.contains("cloudinary")) {
            return null;
        }

        // Extract public ID từ URL
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1];
        return filename.substring(0, filename.lastIndexOf("."));
    }
}