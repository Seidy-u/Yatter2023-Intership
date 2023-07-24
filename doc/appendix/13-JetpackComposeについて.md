# Jetpack Composeとは
Jetpack ComposeとはAndroidアプリのネイティブUIをビルドする際に推奨されるAndroidの最新ツールキットです。  

Jetpack Composeを使うことによって次のようなメリットがあります。  
- 少ないコードで記述できる
- 簡潔に記述できるため不具合混入を防げる
- Kotlinで実装できる
- アプリの状態が更新されると自動でUI状態が更新される
  - UI更新忘れが発生しづらい
- 開発速度の向上
- 直観的

などさまざまなメリットがあります。  

Jetpack Composeでは宣言的UI構築方法です。

## コンポーザブル
コンポーザブルと呼ばれる関数を実装・組み合わせることで期待したUIを構築していきます。  
コンポーザブルには次のような特徴があります。  

- @Composableアノテーションのついたメソッド
- 関数の引数でそのコンポーザブルに利用する値を受け取る
- コンポーザブル関数内で他のコンポーザブル関数を呼び出して構築される
- コンポーザブル関数は値を返さず、呼ばれることによってUIに直接描画される
- 同じ引数が渡されたら何度呼び出されても同じ見た目を出力するような冪等性を意識する
- アプリの状態に応じて何度も呼び出される

基本的なコンポーザブルはすでに定義されており、開発者はそのコンポーザブルを組み合わせてUIを構築します。  
代表的なコンポーザブルには次のようなものがあります。  

- Text：テキストを表示する
- Button：ボタンを表示する
- TextField：テキストを入力する
- Column：縦方向にコンポーザブルを並べる
- Row：横方向にコンポーザブルを並べる
- Box：コンポーザブルを重ねる
- などなど

コンポーザブルは厳密には2種類あります。
`Text`や`Button`、`TextField`などの呼び出すことでUIを描画するものと、`Column`や`Row`、`Box`のように渡されたコンポーザブルをレイアウトするものです。  

実装する上で特に違いを意識する必要はありませんがどちらもコンポーザブルと呼ばれていることに注意してください。  

## Modifier
Jetpack ComposeでUIを構築する上で、サイズ・レイアウト・動作・外観を変更したいときや要素をクリック可能・スクロール可能・ドラッグ可能・ズーム可能にするなど、高レベルの操作を追加したい場合があります。  

その時にはModifierと呼ばれるCompose修飾子を利用してコンポーザブルを装飾・拡張することができます。  

デフォルトで実装されているコンポーザブルは全てModifierを引数に受け取れるようになっています。（受け取らないコンポーザブルもあるかもしれないですが、思想的には全てのコンポーザブルは受け取れるようにするべきとなっています）

次のようにModifierをコンポーザブルの引数に渡すことで装飾・拡張できます。  

```Kotlin
Text(
  text = "Hello, Jetpack Compose",
  modifier = Modifier
    .padding(20.dp)
    .background(color = Color.Cyan)
)
```

先ほどの例のように、Modifierは`.`で繋いで、いくつも連鎖することができます。  
この連鎖する順番にも意味があり、次のドキュメントがアニメーション付きでわかりやすくなっているためご一読ください。  
https://developer.android.com/jetpack/compose/modifiers?hl=ja#order-modifier-matters

この順番が意識できていないと思ったような挙動・表示をしない場合がありますので意図してない表示になったら順番を考え直してみてください。  


## 状態の管理
Jetpack ComposeでUIを構築する上で**状態**という言葉がよく登場します。  
アプリにおける状態とは、時間によって変化する可能性のある値全てを指しています。  
SNSアプリの投稿一覧もアプリの設定もアプリに入力するテキストも全て状態と呼べます。  

Jetpack Composeではこの状態をどこでどのように保存し、使用するか明示的に定義することができます。

Jetpack Composeにおいて状態はコンポーザブルに引数で渡される値やコンポーザブル内で保持している変数を指します。  
前述したようにコンポーザブルは冪等性があるため、表示される見た目を変更したいときは引数を変える必要があり、この変化する値が状態です。  
状態が更新される(=引数が更新される)と、コンポーザブルは更新された引数を利用しているコンポーザブルを全て再実行します。  
この再実行を再コンポーザブルと言います。  
再実行することによって新しい状態を用いたUIが描画されます。  

再コンポーザブルは引数を変更することによって自動で行われるため、アプリ開発者はコンポーザブルに渡す状態を変化させるだけで見た目も更新することができます。  

Jetpack Composeにおいて状態とは`State`クラスでオブザーバブルなオブジェクトを指します。  
このStateクラスが更新されたことをCompose側で検知して再コンポーズを実施します

Stateは次のように定義することができます。  
定義方法によって更新・参照のさせ方が違いますが、同じ意味合いを持ちます。  
作成するコンポーザブルによって使い分けることができます。  

```Kotlin
// 1
val mutableState = remember { mutableStateOf(default) }
// 更新
mutableState.value = newValue
// 参照
mutableState.value

// 2
var value by remember { mutableStateOf(default) }
// 更新
value = newValue
// 参照
value

// 3
val (value, setValue) = remember { mutableStateOf(default) }
// 更新
setValue(newValue)
// 参照
value
```

また、LiveDataやFlowの対応もされており、次のようにStateに変換することができます。  
今回は主に`collectAsStateWithLifecycle`の方を利用します。  

```Kotlin
// LiveData
val value by liveDataValue.observeAsState()

// Flow
val value by flowValue.collectAsStateWithLifecycle()
```

ここまでが、基本的な概念に関しての説明となります。  

詳細な概念の説明や使い方等は公式のドキュメント(日本語)で簡潔に説明されているため合わせてご確認ください。  
https://developer.android.com/jetpack/compose?hl=ja
