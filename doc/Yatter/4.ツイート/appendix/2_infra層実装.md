# ツイート画面のinfra層実装
ツイート画面のinfra層実装を行います。
infra層では、domain層実装時に定義した`GetMeService`と`AccountRepository`、`StatusRepository`の実装を行います。

まずは、`Me`の実装から始めます。
`infra/domina`に`MeImpl`クラスを作成し実装します。  
基本的には`AccountImpl`と同じような内容になります。  

```Kotlin
class MeImpl(
  id: AccountId,
  username: Username,
  displayName: String?,
  note: String?,
  avatar: URL,
  header: URL,
  followingCount: Int,
  followerCount: Int,
) : Me(
  id = id,
  username = username,
  displayName = displayName,
  note = note,
  avatar = avatar,
  header = header,
  followingCount = followingCount,
  followerCount = followerCount,
) {
  override suspend fun follow(username: Username) {
    TODO("Not yet implemented")
  }

  override suspend fun unfollow(username: Username) {
    TODO("Not yet implemented")
  }

  override suspend fun followings(): List<Account> {
    TODO("Not yet implemented")
  }

  override suspend fun followers(): List<Account> {
    TODO("Not yet implemented")
  }
}
```

`MeImpl`が実装できたところでコンバーターも実装します。  
`AccountConverter`と同じディレクトリに`MeConverter`を定義します。  

```Kotlin
object MeConverter {
  fun convertToMe(account: Account): Me {
    return MeImpl(
      id = account.id,
      username = account.username,
      displayName = account.displayName,
      note = account.note,
      avatar = account.avatar,
      header = account.header,
      followingCount = account.followingCount,
      followerCount = account.followerCount,
    )
  }
}
```

続いては`AccountRepository`の実装です。  
必要な引数と`impl`クラスの定義を`infra/domain/repository`に追加します。  

```Kotlin
class AccountRepositoryImpl(
  private val yatterApi: YatterApi,
  private val mePreferences: MePreferences
) : AccountRepository {
}
```
必要なメソッドをオーバーライドします。  

```Kotlin
class AccountRepositoryImpl(
  private val yatterApi: YatterApi,
  private val mePreferences: MePreferences
) : AccountRepository {
  override suspend fun findMe(): Me? {
    TODO("Not yet implemented")
  }

  override suspend fun findByUsername(username: Username): Account? {
    TODO("Not yet implemented")
  }

  override suspend fun create(username: Username, password: Password): Me {
    TODO("Not yet implemented")
  }

  override suspend fun update(
    me: Me,
    newDisplayName: String?,
    newNote: String?,
    newAvatar: URL?,
    newHeader: URL?
  ): Me {
    TODO("Not yet implemented")
  }
}
```

`AccountRepositoryImpl`では、ユーザー名をもとにAPIからアカウント情報を取得します。  

そのためまずは、`YatterApi`の定義から始めます。  
`getAccountByUsername`を追加してユーザー名からアカウント情報を取得できるようにします。  

```Kotlin
@GET("accounts/{username}")
suspend fun getAccountByUsername(
  @Path("username") username: String
): AccountJson
```

APIの定義ができたらツイート機能に必要な`findMe`と`findByUsername`をまずは実装します。  
処理は次の手順を実装します。  

- mePreferencesからログイン済みユーザー情報取得
  - ユーザー情報取得できなければ取得不可(null)
- ユーザー名からAPIを経由してアカウント情報取得
- `Me`ドメインへ変換

コードは次のようになります。  

```Kotlin
override suspend fun findMe(): Me? = withContext(Dispatchers.IO) {
  val username = mePreferences.getUsername() ?: return@withContext null
  if (username.isEmpty()) return@withContext null

  val account = findByUsername(username = Username(username)) ?: return@withContext null
  MeConverter.convertToMe(account)
}

override suspend fun findByUsername(username: Username): Account? = withContext(Dispatchers.IO) {
  val accountJson = yatterApi.getAccountByUsername(username = username.value)
  AccountConverter.convertToDomainModel(accountJson)
}
```

