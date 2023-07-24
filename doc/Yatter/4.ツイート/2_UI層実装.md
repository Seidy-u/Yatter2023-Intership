# ツイート画面のUI層実装
ツイート画面のUI層実装を行います。

## BindingModelの実装
まずはBindingModelの実装からです。  
`ui/post`パッケージに`PostBindingModel`ファイルを作成し、定義します。  

ツイート画面で表示する内容としては、ツイート主のアイコンとツイート内容をBindingModelが持つようにします。  

```Kotlin
data class PostBindingModel(
  val avatarUrl: String?,
  val statusText: String,
)
```

## UiStateの実装
続いて、UiStateの実装です。  
UiStateではツイートの投稿内容`PostBindingModel`と読み込み中かを表す`isLoading`、そしてツイートの投稿ができるかどうかの`canPost`を実装します。  
`canPost`は`PostBindingModel#statusText`が空かどうかで判断するようにします。

```Kotlin
data class PostUiState(
  val bindingModel: PostBindingModel,
  val isLoading: Boolean,
) {
  companion object {
    fun empty(): PostUiState = PostUiState(
      bindingModel = PostBindingModel(
        avatarUrl = null,
        statusText = ""
      ),
      isLoading = false,
    )
  }

  val canPost: Boolean
    get() = bindingModel.statusText.isNotBlank()
}
```

## ViewModelの実装
ViewModelの実装に移ります。  
`ui/post`パッケージに`PostViewModel`クラスを作成しましょう。  
投稿用のUseCaseである`PostStatusUseCase`とログインユーザーの情報を取得する`getMeService`も引数に追加します。  

```Kotlin
class PostViewModel(
  private val postStatusUseCase: PostStatusUseCase,
  private val getMeService: GetMeService,
) : ViewModel() {}
```

次に、必要なメソッドを定義します。  
今回は画面の初期起動時にユーザー情報取得する`onCreate`とStatusの内容を書き換えた時に呼び出される`onChangedStatusText`、そして投稿ボタンを押下した時の`onClickPost`を用意します。  
さらにツイート画面ではパブリックタイムライン画面に戻るために表示するボタン押下時の`onClickNavIcon`も定義します。  


```Kotlin
class PostViewModel(...) : ViewModel() {
  fun onCreate() {}

  fun onChangedStatusText(statusText: String) {}

  fun onClickPost() {}

  fun onClickNavIcon() {}
}
```

ViewModel内のUiStateとパブリックタイムライン画面に戻るための`SingleLiveEvent`も定義しておきます。  

```Kotlin
class PostViewModel(...) : ViewModel() {
  private val _uiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.empty())
  val uiState: StateFlow<PostUiState> = _uiState

  private val _goBack: SingleLiveEvent<Unit> = SingleLiveEvent()
  val goBack: LiveData<Unit> = _goBack
}
```

`onCreate`でユーザー情報取得する部分から実装します。  
`GetMeService`でログイン済みのユーザーを取得し、必要なアバター画像の情報のみをUiStateに更新します。  
読み込みを行うため、`isLoading`も更新してローディング表示するようにしましょう。  

```Kotlin
fun onCreate() {
  viewModelScope.launch {
    _uiState.update { it.copy(isLoading = true) }

    val me = getMeService.execute()

    val snapshotBindingModel = uiState.value.bindingModel
    _uiState.update {
      it.copy(
        bindingModel = snapshotBindingModel.copy(avatarUrl = me?.avatar.toString()),
        isLoading = false,
      )
    }
  }
}
```

次は、入力された文字列にUiStateを更新するための`onChangedStatusText`の実装です。  

```Kotlin
fun onChangedStatusText(statusText: String) {
  _uiState.update { it.copy(bindingModel = uiState.value.bindingModel.copy(statusText = statusText)) }
}
```

続いては、投稿用のボタン押下時の`onClickPost`の実装です。  
ローディング表示を行い、投稿完了したら画面を戻るようにし失敗した場合はそのままにしておきます。  

