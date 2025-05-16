package movieapp.webmovie.dto;

public class AuthResponse {
    private String message;
    private Integer userId;

    public AuthResponse(String message, Integer userId) {
        this.message = message;
        this.userId = userId;
    }

    // Getters & Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
