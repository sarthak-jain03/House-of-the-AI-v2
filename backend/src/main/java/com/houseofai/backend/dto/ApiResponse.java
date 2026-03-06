package com.houseofai.backend.dto;

import java.util.List;

public class ApiResponse {

    private Boolean success;
    private String message;
    private String error;
    private String tempId;
    private UserResponse user;
    private List<?> history;
    private Long count;

    private ApiResponse() {}

    public Boolean getSuccess() { return success; }
    public String getMessage() { return message; }
    public String getError() { return error; }
    public String getTempId() { return tempId; }
    public UserResponse getUser() { return user; }
    public List<?> getHistory() { return history; }
    public Long getCount() { return count; }

    public static ApiResponseBuilder builder() {
        return new ApiResponseBuilder();
    }

    public static class ApiResponseBuilder {
        private final ApiResponse response = new ApiResponse();

        public ApiResponseBuilder success(Boolean success) { response.success = success; return this; }
        public ApiResponseBuilder message(String message) { response.message = message; return this; }
        public ApiResponseBuilder error(String error) { response.error = error; return this; }
        public ApiResponseBuilder tempId(String tempId) { response.tempId = tempId; return this; }
        public ApiResponseBuilder user(UserResponse user) { response.user = user; return this; }
        public ApiResponseBuilder history(List<?> history) { response.history = history; return this; }
        public ApiResponseBuilder count(Long count) { response.count = count; return this; }

        public ApiResponse build() { return response; }
    }
}