```Kotlin
fun onClickPost() {
  viewModelScope.launch {
    _uiState.update { it.copy(isLoading = true) }

    val result = postStatusUseCase.execute(
      content = uiState.value.bindingModel.statusText,
      attachmentList = listOf()
    )
    when (result) {
      PostStatusUseCaseResult.Success -> {
        _goBack.value = Unit
      }

      is PostStatusUseCaseResult.Failure -> {
        // エラー表示
      }
    }
    _uiState.update { it.copy(isLoading = false) }
  }
}
```

最後に、戻る用のボタン押下時の`onClickNavIcon`です。  
このメソッドでは単純に、`goBack`に値を流すだけです。  

```Kotlin
fun onClickNavIcon() {
  _goBack.value = Unit
}
```

## UI構築
UI構築を行います。  
今までと同様にActivity・Page・Templateを作成します。  
パッケージは`ui/post`にします。  

- PostActivity
- PostPage
- PostTemplate

### Composeの実装
#### Templateの実装
Templateの実装から始めます。  
`PostTemplate`ファイルに`PostTemplate`コンポーザブルを作成し、プレビューコンポーザブルも定義します。  

```Kotlin
@Composable
fun PostTemplate() {

}

@Preview
@Composable
fun PostTemplatePreview() {
  Yatter2023Theme {
    Surface() {
      PostTemplate()
    }
  }
}
```

ツイート画面には次の要素が表示されます。  

- TopAppBarの表示
  - ページタイトル
  - 戻るボタン
- 投稿者のアバターアイコン
- 入力中のツイート(Status)内容
- 投稿用ボタン

これらの要素を順番に実装していきます。  

まずは、PostTemplateの引数を埋めてコンポーザブル実装中に利用できるようにします。  

引数には次の要素が考えられます。  

- 画面に表示する値
  - PostBindingModel
- ローディングインディケータ表示フラグ
  - Boolean
- 投稿可能フラグ
  - Boolean
- Status入力状況監視ラムダ
  - (String) -> Unit
- 投稿ボタン押下ラムダ
  - () -> Unit
- 戻るボタン押下ラムダ
  - () -> Unit

`PostTemplate`の引数にこれらを追加します。  

```Kotlin
@Composable
fun PostTemplate(
  postBindingModel: PostBindingModel,
  isLoading: Boolean,
  canPost: Boolean,
  onStatusTextChanged: (String) -> Unit,
  onClickPost: () -> Unit,
  onClickNavIcon: () -> Unit,
) {}
```

引数を追加できたらまずはTopAppBarの表示から行います。  
他ページ同様にScaffoldコンポーザブルを用意し、TopAppBarを表示します。  

```Kotlin
@Composable
fun PostTemplate(...) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "投稿")
        },
        navigationIcon = {
          IconButton(onClick = onClickNavIcon) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "戻る"
            )
          }
        }
      )
    }
  ) {}
}
```

`navigationIcon`が他の画面と比べて増えていると思います。  
この引数にコンポーザブルを渡すことによってTopAppBarのタイトル左横に表示することができます。  

今回は、`IconButton`というアイコン(画像)をボタンとして扱うことのできるコンポーザブルを呼び出し、アイコンに`Icons.Default.ArrowBack`を指定して戻るボタンを実装しています。  

続いては、投稿画面全体とローディングインディケータを表示するための`Box`コンポーザブルを用意して、最大サイズまで拡げコンテンツを画面中央に配置するように指定します。  

```Kotlin
Scaffold(...) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {}
}
```

それでは、アバターアイコン・ツイート内容・投稿ボタンを並べます。  
実装する画面のイメージを確認するとツイート内容と投稿ボタンが縦に並び、その2つとアバターアイコンが横に並んでいるように考えることができます。  

![post_template_preview](../image/4/post_template_preview.png)

