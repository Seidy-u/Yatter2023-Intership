# ツイート画面のusecase層実装

ツイート画面実装に必要なUseCaseの実装を行います。  
今回必要なUseCaseはツイートを行う`PostStatusUseCase`のみですので、次のファイルを作成しましょう。  

- usecase/post/PostStatusUseCase.kt
- usecase/post/PostStatusUseCaseResult.kt
- usecase/impl/post/PostStatusUseCaseImpl.kt

## UseCaseReusltの定義
まずは、UseCaseResultの定義を行います。  
今回想定するUseCaseの実行結果は次のようになります。  

- 成功
- 失敗
  - 投稿内容が空
  - ログインしていない
  - その他のエラー

これらの実行結果を`UseCaseResult`ととして定義すると次のようになります。  

```Kotlin
sealed interface PostStatusUseCaseResult {
  object Success : PostStatusUseCaseResult
  sealed interface Failure : PostStatusUseCaseResult {
    object EmptyContent : Failure
    object NotLoggedIn : Failure
    data class OtherError(val throwable: Throwable) : Failure
  }
}
```

## UseCase interfaceの定義
UseCaseResultの定義ができたら、UseCaseのinterfaceを定義します。  

```Kotlin
interface PostStatusUseCase {
  suspend fun execute(
    content: String,
    attachmentList: List<File>
  ): PostStatusUseCaseResult
}
```

## UseCaseの実装
続いてUseCaseの実装に入ります。  

`PostStatusUseCase`では次のような処理を行います。  

- 投稿内容が文字列もメディアも無い時は`EmptyContent`
- `StatusRepository`で投稿する
- 投稿時に例外が発生しなければ`Success`
- 投稿時に`AuthenticatorException`が発生すると`NotLoggedIn`
- 投稿時に`AuthenticatorException`以外の例外が発生すると`OtherError`

実際にUseCaseの実装をしてみましょう。  

実装してみたら実装例と比較してみて問題なさそうか確認してください。  
もちろん、実装例とコードが同じである必要はなく、動作に問題なければ大丈夫です。  

<details>
<summary>PostStatusUseCaseImplの実装例</summary>

```Kotlin
class PostStatusUseCaseImpl(
  private val statusRepository: StatusRepository,
) : PostStatusUseCase {
  override suspend fun execute(
    content: String,
    attachmentList: List<File>
  ): PostStatusUseCaseResult {
    if (content == "" && attachmentList.isEmpty()) {
      return PostStatusUseCaseResult.Failure.EmptyContent
    }

    return try {
      statusRepository.create(
        content = content,
        attachmentList = emptyList(),
      )

      PostStatusUseCaseResult.Success
    } catch (e: AuthenticatorException) {
      PostStatusUseCaseResult.Failure.NotLoggedIn
    } catch (e: Exception) {
      PostStatusUseCaseResult.Failure.OtherError(e)
    }
  }
}
```

</details>


---

UseCaseの実装ができたら単体テストを書きます。

テスト用の`usecase/impl`に`PostStatusUseCaseImplSpec`ファイルを作成し、次の項目のテストを書いて実行してみましょう。  

- ログイン済みで投稿内容も空でなければ投稿に成功する
- 投稿内容が空の場合は、対応した失敗になる
- 未ログインであれば、対応した失敗になる
- 何かしらのエラーが発生したら、対応した失敗になる

テストが無事に通過すればUseCaseの実装も完了です。  

<details>
<summary>PostStatusUseCaseImplのテスト実装例</summary>

```Kotlinclass PostStatusUseCaseImplSpec {
  private val statusRepository = mockk<StatusRepository>()
  private val subject = PostStatusUseCaseImpl(statusRepository)

  @Test
  fun postStatusWithSuccess() = runTest {
    val content = "content"

    val status = Status(
      id = StatusId(value = ""),
      account = MeImpl(
        id = AccountId(value = ""),
        username = Username(value = ""),
        displayName = null,
        note = null,
        avatar = URL("https://www.google.com"),
        header = URL("https://www.google.com"),
        followingCount = 0,
        followerCount = 0
      ),
      content = content,
      attachmentMediaList = listOf(),
    )

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } returns status

    val result = subject.execute(
      content,
      emptyList(),
    )

    coVerify {
      statusRepository.create(
        content,
        emptyList(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Success)
  }

  @Test
  fun postStatusWithEmptyContent() = runTest {
    val content = ""

    val result = subject.execute(
      content,
      emptyList(),
    )

    coVerify(inverse = true) {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.EmptyContent)
  }

  @Test
  fun postStatusWithNotLoggedIn() = runTest {
    val content = "content"

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } throws AuthenticatorException()

    val result = subject.execute(
      content,
      emptyList(),
    )


    coVerify {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.NotLoggedIn)
  }

  @Test
  fun postStatusWithOtherError() = runTest {
    val content = "content"
    val exception = Exception()

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } throws exception

    val result = subject.execute(
      content,
      emptyList(),
    )


    coVerify {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.OtherError(exception))
  }
}
```

</details>
