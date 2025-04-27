package springkpsgroup02.kps.Model.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