残りのメソッドは今回の機能では利用しないため、ひとまずTODOのままにしておきます。  
実装したメソッドには単体テストを書いてみましょう。  


<details>
<summary>AccountRepositoryImplのテスト実装例</summary>

```Kotlin
class AccountRepositoryImplSpec {
  val yatterApi = mockk<YatterApi>()
  val mePreferences = mockk<MePreferences>()
  val subject = AccountRepositoryImpl(yatterApi, mePreferences)

  @Test
  fun getAccountByUsername() = runTest {
    val username = Username("username")
    val accountJson = AccountJson(
      id = "id",
      username = "username",
      displayName = "display name",
      note = null,
      avatar = "",
      header = "",
      followingCount = 0,
      followersCount = 0,
      createAt = ""
    )

    val expect = AccountConverter.convertToDomainModel(accountJson)

    coEvery {
      yatterApi.getAccountByUsername(any())
    } returns accountJson

    val result = subject.findByUsername(username)

    coVerify {
      yatterApi.getAccountByUsername(username.value)
    }

    assertThat(result).isEqualTo(expect)
  }

  @Test
  fun getMe() = runTest {
    val username = "username"
    val accountJson = AccountJson(
      id = "id",
      username = "username",
      displayName = "display name",
      note = null,
      avatar = "",
      header = "",
      followingCount = 0,
      followersCount = 0,
      createAt = ""
    )

    val expect = AccountConverter.convertToDomainModel(accountJson)

    coEvery {
      yatterApi.getAccountByUsername(any())
    } returns accountJson

    coEvery {
      mePreferences.getUsername()
    } returns username

    val result = subject.findMe()

    coVerify {
      yatterApi.getAccountByUsername(username)
    }

    coVerify {
      mePreferences.getUsername()
    }

    assertThat(result).isEqualTo(expect)
  }
}
```

</details>

---

続いて`GetMeService`の実装です。

`infra/domain/service`に`GetMeServiceImpl`クラスを定義します。  
`AccountRepository`を介して`Me`クラスを取得するため引数に`AccountRepository`も追加しておきます。  

```Kotlin
class GetMeServiceImpl(
  private val accountRepository: AccountRepository,
) : GetMeService {
  suspend fun execute(): Me? {
    TODO("Not yet implemented")
  }
}
```

`AccountRepository`を利用して`Me`ドメインを取得します。  

```Kotlin
override suspend fun execute(): Me? = withContext(Dispatchers.IO) {
  accountRepository.findMe()
}
```

こちらもテストを実装して動作の担保をします。  

<details>
<summary>GetMeServiceImplSpecのテスト実装例</summary>

```Kotlin
class GetMeServiceImplSpec {
  private val accountRepository = mockk<AccountRepository>()
  private val subject = GetMeServiceImpl(accountRepository)

  @Test
  fun getMe() {
    val account = AccountImpl(
      id = AccountId(value = ""),
      username = Username(value = ""),
      displayName = null,
      note = null,
      avatar = URL("https://www.google.com"),
      header = URL("https://www.google.com"),
      followingCount = 0,
      followerCount = 0
    )
    val expect = MeConverter.convertToMe(account)

    coEvery { accountRepository.findMe() } returns MeConverter.convertToMe(account)

    val result = runBlocking { subject.execute() }

    assertThat(result).isEqualTo(expect)
  }
}
```

</details>

---

`StatusRepository`の実装です。  
`StatusRepository`でのツイート処理はAPI処理が必要になるため、まずはYatterApiの定義から始めます。  

APIの定義を確認するとツイート用のAPIを実行する際にはBodyJsonが必要となっていますので、`PostStatusJson`クラスを定義します。  
今回の実装ではテキストのみのツイート機能ですが後々にメディアの投稿も行うため、先に定義しておきます。  

```Kotlin
data class PostStatusJson(
  val status: String,
  @Json(name = "media_ids") val mediaIds: List<Int>
)
```

Jsonクラスが定義できたら`YatterApi`の定義です。  
ツイート用の`postStatus`を追加します。  
BodyJsonを利用する場合は`@Body`を使用します。  