そのため次のように横方向に並べる`Row`と縦方向に並べる`Column`を組みあせて画面を構成します。  

```Kotlin
Row(...) {
  Column(...) {
    ...
  }
}
```

この配置をもとに、アバターアイコン・ツイート内容・投稿ボタンを配置していきます。  

```Kotlin
Box(...) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .padding(it)
  ) {
    AsyncImage(
      modifier = Modifier.size(64.dp),
      model = postBindingModel.avatarUrl,
      contentDescription = "アバター画像",
      contentScale = ContentScale.Crop
    )

    Column(
      horizontalAlignment = Alignment.End,
    ) {
      TextField(
        modifier = Modifier
          .fillMaxWidth() // 横幅最大サイズ確保
          .weight(1f), // 他のコンポーザブルのサイズを確保した上で最大サイズを取る
        value = postBindingModel.statusText,
        onValueChange = onStatusTextChanged,
        colors = TextFieldDefaults.textFieldColors(
          backgroundColor = Color.Transparent,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
          disabledIndicatorColor = Color.Transparent,
        ), // TextFieldの枠を透明にするための設定
        placeholder = {
          Text(text = "今何してる？")
        },
      )
      Button(
        onClick = onClickPost,
        modifier = Modifier.padding(16.dp),
        enabled = canPost,
      ) {
        Text(text = "ツイート")
      }
    }
  }
}
```

最後にローディングインディケータを配置してTemplateの実装は終了です。  

```Kotlin
Box(...) {
  Row(...)

  if (isLoading) {
    CircularProgressIndicator()
  }
}
```

ここまで実装できたらプレビューを確認して実装できているか確認します。  


#### Pageの実装
Templateの実装が完了したら、Pageの実装に入ります。  
`PostPage`ファイルに`PostPage`コンポーザブルを定義します。  
引数として、ViewModelを受け取ります。  

```Kotlin
@Composable
fun PostPage(viewModel: PostViewModel) {
}
```

`PostPage`の定義ができたら、ViewModelからUiStateを取り出し、`PageTemplate`に適した引数を渡します。  

```Kotlin
@Composable
fun PostPage(viewModel: PostViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  PostTemplate(
    postBindingModel = uiState.bindingModel,
    isLoading = uiState.isLoading,
    canPost = uiState.canPost,
    onStatusTextChanged = viewModel::onChangedStatusText,
    onClickPost = viewModel::onClickPost,
    onClickNavIcon = viewModel::onClickNavIcon,
  )
}
```

### Activityの実装
Composeの実装ができたらActivityの実装に移ります。  
`PostActivity`ファイルに`PostActivity`クラスを定義し、`newIntent`メソッドの実装とViewModelのインスタンス化も行います。  

```Kotlin
class PostActivity: AppCompatActivity() {
  companion object {
    fun newIntent(context: Context): Intent = Intent(
      context,
      PostActivity::class.java,
    )
  }

  private val viewModel: PostViewModel by viewModel()
}
```

`onCreate`メソッドをオーバーライドし、`PostPage`コンポーザブルの呼び出しと`PostViewModel#onCreate`の呼び出し、`PostViewModel#goBack`の購読を行います。  
`goBack`の購読時は`finish`メソッドを呼び出すことで`PostActivity`を終了し前に開いていた画面を表示するようにします。  

```Kotlin
class PostActivity: AppCompatActivity() {
  ...

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      Yatter2023Theme {
        Surface {
          PostPage(viewModel = viewModel)
        }
      }
    }

    viewModel.onCreate()

    viewModel.goBack.observe(this) {
      finish()
    }
  }
}
```

`PostActivity`の実装ができたら`AndroidManifest.xml`へのActivityの定義も忘れずに行いましょう。  

```XML
<application ...>
  <activity .../>

  <activity
      android:name=".ui.post.PostActivity"
      android:exported="false" />
</application>

```

これでツイート機能画面のUI層実装は完了です。  
