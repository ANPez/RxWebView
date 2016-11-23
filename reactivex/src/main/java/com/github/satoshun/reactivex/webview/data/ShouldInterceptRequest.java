package com.github.satoshun.reactivex.webview.data;

public class ShouldInterceptRequest implements RxWebViewClientData {

  private final String url;

  public ShouldInterceptRequest(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
