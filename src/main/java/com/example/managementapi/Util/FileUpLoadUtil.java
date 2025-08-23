package com.example.managementapi.Util;

import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class FileUpLoadUtil {

    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;


    //? Check định dạng file img
    public static final String IMAGE_PATTERN = "(?i)^(jpg|jpeg|png|gif|bmp|webp)$";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(final String extension, final String pattern) {
        if (extension == null) {
            log.error("Extension is null");
            return false;
        }
        log.info("Checking extension: '{}'", extension);
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(extension.trim());
        boolean matches = matcher.matches();
        log.info("Extension '{}' matches pattern '{}': {}", extension, pattern, matches);
        return matches;
    }

    public static void assertAllowed(MultipartFile file, String pattern){
        log.info("Validating file: {}", file.getOriginalFilename());
        final long size = file.getSize();

        if (size > MAX_FILE_SIZE) {
            log.error("File size exceeds limit: {}", size);
            throw new AppException(ErrorCode.IMG_OVER_SIZE);
        }

        final String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            log.error("File name is null or empty");
            throw new RuntimeException("File name cannot be null or empty");
        }

        final String extension = FilenameUtils.getExtension(fileName);
        log.info("File extension: {}", extension);
        if (!isAllowedExtension(extension, pattern)) {
            log.error("Invalid file extension: {}", extension);
            throw new RuntimeException("Only jpg, png, gif, bmp, webp files are allowed");
        }

    }

    public static String getFileName(final String name) {

        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        final String date = dateFormat.format(System.currentTimeMillis());
        String cleanName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
        return String.format(FILE_NAME_FORMAT, name, date);

    }
}
