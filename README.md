Biblia
==========

Android向けの書籍検索ライブラリです。次の書籍検索APIに対応しています。

* [楽天ブックス書籍検索API](https://webservice.rakuten.co.jp/api/booksbooksearch/)

使い方
----------

### インストール

```diff
// app/build.gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:24.1.1'
+    compile 'info.nkzn.biblia:biblia:0.2.0'
    testCompile 'junit:junit:4.12'
}
```

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

### 色んなパラメータで検索を行う

複数のパラメータで検索を行う場合は、 `Biblia.client().search(SearchParams)` を使用します。 `SearchParams.Builder` を利用すると、より効率的にSearchParamsを組み立てられます。

```java
SearchParams params = new SearchParams.Builder()
    .title("メロス")
    .author("太宰治")
    .build();

// or
// SearchParams.Builder builder = new SearchParams.Builder();
// builder.title("メロス");
// builder.author("太宰治");
// SearchParams params = builder.build();

Biblia.client().search(params)
    ...
    .subscribe(books -> {
        displayResults(books);
    });
```

サンプルを動かす
----------

1. [楽天APIアプリID発行ページ](https://webservice.rakuten.co.jp/app/create)でAppIDを発行します
2. gradle.properties.templateをgradle.propertiesにコピーして、AppIDを書き込む
3. ビルドする
