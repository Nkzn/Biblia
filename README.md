Biblia
==========

Android向けの書籍検索ライブラリです。次の書籍検索APIに対応しています。

* [楽天ブックス書籍検索API](https://webservice.rakuten.co.jp/api/booksbooksearch/)

Usage
----------

### 初期設定

`Application#onCreate` でAPIキー（AppID）の登録をおこなってください。

```java
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    Biblia.setRakutenApplicationId("1234567891234567890");
  }
}
```

### 書籍名で検索を行う

`Biblia.client().search(String)` で書籍名による検索を行います。[RxJava](https://github.com/ReactiveX/RxJava)の `Observable` を戻り値にしており、 `subscribe` に登録したコールバックへ通信の結果が渡されます。

```java
Biblia.client().search(title)
    .subscribe((List<Book> books) -> {
      // 通信が成功したときの処理
    }, (Throwable throwable) -> {
      // 通信が失敗したときの処理
    });
```

実際に利用する際は、上記のコードだけでは不十分です。Bibliaはスレッドの扱いについてRxJavaのデフォルトのまま動作するので、RxAndroidなどを使ってsubscribeがUIスレッドになるようにする必要があるでしょう。また、subscribeしたままにしておくとメモリリークするため、CompositeSubscriptionで適切にsubscriptionをunsubscribeしてあげましょう。

実用的なコードは次のような形になるはずです。

```java
private final CompositeSubscription subscriptions =
  new CompositeSubscription();

private void search(String title) {
  Subscription subscription = Biblia.client().search(title)
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread()) // RxAndroid
      .subscribe((List<Book> books) -> {
        displayResults(books);
      }, (Throwable throwable) -> {
        treatErrors(throwable);
      });

  subscriptions.add(subscription);
}

@Override
protected void onDestroy() {
    if (subscriptions.isUnsubscribed()) {
        subscriptions.unsubscribe();
    }
    super.onDestroy();
}
```
