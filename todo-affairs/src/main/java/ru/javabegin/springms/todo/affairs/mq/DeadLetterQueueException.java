package ru.javabegin.springms.todo.affairs.mq;

public class DeadLetterQueueException extends Throwable {
    public DeadLetterQueueException(String message) {
        super(message);
    }

    public DeadLetterQueueException(String message, Throwable cause) {
        super(message, cause);
    }

}
