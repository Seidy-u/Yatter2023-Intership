# Composable関数について

Jetpack ComposeでUIを構築する際に基本となるのがComposable関数です。

Composable関数は、Kotlinの関数に `@Composable` アノテーションを付与したものです。

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}
```

`Text` や `Button`, `Column` や `Row` などのJetpack Composeで提供されているUIのコンポーネントはすべてComposable関数です。

Composable関数はComposable関数の中からしか呼び出すことができないという特徴があります。

`MainActivity` の `onCreate` を見てみましょう。 `BasicTheme`, `Surface`, `Greeting` はComposable関数です。 

```kotlin
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BasicTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("Android")
        }
      }
    }
  }
}
```

`setContent` 内でそれらを呼び出していますが、これは `setContent` 関数が引数にComposable関数を受け取るからです。これにより、 `setContent { }` の　`{ }` の中がComposable関数となり、その中で別のComposable関数を呼び出すことができます。

```kotlin
public fun ComponentActivity.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit
)
```

試しに `setContent` の呼び出しをやめて、 `onCreate` の中で直接Composable関数を呼び出すようにしてみましょう。

```kotlin
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    BasicTheme {
      // A surface container using the 'background' color from the theme
      Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Greeting("Android")
      }
    }
  }
}
```

この状態だと画面にエラーが表示されると思います。エラーメッセージを読むと

```
@Composable invocations can only happen from the context of a @Composable function
```

と書いてあります。Composable関数はComposable関数の中からでしか呼び出せないという内容が書いてありますね。

もとに戻すとエラーは消えます。Jetpack Composeでアプリを実装する際には、Composable関数をComposable関数以外から呼び出さないように注意しましょう。

```kotlin
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BasicTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("Android")
        }
      }
    }
  }
}
```
