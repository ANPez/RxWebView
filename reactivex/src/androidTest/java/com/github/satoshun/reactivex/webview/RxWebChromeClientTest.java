package com.github.satoshun.reactivex.webview;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.WebChromeClient;

import com.github.satoshun.reactivex.webview.data.OnProgressChanged;
import com.github.satoshun.reactivex.webview.data.RxWebChromeClientData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RxWebChromeClientTest {

  @Rule public final ActivityTestRule<RxWebViewTestActivity> activityRule =
      new ActivityTestRule<>(RxWebViewTestActivity.class);

  private RxWebViewTestActivity activity;

  @Before public void setUp() throws Exception {
    activity = activityRule.getActivity();
  }


  @Test public void onPageFinished() throws Exception {
    WebChromeClient client = new WebChromeClient();

    TestObserver<Integer> o = RxWebChromeClient.all(activity.webview, client)
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override public void accept(Disposable disposable) throws Exception {
            activity.webview.loadUrl("https://www.google.com/");
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .filter(new Predicate<RxWebChromeClientData>() {
          @Override
          public boolean test(RxWebChromeClientData data) throws Exception {
            return data instanceof OnProgressChanged;
          }
        })
        .map(new Function<RxWebChromeClientData, Integer>() {
          @Override
          public Integer apply(RxWebChromeClientData data) throws Exception {
            return ((OnProgressChanged) data).getNewProgress();
          }
        })
        .take(1)
        .test();
    o.await(1, TimeUnit.SECONDS);
    o.assertValueCount(1);
  }
}
