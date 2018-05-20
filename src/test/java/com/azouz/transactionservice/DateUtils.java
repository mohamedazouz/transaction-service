package com.azouz.transactionservice;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateUtils {

  public static long getCurrentTimestamp() {
    return DateTime.now(DateTimeZone.UTC).getMillis();
  }

}
