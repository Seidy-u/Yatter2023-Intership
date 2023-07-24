# Gradleについて

Androidアプリ開発をはじめ、Kotlinでの開発ではビルドツールにGradleが使われています。

Gradleのビルドスクリプトは、GroovyもしくはKotlinで記述します。ファイルの拡張子が `.gradle` の場合はGroovyで `.gradle.kts` の場合はKotlinになります。2023年現在ではAndroid Studioで生成されるデフォルトはGroovyのほうになっています。

Gradleはマルチモジュールをサポートしています。Androidのプロジェクトを作成すると、ルートディレクトリとappディレクトリにそれぞれ `build.gradle` が生成されると思いますが、これは `app` というモジュールがルートモジュールのサブモジュールとして定義されているからです。

## ルートのbuild.gradle

Yatterのルートの `build.gradle` を見てみましょう。

```gradle
plugins {
    id 'com.android.application' version '8.0.0' apply false
    id 'com.android.library' version '8.0.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.20' apply false
}
```

Gradleは汎用的なビルドツールなので、開発には目的にあったプラグインを導入する必要があります。

ここではAndroidアプリ開発に必要なAndroidのプラグインとKotlinのプラグインを導入しています。

`apply false` としているのは、実際にこれらのプラグインを使うのはルートモジュールではなく `app` などのサブモジュールだからで、その使わないプラグインは `apply` するとエラーになるためこうしています。使う側ではなくルートモジュールでプラグインの定義をするメリットとしては、導入するバージョンの指定をここですることにより、使う側のモジュールではバージョンの指定がいらなくなることなどがあります。

## app/build.gradle

`app/build.gradle` を見てみましょう。

```gradle
plugins {
    id "com.android.application"
    id "org.jetbrains.kotlin.android"
}

android {
    namespace "com.dmm.bootcamp.yatter2023"
    compileSdk 33

    defaultConfig {
        applicationId "com.dmm.bootcamp.yatter2023"
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary true
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose true
        buildConfig true
    }
    composeOptions {
        kotlinCompilerExtensionVersion "1.4.6"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation "androidx.core:core-ktx:1.10.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-runtime-compose:2.6.1"
    implementation "androidx.activity:activity-compose:1.7.2"
    implementation "com.google.android.material:material:1.9.0"
    implementation platform("androidx.compose:compose-bom:2023.04.01")
    implementation "androidx.compose.ui:ui"
    implementation "androidx.compose.ui:ui-graphics"
    implementation "androidx.compose.ui:ui-tooling-preview"
    implementation "androidx.compose.material:material"
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    implementation "com.squareup.moshi:moshi:1.14.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.14.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.10.0"
    implementation "io.insert-koin:koin-core:3.4.0"
    implementation "io.insert-koin:koin-android:3.4.0"
    implementation "io.coil-kt:coil-compose:2.3.0"
    implementation "androidx.core:core-splashscreen:1.0.1"

    testImplementation "junit:junit:4.13.2"
    testImplementation "io.mockk:mockk:1.13.5"
    testImplementation "com.google.truth:truth:1.1.3"
    testImplementation "org.jetbrains.kotlin:kotlin-test"
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test"
    testImplementation "androidx.arch.core:core-testing:2.2.0"
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
    androidTestImplementation platform("androidx.compose:compose-bom:2022.10.00")
    androidTestImplementation "androidx.compose.ui:ui-test-junit4"
    debugImplementation "androidx.compose.ui:ui-tooling"
    debugImplementation "androidx.compose.ui:ui-test-manifest"
}
```

### plugins

`plugins` ブロックを見てみましょう。ルートモジュールでバージョンの指定をしているため、ここではバージョンの指定を行っていません。

`app` モジュールは実際にアプリを起動するコードが記述されています。 `app` モジュールには `com.android.application` のプラグインを導入します。アプリの起動を含まないサブモジュールには `com.android.library` を指定します。

```gradle
plugins {
    id "com.android.application"
    id "org.jetbrains.kotlin.android"
}
```

### android

`android` ブロックを見てみましょう。

```gradle
android {
    namespace "com.dmm.bootcamp.yatter2023"
    compileSdk 33

    defaultConfig {
        applicationId "com.dmm.bootcamp.yatter2023"
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary true
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose true
        buildConfig true
    }
    composeOptions {
        kotlinCompilerExtensionVersion "1.4.6"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
```

重要なものについて解説します。

