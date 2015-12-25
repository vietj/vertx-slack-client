package io.nonobot.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class SlackOptions {

  private static final String DEFAULT_HOST = "slack.com";
  private static final HttpClientOptions DEFAULT_CLIENT_OPTIONS = new HttpClientOptions().
      setDefaultHost("slack.com").
      setDefaultPort(443).
      setSsl(true).
      setTrustAll(true);

  private String token;
  private HttpClientOptions clientOptions;

  public SlackOptions() {
    clientOptions = new HttpClientOptions(DEFAULT_CLIENT_OPTIONS);
  }

  public SlackOptions(JsonObject json) {
    throw new UnsupportedOperationException();
  }

  public SlackOptions(SlackOptions that) {
    clientOptions = new HttpClientOptions(that.getClientOptions());
    token = that.token;
  }


  public String getToken() {
    return token;
  }

  public SlackOptions setToken(String token) {
    this.token = token;
    return this;
  }

  public HttpClientOptions getClientOptions() {
    return clientOptions;
  }

  public SlackOptions setClientOptions(HttpClientOptions clientOptions) {
    this.clientOptions = clientOptions;
    return this;
  }
}
