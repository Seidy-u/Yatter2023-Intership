# パブリックタイムラインのテスト実装
パブリックタイムラインで使用しているモジュールのテストを書いてみましょう。  

## ViewModelの単体テスト

ViewModelにもテストを書きます。  
特にViewModelでは、ユーザーが起こしたアクションに対して処理をして結果をUIに反映するというアプリにおいて重要な役割を担うことが多いため単体テストもしっかり書きます。  

まずは、テストファイルをinfra層実装時と同様に作成します。  
同時にテスト対象クラスのインスタンス化も行います。  

```Kotlin
class PublicTimelineViewModelSpec {
  private val statusRepository = mockk<StatusRepository>()
  private val subject = PublicTimelineViewModel(statusRepository)
}
```

続いて、ViewModelテスト時用の設定を行います。  
事前に定義してある、`MainDispatcherRule`を用いてViewModelの単体テストを実行できるようにします。  

このルールが必要な背景としては、ViewModelで利用している`viewModelScope`にあります。  
`viewModelScope`はViewModelのライフサイクルに合わせてメインディスパッチャーで処理を実行します。(ディスパッチャーは処理を実行するスレッドの環境くらいの理解で大丈夫です)  
このメインディスパッチャーは実際のアプリケーションでは存在する環境ですが、AndroidのUnitTest環境では存在しません。  
そのため、`viewModelScope`を利用した処理のテストが実施できなくなるため、テスト用の環境に切り替えるためのルールを用いてテストを実施します。  

ルールの定義は次のように行います。  

```Kotlin
class PublicTimelineViewModelSpec {
  ...

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
}
```

このように定義するだけで切り替えが実施されます。  

あとはinfra層で書いたテストと同じように書きます。  

- メソッドのモック化
- テスト対象の実行
- モックしたメソッドの実行確認
- 実行結果の確認

```Kotlin
@Test
fun getPublicTimelineFromRepository() = runTest {
  val statusList = listOf(
    Status(
      id = StatusId(value = "id"),
      account = AccountImpl(
        username = Username("username"),
        displayName = "display name",
        note = "note",
        avatar = null,
        header = null,
        followingCount = 100,
        followerCount = 200
      ),
      content = "content",
      attachmentMediaList = listOf()
    )
  )

  val expect = StatusConverter.convertToBindingModel(statusList)

  coEvery {
    statusRepository.findAllPublic()
  } returns statusList

  subject.onResume()

  coVerify {
    statusRepository.findAllPublic()
  }

  assertThat(subject.uiState.value.statusList).isEqualTo(expect)
}
```

`onRefresh`のテストも追加します。  

```Kotlin
@Test
fun onRefreshPublicTimeline() = runTest {
  val statusList = listOf(
    Status(
      id = StatusId(value = "id"),
      account = AccountImpl(
        username = Username("username"),
        displayName = "display name",
        note = "note",
        avatar = null,
        header = null,
        followingCount = 100,
        followerCount = 200
      ),
      content = "content",
      attachmentMediaList = listOf()
    )
  )

  val expect = StatusConverter.convertToBindingModel(statusList)

  coEvery {
    statusRepository.findAllPublic()
  } returns statusList

  subject.onRefresh()

  coVerify {
    statusRepository.findAllPublic()
  }

  assertThat(subject.uiState.value.statusList).isEqualTo(expect)
}
```
