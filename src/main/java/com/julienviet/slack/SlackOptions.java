package com.julienviet.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JdkSSLEngineOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.SSLEngineOptions;
import io.vertx.core.net.TrustOptions;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class SlackOptions extends HttpClientOptions {

  private static final String DEFAULT_HOST = "slack.com";

  private String token;

  public SlackOptions() {
    setDefaultHost(DEFAULT_HOST);
    setDefaultPort(443);
    setSsl(true);
    setTrustAll(true);
  }

  public SlackOptions(JsonObject json) {
    throw new UnsupportedOperationException();
  }

  public SlackOptions(SlackOptions that) {
    super(that);
    token = that.token;
  }


  public String getToken() {
    return token;
  }

  public SlackOptions setToken(String token) {
    this.token = token;
    return this;
  }

  @Override
  public SlackOptions setSendBufferSize(int sendBufferSize) {
    return (SlackOptions) super.setSendBufferSize(sendBufferSize);
  }

  @Override
  public SlackOptions setReceiveBufferSize(int receiveBufferSize) {
    return (SlackOptions) super.setReceiveBufferSize(receiveBufferSize);
  }

  @Override
  public SlackOptions setReuseAddress(boolean reuseAddress) {
    return (SlackOptions) super.setReuseAddress(reuseAddress);
  }

  @Override
  public SlackOptions setTrafficClass(int trafficClass) {
    return (SlackOptions) super.setTrafficClass(trafficClass);
  }

  @Override
  public SlackOptions setTcpNoDelay(boolean tcpNoDelay) {
    return (SlackOptions) super.setTcpNoDelay(tcpNoDelay);
  }

  @Override
  public SlackOptions setTcpKeepAlive(boolean tcpKeepAlive) {
    return (SlackOptions) super.setTcpKeepAlive(tcpKeepAlive);
  }

  @Override
  public SlackOptions setSoLinger(int soLinger) {
    return (SlackOptions) super.setSoLinger(soLinger);
  }

  @Override
  public SlackOptions setUsePooledBuffers(boolean usePooledBuffers) {
    return (SlackOptions) super.setUsePooledBuffers(usePooledBuffers);
  }

  @Override
  public SlackOptions setIdleTimeout(int idleTimeout) {
    return (SlackOptions) super.setIdleTimeout(idleTimeout);
  }

  @Override
  public SlackOptions setSsl(boolean ssl) {
    return (SlackOptions) super.setSsl(ssl);
  }

  @Override
  public SlackOptions setKeyCertOptions(KeyCertOptions options) {
    return (SlackOptions) super.setKeyCertOptions(options);
  }

  @Override
  public SlackOptions setKeyStoreOptions(JksOptions options) {
    return (SlackOptions) super.setKeyStoreOptions(options);
  }

  @Override
  public SlackOptions setPfxKeyCertOptions(PfxOptions options) {
    return (SlackOptions) super.setPfxKeyCertOptions(options);
  }

  @Override
  public SlackOptions setTrustOptions(TrustOptions options) {
    return (SlackOptions) super.setTrustOptions(options);
  }

  @Override
  public SlackOptions setPemKeyCertOptions(PemKeyCertOptions options) {
    return (SlackOptions) super.setPemKeyCertOptions(options);
  }

  @Override
  public SlackOptions setTrustStoreOptions(JksOptions options) {
    return (SlackOptions) super.setTrustStoreOptions(options);
  }

  @Override
  public SlackOptions setPfxTrustOptions(PfxOptions options) {
    return (SlackOptions) super.setPfxTrustOptions(options);
  }

  @Override
  public SlackOptions setPemTrustOptions(PemTrustOptions options) {
    return (SlackOptions) super.setPemTrustOptions(options);
  }

  @Override
  public SlackOptions addEnabledCipherSuite(String suite) {
    return (SlackOptions) super.addEnabledCipherSuite(suite);
  }

  @Override
  public SlackOptions addEnabledSecureTransportProtocol(String protocol) {
    return (SlackOptions) super.addEnabledSecureTransportProtocol(protocol);
  }

  @Override
  public SlackOptions addCrlPath(String crlPath) throws NullPointerException {
    return (SlackOptions) super.addCrlPath(crlPath);
  }

  @Override
  public SlackOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    return (SlackOptions) super.addCrlValue(crlValue);
  }

  @Override
  public SlackOptions setConnectTimeout(int connectTimeout) {
    return (SlackOptions) super.setConnectTimeout(connectTimeout);
  }

  @Override
  public SlackOptions setTrustAll(boolean trustAll) {
    return (SlackOptions) super.setTrustAll(trustAll);
  }

  @Override
  public SlackOptions setMaxPoolSize(int maxPoolSize) {
    return (SlackOptions) super.setMaxPoolSize(maxPoolSize);
  }

  @Override
  public SlackOptions setHttp2MultiplexingLimit(int limit) {
    return (SlackOptions) super.setHttp2MultiplexingLimit(limit);
  }

  @Override
  public SlackOptions setHttp2MaxPoolSize(int max) {
    return (SlackOptions) super.setHttp2MaxPoolSize(max);
  }

  @Override
  public SlackOptions setHttp2ConnectionWindowSize(int http2ConnectionWindowSize) {
    return (SlackOptions) super.setHttp2ConnectionWindowSize(http2ConnectionWindowSize);
  }

  @Override
  public SlackOptions setKeepAlive(boolean keepAlive) {
    return (SlackOptions) super.setKeepAlive(keepAlive);
  }

  @Override
  public SlackOptions setPipelining(boolean pipelining) {
    return (SlackOptions) super.setPipelining(pipelining);
  }

  @Override
  public SlackOptions setPipeliningLimit(int limit) {
    return (SlackOptions) super.setPipeliningLimit(limit);
  }

  @Override
  public SlackOptions setVerifyHost(boolean verifyHost) {
    return (SlackOptions) super.setVerifyHost(verifyHost);
  }

  @Override
  public SlackOptions setTryUseCompression(boolean tryUseCompression) {
    return (SlackOptions) super.setTryUseCompression(tryUseCompression);
  }

  @Override
  public SlackOptions setMaxWebsocketFrameSize(int maxWebsocketFrameSize) {
    return (SlackOptions) super.setMaxWebsocketFrameSize(maxWebsocketFrameSize);
  }

  @Override
  public SlackOptions setDefaultHost(String defaultHost) {
    return (SlackOptions) super.setDefaultHost(defaultHost);
  }

  @Override
  public SlackOptions setDefaultPort(int defaultPort) {
    return (SlackOptions) super.setDefaultPort(defaultPort);
  }

  @Override
  public SlackOptions setProtocolVersion(HttpVersion protocolVersion) {
    return (SlackOptions) super.setProtocolVersion(protocolVersion);
  }

  @Override
  public SlackOptions setMaxChunkSize(int maxChunkSize) {
    return (SlackOptions) super.setMaxChunkSize(maxChunkSize);
  }

  @Override
  public SlackOptions setMaxWaitQueueSize(int maxWaitQueueSize) {
    return (SlackOptions) super.setMaxWaitQueueSize(maxWaitQueueSize);
  }

  @Override
  public SlackOptions setInitialSettings(Http2Settings settings) {
    return (SlackOptions) super.setInitialSettings(settings);
  }

  @Override
  public SlackOptions setUseAlpn(boolean useAlpn) {
    return (SlackOptions) super.setUseAlpn(useAlpn);
  }

  @Override
  public SlackOptions setSslEngineOptions(SSLEngineOptions sslEngineOptions) {
    return (SlackOptions) super.setSslEngineOptions(sslEngineOptions);
  }

  @Override
  public SlackOptions setJdkSslEngineOptions(JdkSSLEngineOptions sslEngineOptions) {
    return (SlackOptions) super.setJdkSslEngineOptions(sslEngineOptions);
  }

  @Override
  public SlackOptions setOpenSslEngineOptions(OpenSSLEngineOptions sslEngineOptions) {
    return (SlackOptions) super.setOpenSslEngineOptions(sslEngineOptions);
  }

  @Override
  public SlackOptions setAlpnVersions(List<HttpVersion> alpnVersions) {
    return (SlackOptions) super.setAlpnVersions(alpnVersions);
  }

  @Override
  public SlackOptions setHttp2ClearTextUpgrade(boolean value) {
    return (SlackOptions) super.setHttp2ClearTextUpgrade(value);
  }

  @Override
  public SlackOptions setMetricsName(String metricsName) {
    return (SlackOptions) super.setMetricsName(metricsName);
  }

  @Override
  public SlackOptions setProxyOptions(ProxyOptions proxyOptions) {
    return (SlackOptions) super.setProxyOptions(proxyOptions);
  }

  @Override
  public SlackOptions setLogActivity(boolean logEnabled) {
    return (SlackOptions) super.setLogActivity(logEnabled);
  }
}
