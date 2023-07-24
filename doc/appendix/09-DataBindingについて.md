# DataBindingについて

画面の要素を取得する章で少しだけ `DataBinding` に触れましたが、それについてもう少し解説します。

DataBindingを使うと、例えばActivityの `onCreate` やFragmentの `onCreateView` で画面の要素を取得して値をセットするといった処理を書かずに、データをUIに反映させることができます。

前の章で実装したものを、DataBindingを使う形に書き換えてみましょう。

FragmentのレイアウトのXMLのルートの要素を `layout` にし、 `data` 要素を追加して `viewModel` の `variable` を持たせましょう。

TextViewに `android:text` 属性を指定しますが、値に `"@{viewModel.textStateFlow}"` を指定しています。これにより、StateFlowの更新が自動でこのTextViewに反映されて表示されます。

ボタンのクリックリスナーもDataBindingで定義することができます。ラムダ式が使えるので、それを `android:onClick` 属性に渡しています。DataBindingでは、バインディングの式の中でXMLの要素を参照することもできます。今回は、 `edit_text` のidのEditTextをラムダ式で参照し、入力された文字列を取得しています。バインディング式でXMLの要素を参照する際には、idがsnake_caseの場合もcamelCaseに変換されるので、 `editText` で参照しています。

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="viewModel"
            type="com.dmm.playground.MainViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
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
            android:text="@{viewModel.textStateFlow}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/edit_text"
            app:layout_constraintEnd_toEndOf="parent" />

        <Button
            android:id="@+id/change_text_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Set Text"
            android:onClick="@{() -> viewModel.setText(editText.getText().toString())}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/text_view"
            app:layout_constraintEnd_toEndOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

これにより、Fragmentの `onCreateView` でしていた処理がかなり短くなりました。 `binding` にViewModelをセットするだけで、あとはDataBindingがUIに反映してくれます。

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
): View {
    val binding = FragmentFirstBinding.inflate(inflater, container, false)
    binding.viewModel = viewModel
    binding.lifecycleOwner = viewLifecycleOwner
    return binding.root
}
```
