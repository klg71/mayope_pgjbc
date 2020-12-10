package org.postgresql.jdbc;

import org.postgresql.PGProperty;
import org.postgresql.util.GT;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.util.Properties;

public enum SslTermination {
  /**
   * Terminate SSL at proxy/loadbalancer
   */
  PROXY("proxy"),
  /**
   * Terminate SSL at psql server
   */
  SERVER("server");


  public static final SslTermination[] VALUES = values();

  public final String value;

  SslTermination(String value) {
    this.value = value;
  }

  public static SslTermination of(Properties info) throws PSQLException {
    String sslTerminationParam = PGProperty.SSL_TERMINATION.get(info);
    // If sslmode is not set, fallback to ssl parameter
    if (sslTerminationParam == null) {
      return SERVER;
    }

    for (SslTermination sslTermination : VALUES) {
      if (sslTermination.value.equalsIgnoreCase(sslTerminationParam)) {
        return sslTermination;
      }
    }
    throw new PSQLException(GT.tr("Invalid ssltermination value: {0}", sslTerminationParam),
        PSQLState.CONNECTION_UNABLE_TO_CONNECT);
  }
}
