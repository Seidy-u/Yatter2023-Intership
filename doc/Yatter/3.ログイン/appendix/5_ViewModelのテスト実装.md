# ログイン画面のテスト実装
ログイン画面でしているモジュールのテストを書いてみましょう。  

## LoginViewModelのテスト
`PublicTimelineViewModel`の単体テストを参考にしてみてください。  

テストする項目は次のようになります。  
- ユーザー名を変更したときにUiStateに反映されるか
- パスワードを変更したときにUiStateに反映されるか
- 入力したユーザー名とパスワードが有効な値になっているか
- ログインボタンを押してログイン成功した場合
- ログインボタンを押してログイン失敗した場合
- 登録ボタンを押したとき

ヒントとして、LiveDataを単体テストで扱う場合は次のルールを追加します。  

```Kotlin
@get:Rule
val rule: TestRule = InstantTaskExecutorRule()
```

<details>
<summary>LoginViewModelのテスト例</summary>

```Kotlin
class LoginViewModelSpec {
  private val loginUseCase = mockk<LoginUseCase>()
  private val subject = LoginViewModel(loginUseCase)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun changeUsername() = runTest {
    val newUsername = "newUsername"

    subject.onChangedUsername(newUsername)

    assertThat(subject.uiState.value.loginBindingModel.username).isEqualTo(newUsername)
    assertThat(subject.uiState.value.validUsername).isTrue()
  }

  @Test
  fun changePasswordValid() = runTest {
    val newPassword = "newPassword1$"

    subject.onChangedPassword(newPassword)

    assertThat(subject.uiState.value.loginBindingModel.password).isEqualTo(newPassword)
    assertThat(subject.uiState.value.validPassword).isTrue()
  }

  @Test
  fun changePasswordInvalid() = runTest {
    val newPassword = "newPassword"

    subject.onChangedPassword(newPassword)

    assertThat(subject.uiState.value.loginBindingModel.password).isEqualTo(newPassword)
    assertThat(subject.uiState.value.validPassword).isFalse()
  }

  @Test
  fun clickLoginAndNavigatePublicTimeline() = runTest {
    val username = "username"
    val password = "Password1$"

    subject.onChangedUsername(username)
    subject.onChangedPassword(password)

    coEvery {
      loginUseCase.execute(any(), any())
    } returns LoginUseCaseResult.Success

    subject.onClickLogin()

    coVerify {
      loginUseCase.execute(Username(username), Password(password))
    }

    assertThat(subject.navigateToPublicTimeline.value).isNotNull()
    assertThat(subject.navigateToRegister.value).isNull()
  }

  @Test
  fun clickLoginAndFailure() = runTest {
    val username = "username"
    val password = "Password1$"

    subject.onChangedUsername(username)
    subject.onChangedPassword(password)

    coEvery {
      loginUseCase.execute(any(), any())
    } returns LoginUseCaseResult.Failure.OtherError(Exception())

    subject.onClickLogin()

    coVerify {
      loginUseCase.execute(Username(username), Password(password))
    }

    assertThat(subject.navigateToPublicTimeline.value).isNull()
    assertThat(subject.navigateToRegister.value).isNull()
  }

  @Test
  fun clickRegisterAndNavigate() = runTest {
    subject.onClickRegister()

    assertThat(subject.navigateToRegister.value).isNotNull()
    assertThat(subject.navigateToPublicTimeline.value).isNull()
  }
}
```

</details>

---

## MainViewModelのテスト
`MainViewModel`のテストは次の観点で確認します。  

- ログイン済みであれば、パブリックタイムライン画面への遷移
- ログイン済みでなければ、ログイン画面への遷移

<details>
<summary>MainViewModelのテスト実装例</summary>

```Kotlin
class MainViewModelSpec {
  private val checkLoginService = mockk<CheckLoginService>()
  private val subject = MainViewModel(checkLoginService)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun navigateToPublicTimelineWhenLoggedIn() = runTest {
    coEvery {
      checkLoginService.execute()
    } returns true

    subject.onCreate()

    assertThat(subject.navigateToPublicTimeline.value).isNotNull()
    assertThat(subject.navigateToLogin.value).isNull()
  }

  @Test
  fun navigateToLoginWhenNotLoggedIn() = runTest {
    coEvery {
      checkLoginService.execute()
    } returns false

    subject.onCreate()

    assertThat(subject.navigateToLogin.value).isNotNull()
    assertThat(subject.navigateToPublicTimeline.value).isNull()
  }
}
```

</details>

---