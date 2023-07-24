# ログイン画面のDI実装
ログイン画面のDI実装を行います。  

基本的にはパブリックタイムライン画面実装で行なったことと同様です。  
ですが、ログイン画面では`UseCase`を実装したため、`di`パッケージ内に`UseCaseModule`ファイルを作成し、`UseCase`用のDIモジュールを新規作成します。  

UseCaseもシングルトンである必要はないため、`factory`を使って定義します。  

```Kotlin
val useCaseModule = module {
  // factory<FooUseCase> { FooUseCaseImpl() }
}
```

作成したDIモジュールはApplicationファイルの`modules`に追加します。  

```Kotlin
modules(
  modules = listOf(
    domainImplModule,
    infraModule,
    useCaseModule,
    viewModelModule,
  )
)
```

UseCase用のDIモジュールの用意ができたところでログイン画面実装時に追加したクラスを各層のDIモジュールに定義していきます。  

`DomainImplModule`に`LoginService`、`InfraModule`に`MePreferences`、`UseCaseModule`に`LoginUseCase`、`ViewModelModule`に`LoginViewModel`を定義してみましょう。  

```Kotlin
// DomainImplModule

val domainImplModule = module {
  ...
  factory<LoginService> { LoginServiceImpl(get()) }
  factory<CheckLoginService> { CheckLoginServiceImpl(get()) }
  ...
}

// InfraModule

val infraModule = module {
  ...
  single { MePreferences(get()) }
  ...
}

// UseCaseImplModule

val domainImplModule = module {
  ...
  factory<LoginUseCase> { LoginUseCaseImpl(get()) }
  ...
}

// ViewModelModule

val viewModelModule = module {
  ...
  viewModel { LoginViewModel(get()) }
  viewModel { MainViewModel(get()) }
  ...
}

```


これでDI層の実装も完了です。  

次の章で画面導線実装を行います。  