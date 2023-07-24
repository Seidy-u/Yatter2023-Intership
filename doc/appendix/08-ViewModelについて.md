# ViewModelについて

ActivityやFragmentは、システムによって破棄・再生成が開発者の意図しないタイミングで発生することがあります。

その際、破棄されるタイミングで必要なデータを保持し再生成のタイミングで復元するような処理を実装しないと、画面の状態が初期化するなど、ユーザー体験を損ねてしまいます。

EditTextの紹介のときの例をもう一度見てみましょう。ボタンをクリックしたタイミングで入力したテキストをTextViewにセットしています。

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <EditText
        android:id="@+id/edit_text"
        android:layout_width="128dp"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />

    <TextView
        android:id="@+id/text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/edit_text"
        app:layout_constraintEnd_toEndOf="parent" />

    <Button
        android:id="@+id/change_text_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Set Text"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/text_view"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    val binding = FragmentFirstBinding.inflate(inflater, container, false)
    binding.changeTextButton.setOnClickListener {
        val text = binding.editText.text.toString()
        binding.textView.text = text
        binding.editText.setText("")
    }
    return binding.root
}
```

これでビルドし、一度適当な文字列を入力してボタンを押してセットしたあと、画面回転させてみましょう。

画面回転後はセットした文字列が消えているのがわかると思います。これは、画面回転でActivityとFragmentが破棄・再生成されているからです。

これを画面回転でもテキストを保持するようにするには、いくつか方法がありますがViewModelを使うと簡単にできます。

ViewModelは、UIに表示するデータに関する責務を持ちます。データの所有権をActivityやFragmentからViewModelに切り出すことにより、画面の破棄・再生性に対応するだけでなくロジックの複雑さを軽減させることにも繋がります。

## ViewModelの導入

ViewModel自体は特に依存を追加しなくても使えますが、Kotlinから使う上で便利になるKotlin拡張を導入しましょう。 `app/build.gradle` の `dependencies` ブロックに以下を追加します。

```gradle
dependencies {
    ...
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.5.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
}
```

## ViewModelをFragmentに追加する

先程の画面をViewModelを使う形に書き換えてみましょう。

まずはViewModelを実装します。

```kotlin
class MainViewModel : ViewModel() {
    private val _textStateFlow = MutableStateFlow("")
    val textStateFlow: StateFlow<String> = _textStateFlow.asStateFlow()

    fun setText(text: String) {
        viewModelScope.launch {
            _textStateFlow.update { text }
        }
    }
}
```

表示させるテキストを `StateFlow` で保持しています。 `MutableStateFlow` はprivateにし、publicなプロパティは変更不可能な `StateFlow` にしています。更新は `setText` メソッド経由で行うようにしています。

次に、Fragment側の実装です。

ViewModelのインスタンス生成や保持をよしなにやってくれる `viewModels()` という拡張関数がActivityとFragmentに提供されているので、それを使い今回実装したViewModelを取得します。

```kotlin
private val viewModel: MainViewModel by viewModels()
```

`onCreateView` でしていた、ボタンがクリックされた際にEditTextの内容をTextViewにセットするという処理を、ViewModel経由で行うようにします。

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
): View {
    val binding = FragmentFirstBinding.inflate(inflater, container, false)
    binding.changeTextButton.setOnClickListener {
        viewModel.setText(binding.editText.text.toString())
        binding.editText.setText("")
    }
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.textStateFlow.collect { text ->
                binding.textView.text = text
            }
        }
    }
    return binding.root
}
```

これでビルドして起動すると、画面回転してもテキストが消えないことが確認できると思います。

少し複雑になってしまいましたが、これは別の章で解説するDataBindingを組み合わせることによってもっと単純に書くことができます。
