package com.hcmute.tdshop.model;

import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {

  private String error;
  private String message;
  private Object data;
  private int status = 0;

  public DataResponse(Object data) {
    this.message = ApplicationConstants.SUCCESSFUL;
    this.error = ApplicationConstants.SUCCESSFUL;
    this.data = data;
    this.status = ApplicationConstants.SUCCESSFUL_CODE;
  }

  public DataResponse(String message, Object data) {
    this.message = message;
    this.error = ApplicationConstants.SUCCESSFUL;
    this.data = data;
    this.status = ApplicationConstants.SUCCESSFUL_CODE;
  }

  public DataResponse(String error, String message) {
    this.error = error;
    this.message = message;
  }

  public DataResponse(String error, String message, int status) {
    this.error = error;
    this.message = message;
    this.status = status;
  }
  public static final DataResponse SUCCESSFUL = new DataResponse(ApplicationConstants.SUCCESSFUL, "SUCCESSFUL", ApplicationConstants.SUCCESSFUL_CODE);
  public static final DataResponse NOT_FOUND = new DataResponse(ApplicationConstants.NOT_FOUND, "NOT_FOUND", ApplicationConstants.NOT_FOUND_CODE);
  public static final DataResponse BAD_REQUEST = new DataResponse(ApplicationConstants.BAD_REQUEST,
      ApplicationConstants.BAD_REQUEST_MESSAGE, null, ApplicationConstants.BAD_REQUEST_CODE);
  public static final DataResponse FORBIDDEN = new DataResponse(ApplicationConstants.FORBIDDEN, "FORBIDDEN", ApplicationConstants.FORBIDDEN_CODE);
  public static final DataResponse FAILED = new DataResponse(ApplicationConstants.FAILED, "FAILED");
}
