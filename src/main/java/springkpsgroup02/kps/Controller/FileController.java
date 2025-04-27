package springkpsgroup02.kps.Controller;

import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springkpsgroup02.kps.Model.DTO.Response.BaseResponse;
import springkpsgroup02.kps.Model.DTO.Response.FileDto;
import springkpsgroup02.kps.Service.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController extends BaseResponse {

    private final FileService fileService;
    private final String bucketName = "preview-file";

    @Operation(
            summary = "upload a file",
            description = "Uploads a file and returns metadata about the uploaded file."
    )
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String fileName = fileService.uploadFile(file);

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/" + bucketName + "/" + fileName)
                .toUriString();

        FileDto fileDto = FileDto.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .build();

        return responseEntity(true,"File uploaded successfully! Metadata of the uploaded file is returned.", HttpStatus.CREATED, fileDto);
    }

    @Operation(
            summary = "Preview a file",
            description = "Fetches a file by its name and returns the file as a byte array (usually for image previews)."
    )
    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<?> viewFileByFileName(@PathVariable("file-name")String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        Resource resource = fileService.viewFileByFileName(fileName);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if(fileName.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        }
        else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        }
        else if(fileName.endsWith(".svg")) {
            mediaType = MediaType.valueOf("image/svg+xml");
        }
        else if(fileName.endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(mediaType)
                .body(resource);
    }

}