```Kotlin
@POST("statuses")
suspend fun postStatus(
  @Header("Authentication") token: String,
  @Body statusJson: PostStatusJson
): StatusJson
```

APIの定義ができたらRepositoryの実装に戻ります。  
既に`StatusRepositoryImpl`クラスは定義されているため、クラスを開くだけです。  

`create`メソッドでは次の処理を実施します。  

- ログイン済みか確認
  - ログイン済みでなかったら例外スロー
- APIを通じて投稿
  - メディアファイルの投稿は今は考えない
- 投稿完了したらStatusを返す

ログイン済みか確認する方法として、`MePreferences.getUsername()`の実行時にユーザー名が返るかどうかで判定します。  
`getUsername()`がnull(=ログインしていない)であれば`AuthenticatorException`をスローします。  

`MePreferences`を利用するには`StatusRepositoryImpl`の引数に追加してあげる必要があるのでまずは引数の追加から行います。  

```Kotlin
class StatusRepositoryImpl(
  private val yatterApi: YatterApi,
  private val mePreferences: MePreferences,
) : StatusRepository {...}
```

StatusRepositoryImplの引数を増やしたら、DI側の設定も更新が必要になります。  
`DomainImplModule`を開き、`StatusRepositoryImpl`のインスタンス化箇所にも引数追加します。　　

```Kotlin
val domainImplModule = module {
  single<StatusRepository> { StatusRepositoryImpl(get(), get()) } // 引数2つ目にもget()を追加
}
```

引数に`get()`を追加することで対応したインスタンスをDIが解決して引数に渡してくれます。  

引数の準備ができたら実際に`create`の実装に入ります。  
Statusの投稿APIでは、`username ${ユーザー名}`という形式で値をHeaderに追加する必要があるためそれ用のメソッドも`private`で用意します。  

```Kotlin
override suspend fun create(
  content: String,
  attachmentList: List<File>
): Status = withContext(IO) {
  val username = mePreferences.getUsername() ?: throw AuthenticatorException()
  val statusJson = yatterApi.postStatus(
    getToken(username),
    PostStatusJson(
      content,
      emptyList()
    )
  )
  StatusConverter.convertToDomainModel(statusJson)
}

private fun getToken(username: String) = "username $username"
```

<details>
<summary>トークン生成箇所の追加課題(スキップ可)</summary>

今回の`StatusRepostiroyImpl#create`実装では、`getToken`というメソッドを用意して、`username ${ユーザー名}`という形式の文字列を生成するように実装しました。  

このままでも動作自体は問題ないのですが、コードや設計的に懸念が残ります。  
それは、他の箇所でも同様のトークンが必要になる場合があるからです。  

同じ`StatusRepositoryImpl`内であればこの`getToken`を使いまわせますが、`AccountRepositoryImpl`などの他のクラスで必要になった場合はまた実装が必要になってきます。  

その解決法方法として`TokenProvider`が作成されることがあります。  
他のクラスでもトークンが必要になったり手が空いたりしている方は実装に挑戦してみましょう。(とりあえず最後まで実装したい人はスキップしても問題ありません)  

実装例も記載しますがまずは自分で挑戦してみて回答を見たり参考にしたりしてください。  

---

`TokenProvider`は認証に関わる値ですのでそれ用のパッケージ`auth`を作成してその中に作成するとよいでしょう。  

ヒントとして、interfaceと利用方法を記載しておきますので、`TokenProviderImpl`の実装方法やDIの設定、テストなどは各自で書いてみましょう。  
`TokenProviderImpl`は`auth/impl`パッケージ内で作成してみましょう。  
詰まったりさらにヒントが欲しかったりしたら講師に聞いたりチームで相談したりして進めてください。  

interface定義  
```Kotlin
package com.dmm.bootcamp.yatter2023.auth

interface TokenProvider {
  suspend fun provide(): String
}
```

