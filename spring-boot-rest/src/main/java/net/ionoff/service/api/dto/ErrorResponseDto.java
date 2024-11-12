package net.ionoff.service.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
  private Long timestamp = null;
  private String path = null;
  private Integer status = null;
  private String type = null;
  private String cause = null;
  private String message = null;
  private String traceId = null;
  private List<ErrorMessageDto> errors = null;

  public Integer getCode() {
    return status;
  }

  public String getError() {
    return message;
  }

  public String getIdentifier() {
    return traceId;
  }
}