`compileSdk` は、開発に利用したいAndroid SDKのバージョンを指定します。基本的には最新のバージョンを指定します。

`minSdk` は、サポートする最小のAndroidのバージョンを指定します。この場合は24なのでAndroid 7.0をサポートしています。

`targetSdk` は実行時にどのSdkのバージョンで動かすかを指定します。基本的には `compileSdk` と同じでいいですが、例えば最新のSDKの対応が完了しきっていない場合に `targetSdk` のみバージョンを前のものに指定すると、Android 13でもAndroid 12のときのように動かすといったことができます。

`versionCode` と `versionName` は、アプリ自体のバージョンを定義します。 `versionCode` は数値で、 `versionName` は文字列で指定します。 `versionCode` はリリースごとにインクリメントする必要があります。 `versionName` は基本的に自由ですが、セマンティックバージョニングでつけることが多いです。

`buildTypes` はビルドの種類ごとにビルドの設定を変えることができます。デフォルトで `debug` と `release` が定義されており、この例だと `release` ビルドの場合の設定をしています。 `minifyEnabled` を `true` にすると、使用されていないコードの削除や難読化、最適化がされます。 `proguardFiles` はその設定ファイルです。

`composeOptions` の `kotlinCompilerExtensionVersion` は、Jetpack Composeのcompose-compilerのバージョンの指定をしています。Jetpack Composeはruntimeとcompilerのバージョンが別れているので、compilerのバージョンはここで指定します。

### dependencies

`dependencies` ブロックを見ます。

```gradle
dependencies {

    implementation "androidx.core:core-ktx:1.10.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-runtime-compose:2.6.1"
    implementation "androidx.activity:activity-compose:1.7.2"
    implementation "com.google.android.material:material:1.9.0"
    implementation platform("androidx.compose:compose-bom:2023.04.01")
    implementation "androidx.compose.ui:ui"
    implementation "androidx.compose.ui:ui-graphics"
    implementation "androidx.compose.ui:ui-tooling-preview"
    implementation "androidx.compose.material:material"
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    implementation "com.squareup.moshi:moshi:1.14.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.14.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.10.0"
    implementation "io.insert-koin:koin-core:3.4.0"
    implementation "io.insert-koin:koin-android:3.4.0"
    implementation "io.coil-kt:coil-compose:2.3.0"
    implementation "androidx.core:core-splashscreen:1.0.1"

    testImplementation "junit:junit:4.13.2"
    testImplementation "io.mockk:mockk:1.13.5"
    testImplementation "com.google.truth:truth:1.1.3"
    testImplementation "org.jetbrains.kotlin:kotlin-test"
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test"
    testImplementation "androidx.arch.core:core-testing:2.2.0"
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
    androidTestImplementation platform("androidx.compose:compose-bom:2022.10.00")
    androidTestImplementation "androidx.compose.ui:ui-test-junit4"
    debugImplementation "androidx.compose.ui:ui-tooling"
    debugImplementation "androidx.compose.ui:ui-test-manifest"
}
```

`implementation` でそのGradleモジュールにライブラリの依存を追加することができます。

`testImplementation` と `androidTestImplementation` は、アプリ自体の実装では利用しないがテストでは利用したいライブラリの導入に使います。 `test` は単体テストなどの通常のJVMで動作させるテストで、 `androidTest` はUIテストやAndroidのランタイムで動作を確認したいテストをします。

`denigImplementation` はデバッグビルドにのみ利用したいライブラリの導入に使います。

implementationで指定する文字列は `"groupId:artifactId:version"` という形式になっています。

`androidx.compose` だけ、違う指定をしているのがわかると思います。

```gradle
implementation platform("androidx.compose:compose-bom:2023.04.01")
implementation "androidx.compose.ui:ui"
implementation "androidx.compose.ui:ui-graphics"
implementation "androidx.compose.ui:ui-tooling-preview"
implementation "androidx.compose.material:material"
```

これは、BOMという仕組みを使うことで、複数のライブラリのバージョン指定を省略しています。

今回は、 `androidx.compose` のBOMを `implementation platform` で指定することで、Jetpack Compose関連のライブラリの `implementation` のバージョン指定を省いています。

`dependencies` ブロックで取得できる依存ライブラリは、プロジェクトの `settings.gradle` で定義されている `Maven` のアーティファクトリポジトリから取得されます。

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

このプロジェクトではGoogleとMavenCentralのアーティファクトリポジトリからライブラリを取得します。
