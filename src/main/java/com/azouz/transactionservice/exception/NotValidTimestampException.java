package com.azouz.transactionservice.exception;

public class NotValidTimestampException extends Exception {

  private static final long serialVersionUID = -3932580886051016069L;

  public NotValidTimestampException() {
    super();
  }

  public NotValidTimestampException(final String message) {
    super(message);
  }

  public NotValidTimestampException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NotValidTimestampException(final Throwable cause) {
    super(cause);
  }
}
