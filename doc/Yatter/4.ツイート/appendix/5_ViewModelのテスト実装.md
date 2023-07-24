# ViewModelのテスト実装
ツイート画面で利用しているViewModelのテストを書いてみましょう。

`PostViewModel`のテストを実装します。  
今回のテストは次の観点を確認します。  

- onCreate時にユーザー情報が取得できていること
- テキスト入力するとUiStateが更新されること
- 投稿ボタン押下で投稿完了すること
- 投稿ボタン押下でエラー発生時に何も発生しないこと
- ナビゲーションの戻るボタン押下時に戻る用の値が流れていること

実際にテストを書いてみて、テストコード例も載せていますので見比べながら動作を確認しましょう。

<details>
<summary>PostViewModelのテストコード例</summary>

```Kotlin
class PostViewModelSpec {
  private val getMeService = mockk<GetMeService>()
  private val postStatusUseCase = mockk<PostStatusUseCase>()
  private val subject = PostViewModel(
    postStatusUseCase,
    getMeService,
  )

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun getMeWhenOnCreate() = runTest {
    val avatarUrl = URL("https://www.dmm.com")
    val me = MeImpl(
      id = AccountId(value = "me account"),
      username = Username(value = ""),
      displayName = null,
      note = null,
      avatar = avatarUrl,
      header = URL("https://www.google.com"),
      followingCount = 0,
      followerCount = 0
    )
    coEvery {
      getMeService.execute()
    } returns me

    subject.onCreate()

    assertThat(subject.uiState.value.bindingModel.avatarUrl).isEqualTo(avatarUrl.toString())
  }


  @Test
  fun changeStatusAndCanPost() = runTest {
    val newStatusText = "new"

    subject.onChangedStatusText(newStatusText)

    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(newStatusText)
    assertThat(subject.uiState.value.canPost).isTrue()
  }

  @Test
  fun changeStatusAndCannotPost() = runTest {
    val oldStatusText = "old"
    val newStatusText = ""

    subject.onChangedStatusText(oldStatusText)
    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(oldStatusText)
    assertThat(subject.uiState.value.canPost).isTrue()

    subject.onChangedStatusText(newStatusText)

    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(newStatusText)
    assertThat(subject.uiState.value.canPost).isFalse()
  }

  @Test
  fun postSuccess() = runTest {
    val status = "status"
    subject.onChangedStatusText(status)

    coEvery {
      postStatusUseCase.execute(any(), any())
    } returns PostStatusUseCaseResult.Success

    subject.onClickPost()

    coVerify {
      postStatusUseCase.execute(status, emptyList())
    }

    assertThat(subject.goBack.value).isNotNull()
  }

  @Test
  fun postFailure() = runTest {
    val status = "status"
    subject.onChangedStatusText(status)

    coEvery {
      postStatusUseCase.execute(any(), any())
    } returns PostStatusUseCaseResult.Failure.OtherError(Exception())

    subject.onClickPost()

    coVerify {
      postStatusUseCase.execute(status, emptyList())
    }

    assertThat(subject.goBack.value).isNull()
  }

  @Test
  fun clickBack() = runTest {
    subject.onClickNavIcon()

    assertThat(subject.goBack.value).isNotNull()
  }
}
```

</details>