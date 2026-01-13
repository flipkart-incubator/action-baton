/*
 * Copyright 2026 ActionBaton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.actionbaton.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Base exception class for all errors in the ActionBaton framework.
 * It uses {@link ErrorCode} to categorize different types of failures.
 */
@Getter
public class ActionBatonException extends Exception {

    /**
     * The error code identifying the type of exception.
     */
    private ErrorCode errorCode;

    /**
     * Arguments for formatting the error message.
     */
    private String[] args;

    /**
     * Constructs a new exception with the specified error code and arguments.
     *
     * @param code The error code.
     * @param args The formatting arguments.
     */
    public ActionBatonException(ErrorCode code, String... args){
        this.errorCode = code;
        this.args = args;
    }

    /**
     * Constructs a new exception with the specified error code and cause.
     *
     * @param code The error code.
     * @param ex The cause of the exception.
     */
    public ActionBatonException(ErrorCode code, Exception ex){
        super(ex.getMessage(), ex.getCause());
        this.errorCode = code;
    }

    /**
     * Constructs a new exception with the specified error code, cause, and arguments.
     *
     * @param code The error code.
     * @param ex The cause of the exception.
     * @param args The formatting arguments.
     */
    public ActionBatonException(ErrorCode code, Exception ex, String... args){
        super(ex.getMessage(), ex.getCause());
        this.errorCode = code;
        this.args = args;
    }

    /**
     * Returns the formatted error message based on the {@link ErrorCode} and arguments.
     *
     * @return The formatted message.
     */
    public String getMessage(){
        return (args == null || args.length == 0)
                ? errorInfoMap.get(this.errorCode)
                : String.format(errorInfoMap.get(this.errorCode), (Object[]) args);
    }

    /**
     * Map of error codes to their respective message templates.
     */
    private static final Map<ErrorCode, String> errorInfoMap = new HashMap<ErrorCode, String>() {{
        put(ErrorCode.WRONG_ACTION_BUILDER, "%1$s");
        put(ErrorCode.ACTION_EXECUTION_FAILED, "Action %1$s execution is failed");
        put(ErrorCode.ACTION_EXECUTION_TIMEOUT, "Action execution is timeout. Message : %1$s");
        put(ErrorCode.INVALID_ACTION_DAG_ENTITY, "Invalid Action Dag Entity for Action : %1$s");
        put(ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, "Validation failed for Action entity for Action : %1$s | Action Executor : %2$s");

    }};

    /**
     * Enumeration of error codes representing different failure scenarios.
     */
    public enum ErrorCode {
        /**
         * Indicates that an incorrect builder was used for an action.
         */
        WRONG_ACTION_BUILDER,

        /**
         * Indicates that an action execution failed.
         */
        ACTION_EXECUTION_FAILED,

        /**
         * Indicates that an action execution timed out.
         */
        ACTION_EXECUTION_TIMEOUT,

        /**
         * Indicates that an Action DAG entity is invalid.
         */
        INVALID_ACTION_DAG_ENTITY,

        /**
         * Indicates that validation failed for an action entity.
         */
        ACTION_ENTITY_VALIDATION_FAILED
    }
}


