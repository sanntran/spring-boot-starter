package net.ionoff.common.validation.message;

import java.io.Serializable;
import java.util.Objects;

public class ServiceMessage implements Serializable {

    protected final String code;
    protected final String message;

    ServiceMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return code == null ? 0 : code.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof ServiceMessage)) {
            return false;
        }

        ServiceMessage other = (ServiceMessage) object;
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("code=").append(code).append(", message=").append(message).toString();
    }

    public static ServiceMessage tokenExpired() {
        return new ServiceMessage("tokenExpired",
                "JWT expired");
    }

    public static ServiceMessage tokenInvalid() {
        return new ServiceMessage("tokenInvalid",
                "JWT is invalid");
    }

    public static ServiceMessage unauthorized() {
        return new ServiceMessage("unauthorized",
                "Unauthorized request");
    }

    public static ServiceMessage accessDenied() {
        return new ServiceMessage("accessDenied",
                "Access denied");
    }

    public static ServiceMessage entityNotFound() {
        return new ServiceMessage("entityNotFound",
                "Entity not found");
    }

    public static ServiceMessage internalServerError() {
        return new ServiceMessage("internalServerError",
                "Internal server error");
    }

    public static ServiceMessage internalServerError(String message) {
        return new ServiceMessage("internalServerError",
                String.format("Internal server error: %s", message));
    }

    /**
     *
     * @param springExceptionInternal Exception created by Spring
     * @return ServiceMessage with code depend on the exception
     */
    public static ServiceMessage springExceptionInternal(Exception springExceptionInternal) {
        String clazz = springExceptionInternal.getClass().getSimpleName();
        String firstChar = clazz.substring(0, 1).toLowerCase();
        String code = new StringBuilder(firstChar).append(clazz.substring(1)).toString();
        return new ServiceMessage(code, springExceptionInternal.getMessage());
    }
}
