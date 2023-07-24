# ツイート画面のDI実装
ツイート画面のDI実装をして、これまで実装した各クラスのつなぎ込みを行い、ツイート画面を動作させるようにします。  

ツイート画面用に新規作成したDI実装を行う対象は次になります。  

- `GetMeService`
- `AccountRepository`
- `PostStatusUseCase`
- `PostViewModel`

それぞれ的したモジュールにインスタンス化方法を定義します。  

```Kotlin
// DomainImplModule
internal val domainImplModule = module {
  single<AccountRepository> { AccountRepositoryImpl(get(), get()) }
  ...

  factory<GetMeService> { GetMeServiceImpl(get()) }
}

// UseCaseModule
internal val useCaseModule = module {
  factory<PostStatusUseCase> { PostStatusUseCaseImpl(get()) }
}

// ViewModelModule
internal val viewModelModule = module {
  viewModel { PostViewModel(get(), get()) }
}
```

これでツイート画面実装は完了になります。  

次にツイート画面までの導線を実装します。
