# ImageViewでネットワークの画像を表示する

ImageViewは、プロジェクトの画像リソースやデバイスのローカルに保存してある画像を表示するだけでなく、ライブラリを使うと簡単にネットワーク上の画像を表示することもできます。

いくつかライブラリがありますが、今回はCoilというライブラリを使用します。

## INTERNETパーミッションの追加

ネットワーク上から画像を取得するために、 `AndroidManifest.xml` にインターネット接続を許可するパーミッションを追加する必要があります。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    ...
</manifest>
```

## Coilの導入

`app/build.gradle` の `dependencies` ブロックに以下を追加します。

```gradle
dependencies {
  ...
  implementation("io.coil-kt:coil:2.4.0") // 追加
}
```

## Coilでネットワークの画像を表示する

今回はImageViewを1つもつFragmentを例にします。

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/icon_image_view"
        android:layout_width="300dp"
        android:layout_height="wrap_content"
        android:contentDescription="icon"
        android:scaleType="fitCenter"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
): View {
    val binding = FragmentFirstBinding.inflate(inflater, container, false)
    binding.iconImageView.load("https://avatars.githubusercontent.com/u/39693306")
    return binding.root
}
```

`Coil` は `ImageView` に `load` という拡張関数を提供しています。画像のURLを渡して呼び出すことで、目的のImageViewに画像を表示させることができます。

URLを自分の好きな画像のものに置き換えて実行してみましょう。
