# Retrofitを用いた通信について

AndroidでHTTP通信をする際に使えるライブラリの1つに、Retrofitがあります。

今回は、Retrofitを用いてHTTP通信をする方法について解説します。

## Retrofitをプロジェクトに導入する

HTTP通信をするので、 `AndroidManifest.xml` に　`android.permission.INTERNET` のパーミッションを追加するのを忘れないようにしましょう。

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    
    ...
</manifest>
```

`app/build.gradle` に以下を追加します。

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
```

`Retrofit` はタイプセーフなHTTP通信クライアントライブラリですが、HTTP通信でやりとりするJSONとKotlinの型の変換の部分はRetrofitに含まれていません。JSONライブラリはまた別で導入する必要があり、今回は  `Moshi` を使います。 `Moshi` は公式で `Retrofit` の拡張が用意されており、今回はそれも合わせて導入します。

## Retrofitのセットアップ

Retrofitを使う際に、HTTP通信のBase URLやJSONのコンバーター、必要に応じてカスタムのインターセプターなどを設定します。

Retrofitのセットアップの前に、コンバーターとして使うMoshiのセットアップをします。

```kotlin
val moshi = Moshi.Builder()
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()
```

`Rfc3333DateJsonAdapter` は、RFC3333形式の文字列を `Date` 型に変換するAdapterです。 `KotlinJsonAdapterFactory` を使うことでKotlinのclassのオブジェクトをJSONに変換することができます。

Moshiのセットアップをしたら、次はRetrofitのセットアップです。

```kotlin
val baseUrl = "https://example.com"
val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
```

`baseUrl` は実際に呼び出すAPIのURLに置き換えてください。

`MoshiConverterFactory.create` で先程セットアップした `Moshi` のインスタンスを渡します。

`bnild` してRetrofitのセットアップは完了です。

## エンドポイントの定義

呼び出すエンドポイントのinterfaceを実装します。

今回はYatterのAPIを例にします。

まずはパブリックタイムラインの取得のエンドポイントを実装します。

```
GET /timelines/public
```

以下のようなJSONがレスポンスとして返るエンドポイントです。

```json
[
  {
    "id": 123,
    "account": {
      "id": 0,
      "username": "mayamito",
      "display_name": "マヤミト",
      "create_at": "2023-06-05T12:00:00.000Z",
      "followers_count": 10,
      "note": "Kotlinおたく",
      "avatar": "https://example.com/avatars/mayamito.png",
      "header": "https://example.com/headers/mayamito.png"
    },
    "content": "KotlinかわいいよKotlin",
    "create_at": "2023-06-05T13:00:00.000Z",
    "media_attachments": [
      {
        "id": 123,
        "type": "image",
        "url": "https://example.com/attachments/123.png",
        "description": "example image"
      }
    ]
  }
]
```

まずはレスポンスの型を定義します。

```kotlin
data class AccountJson(
    val id: Int,
    val username: String,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "create_at")
    val createAt: Date,
    @Json(name = "followers_count")
    val followersCount: Int,
    @Json(name = "following_count")
    val followingCount: Int,
    val note: String,
    val avatar: String,
    val header: String,
)

data class AttachmentJson(
    val id: Int,
    val type: String,
    val url: String,
    val description: String,
)

data class StatusJson(
    val id: String,
    val account: AccountJson,
    val content: String,
    @Json(name = "create_at")
    val createAt: Date,
    @Json(name = "media_attachments")
    val mediaAttachments: List<AttachmentJson>,
)
```

Kotlinのプロパティ名とJSONのフィールド名が一致しない場合(createAtとcreate_atなど)は、 `@Json` で実際のJSONのフィールド名を `name` に指定することで対応させることができます。

次はエンドポイントのinterfaceの定義です。

```kotlin
interface YatterApi {

    @GET("/timelines/public")
    suspend fun getPublicTimeline(
        @Query("only_media")
        onlyMedia: Boolean? = null,
        @Query("max_id")
        maxId: Int? = null,
        @Query("since_id")
        sinceId: Int? = null,
        @Query("limit")
        limit: Int? = null,
    ): List<StatusJson>
}
```

`suspend fun` で定義します。返り値の型はMoshiを使って定義したレスポンスのJSONの型を指定します。

`@GET` アノテーションにエンドポイントのpathを指定します。

`@Query` アノテーションをつけた引数がクエリパラメータになります。クエリのkeyはアノテーションに文字列を渡して指定します。

## エンドポイントの呼び出しまで

まずRetrofitオブジェクトからエンドポイントの呼び出しを定義したinterfaceのオブジェクトを取得します。

```kotlin
val yatterApi = retrofit.create<YatterApi>()
```

`create` のジェネリクスにこちらで定義したエンドポイントの型を指定すると、それを取得できます。

あとはそれの目的のメソッドを呼び出せばAPIコールができます。

```kotlin
val timelineResponse = yatterApi.getPublicTimeline()
```

## エンドポイントの定義いろいろ

先程の例ではクエリパラメータがあるくらいの単純なGETリクエストの例でしたが、他にもいくつかパターンを見てみましょう。

### JSONのbodyを渡したい場合

例えば、Yatterのアカウント作成のエンドポイントなど、bodyにJSONを渡したい場合はよくあります。

```kotlin
data class CreateAccountJson(
    val username: String,
    val password: String,
)
```

このJSONをbodyとして渡すには、エンドポイントの定義で引数に `@Body` アノテーションを付与することで指定した引数をbodyにできます。

```kotlin
@POST("/accounts")
suspend fun createAccount(
    @Body
    createAccountJson: CreateAccountJson,
): AccountJson
```

### Headerを渡したい場合

例えば、YatterのHomeタイムラインを取得するのに `Authorization` を渡すなど、ヘッダーを指定したい場合です。

これはエンドポイントの定義で引数に `@Header` アノテーションを付与し、アノテーションにヘッダー名を渡すことで指定した引数をヘッダーにできます。

```kotlin
@GET("/timelines/home")
suspend fun getHomeTimeline(
    @Header("Authorization")
    token: String,
    @Query("only_media")
    onlyMedia: Boolean? = null,
    @Query("max_id")
    maxId: Int? = null,
    @Query("since_id")
    sinceId: Int? = null,
    @Query("limit")
    limit: Int? = null,
): List<StatusJson>
```

### multipart/form-data でリクエストしたい場合

例えば、Yatterのアカウント情報の更新のエンドポイントなど、一部のエンドポイントだけ `ContentType: multipart/form-data` でリクエストを送りたい場合があると思います。

この場合は、メソッドにHTTPのメソッドの指定に加えて `@Multipart` アノテーションを付与し、フォームとして渡したい引数に `@Part` アノテーションを付与することでできます。 `@Part` アノテーションを付与する場合、単純な文字列などの場合は型の指定を通常と同じように `String` のようにして大丈夫ですが、ファイルのアップロードをしたい場合はファイルとして渡したい引数を `RequestBody` 型で指定する必要があります。

```kotlin
@Multipart
@POST("/accounts/update_credentials")
suspend fun updateCredentials(
    @Header("Authorization")
    token: String,
    @Part("display_name")
    displayName: String,
    @Part
    note: String,
    @Part("avatar")
    avatar: RequestBody,
    @Part("header")
    header: RequestBody,
): AccountJson
```

呼び出し側は次のようになります。

```kotlin
val avatarBody = avatarFile.asRequestBody()
val headerBody = headerFile.asRequestBody()
val updateResponse = yatterApi.updateCredentials(
    token = token,
    displayName = displayName,
    note = note,
    avatar = avatarBody,
    header = headerBody,
)
```