利用方法  
```Kotlin
class StatusRepositoryImpl(
  private val yatterApi: YatterApi,
  private val tokenProvider: TokenProvider,
) : StatusRepository {
  ...
  override suspend fun create(
    content: String,
    attachmentList: List<File>
  ): Status = withContext(IO) {
    val statusJson = yatterApi.postStatus(
      tokenProvider.provide(),
      PostStatusJson(
        content,
        listOf()
      )
    )
    StatusConverter.convertToDomainModel(statusJson)
  }
  ...
}
```

<details>
<summary>TokenProviderImpl実装例</summary>

auth/impl/TokenProviderImpl.kt  

```Kotlin
class TokenProviderImpl(private val getMeService: GetMeService) : TokenProvider {
  override suspend fun provide(): String {
    val me = getMeService.execute()
    return me?.username?.value?.let { "username $it" } ?: throw AuthenticatorException()
  }
}
```

di/InfraModule.kt  

```Kotlin
internal val infraModule = module {
  ...
  factory<TokenProvider> { TokenProviderImpl(get()) }
}
```

TokenProviderImplのテスト

```Kotlin
class TokenProviderImplSpec {
  private val getMeService = mockk<GetMeService>()
  private val subject = TokenProviderImpl(getMeService)

  @Test
  fun getTokenSuccess() = runTest {
    val username = "username"
    val me = MeImpl(
      id = AccountId(value = "id"),
      username = Username(value = username),
      displayName = null,
      note = null,
      avatar = URL("https:www.google.com"),
      header = URL("https:www.google.com"),
      followingCount = 0,
      followerCount = 0
    )
    
    val expect = "username $username"

    coEvery {
      getMeService.execute()
    } returns me
    
    val result = subject.provide()
    
    coVerify { 
      getMeService.execute()
    }
    
    assertThat(result).isEqualTo(expect)
  }
  
  @Test
  fun getTokenFailure() = runTest { 
    coEvery { 
      getMeService.execute()
    } returns null
    
    var error: Throwable? = null 
    var result: String? = null
    
    try {
      result = subject.provide()
    } catch (e: Exception) {
      error = e
    }

    coVerify {
      getMeService.execute()
    }
    
    assertThat(result).isNull()
    assertThat(error).isInstanceOf(AuthenticatorException::class.java)
  }
}
```

</details>

</details>

---

`create`メソッドが実装できたら単体テストを実施しましょう。  
次に示す項目のテストを書いてみてください。  

- ログイン済み時に呼び出すと投稿に成功する
- 未ログイン時に実行すると例外が発生する

<details>
<summary>StatusRepositoryImpl#createのテスト実装例</summary>

```Kotlin
@Test
fun postStatusWhenLoggedIn() = runTest {
  val username = "username"
  val content = "content"

  val statusJson = StatusJson(
    id = "id",
    account = AccountJson(
      id = "id",
      username = username,
      displayName = "",
      note = null,
      avatar = "",
      header = "",
      followingCount = 0,
      followersCount = 0,
      createAt = ""
    ),
    content = content,
    createAt = ""

  )

  coEvery {
    mePreferences.getUsername()
  } returns username

  coEvery {
    yatterApi.postStatus(any(), any())
  } returns statusJson

  val expect = StatusConverter.convertToDomainModel(statusJson)

  val result = subject.create(
    content,
    emptyList()
  )

  assertThat(result).isEqualTo(expect)

  coVerifyAll {
    mePreferences.getUsername()
    yatterApi.postStatus(
      username,
      PostStatusJson(
        status = content,
        mediaIds = emptyList()
      )
    )
  }
}

@Test
fun postStatusWhenNotLoggedIn() = runTest {
  val username = null
  val content = "content"

  coEvery {
    mePreferences.getUsername()
  } returns username


  var error: Throwable? = null
  var result: Status? = null

  try {
    result = subject.create(
      content,
      emptyList()
    )
  } catch (e: Exception) {
    error = e
  }


  assertThat(result).isNull()
  assertThat(error).isInstanceOf(AuthenticatorException::class.java)

  coVerify {
    mePreferences.getUsername()
  }
}
```

</details>

---

テストが通ることまで確認できたらinfra層の実装は完了です。  