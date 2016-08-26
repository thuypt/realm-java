package io.realm.objectserver;

import static android.R.attr.path;

public enum Error {

    // See https://github.com/realm/realm-sync/issues/585
    // See https://github.com/realm/realm-sync/blob/master/doc/protocol.md

    // Realm Java errors (0-49)

    IO_ERROR(0, Category.RECOVERABLE),              // Some IO error while either contacting the server or reading the response
    UNEXPECTED_JSON_FORMAT(1, Category.FATAL),      // JSON input could not be parsed correctly

    // Realm Authentication Server response errors (50 - 99)

    REALM_PROBLEM(50),
    INVALID_PARAMETERS(51),
    MISSING_PARAMETERS(52),
    INVALID_CREDENTIALS(53),
    UNKNOWN_ACCOUNT(54),
    EXISTING_ACCOUNT(55),
    ACCESS_DENIED(56),
    INVALID_REFRESH_TOKEN(57),
    EXPIRED_REFRESH_TOKEN(58),
    INTERNAL_SERVER_ERROR(59),

    // Realm Object Server errors (100 - 199)

    // Connection level and protocol errors
    CONNECTION_CLOSED(100, Category.INFO),      // Connection closed (no error)
    OTHER_ERROR(101, Category.INFO),            // Other connection level error
    UNKNOWN_MESSAGE(102, Category.INFO),        // Unknown type of input message
    BAD_SYNTAX(103, Category.INFO),             // Bad syntax in input message head
    LIMITS_EXCEEDED(104, Category.INFO),        // Limits exceeded in input message
    WRONG_PROTOCOL_VERSION(105, Category.INFO), // Wrong protocol version (CLIENT)
    BAD_SESSION_IDENT(106, Category.INFO),      // Bad session identifier in input message
    REUSE_OF_SESSION_IDENT(107, Category.INFO), // Overlapping reuse of session identifier (BIND)
    BOUND_IN_OTHER_SESSION(108, Category.INFO), // Client file bound in other session (IDENT)
    BAD_MESSAGE_ORDER(109, Category.INFO),      // Bad input message order

    // Session level errors (200 - 299)
    SESSION_CLOSED(200),                        // Session closed (no error)
    OTHER_SESSION_ERROR(201),                   // Other session level error
    TOKEN_EXPIRED(202, Category.RECOVERABLE),   // Access token expired
    BAD_AUTHENTICATION(203),                    // Bad user authentication (BIND, REFRESH)
    ILLEGAL_REALM_PATH(204),                    // Illegal Realm path (BIND)
    NO_SUCH_PATH(205),                          // No such Realm (BIND)
    PERMISSION_DENIED(206),                     // Permission denied (BIND, REFRESH)
    BAD_SERVER_FILE_IDENT(207),                 // Bad server file identifier (IDENT)
    BAD_CLIENT_FILE_IDENT(208),                 // Bad client file identifier (IDENT)
    BAD_SERVER_VERSION(209),                    // Bad server version (IDENT, UPLOAD)
    BAD_CLIENT_VERSION(210),                    // Bad client version (IDENT, UPLOAD)
    DIVERGING_HISTORIES(211),                   // Diverging histories (IDENT)
    BAD_CHANGESET(212);                         // Bad changeset (UPLOAD)

    private final int code;
    private final Category category;

    Error(int errorCode) {
        this(errorCode, Category.FATAL);
    }

    Error(int errorCode, Category category) {
        this.code = errorCode;
        this.category = category;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + code + ")";
    }

    public int errorCode() {
        return code;
    }


    /**
     * Returns the category of the error.
     *
     * Errors come in 3 categories: FATAL, RECOVERABLE, and INFO.
     *
     * FATAL: The session cannot be recovered and needs to be re-created. A likely cause is that the User does not
     * have access to this Realm. Check that the {@link SyncConfiguration} is correct.
     *
     * RECOVERABLE: The session is paused until given additional information. Most likely cause is an expired access
     * token or similar.
     *
     * INFO: The underlying sync client will automatically try to recover from this.
     *
     * @return the severity of the error.
     */
    public Category getCategory() {
        return category;
    }

    public static Error fromInt(int errorCode) {
        Error[] errors = values();
        for (int i = 0; i < errors.length; i++) {
            Error error = errors[i];
            if (error.errorCode() == errorCode) {
                return error;
            }
        }
        throw new IllegalArgumentException("Unknown error code: " + errorCode);
    }

    public static Error fromAuthError(String type) {
        switch(type) {
            case "https://realm.io/docs/object-server/problems/invalid-credentials" : return Error.INVALID_CREDENTIALS;
            case "https://realm.io/docs/object-server/problems/unknown-account" : return Error.UNKNOWN_ACCOUNT;
            case "https://realm.io/docs/object-server/problems/existing-account" : return Error.EXISTING_ACCOUNT;
            case "https://realm.io/docs/object-server/problems/access-denied" : return Error.ACCESS_DENIED;
            case "https://realm.io/docs/object-server/problems/expired-refresh-token" : return Error.EXPIRED_REFRESH_TOKEN;
            case "https://realm.io/docs/object-server/problems/internal-server-error" : return Error.INTERNAL_SERVER_ERROR;
            default:
                return Error.UNEXPECTED_JSON_FORMAT;
        }
    }

public enum Category {
        FATAL,          // Abort session as soon as possible
        RECOVERABLE,    // Still possible to recover by providing additional information to the session
        INFO            // Just FYI. The underlying network client will automatically try to recover.
    }
}