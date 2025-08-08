## Setup

1. リポジトリをクローンします。
2. Android Studio で開き、ルートフォルダにある `properties.secrets.example` を `properties.secrets` に名前を変えます。そして [https://console.mimi.fd.ai](https://console.mimi.fd.ai) の mimi コンソールで見つけられる情報を入力します。
3. アプリをビルドして実行します。

```properties
MIMI_CLIENT_ID=             // 空のままにします
MIMI_CLIENT_SECRET=         // クライアントシークレット
MIMI_APP_ID=                // アプリ ID
MIMI_APP_CLIENT_ID=         // クライアントを作成したときの App client ID
MIMI_APP_NAME=              // mimi コンソールで設定したアプリ名
```

## Features

#### 完了したステップ

* [x] Step 1
* [x] Step 2
* [x] Step 3

#### Transcription

* マイクから音声を取り、リアルタイムで mimi ASR WebSocket API に送ります。それからストリーミング書き起こし結果をリアルタイムで受け取り表示します。

#### Keyword management

* キーワードを作成、削除、更新、アクティブ／非アクティブにできます。
* 最新の結果は、書き起こし中にマッチしたキーワードをダイアログで表示します。

機能は Google Pixel 7 でテストしました。

## Testing

テストは期待される uiState の動き、認証、ViewModel、Flow と入力結果をカバーします。

#### 技術実装

* Jetpack Compose
* 機能ごとのパッケージ構造
* MVI／クリーンアーキテクチャ
* UI モデル／ドメインモデル／DTO モデル

**主なライブラリ**

* OkHttp
* Retrofit
* Room
* Dagger Hilt

**テスト用ライブラリ**

* Google Truth
* Turbine
* Mockk

---

## TODO

アプリで改善したいことです。

#### DATA

* [ ] トークンをキャッシュするため、暗号化付き DataStore を追加します。
* [ ] リクエストを包むカスタムエラーハンドリングクラスを追加します。これでドメイン層とプレゼンテーション層で反応したいエラーを選べます。今は Kotlin の `Result` クラスを使っていますが、もっと細かい `Result` クラスに置き換えたいです。
* [ ] いくつかの依存関係を別モジュールにリファクタリングします。
* [ ] 接続エラーや切断時に WebSocket の再接続戦略を追加します。
* [ ] スタート／ストップボタンを繰り返し押せないようにします。
* [ ] 特定の状況で音声と WebSocket 通信がずれるので、バックプレッシャー処理を追加します。

#### UX

* [ ] 成功とエラーの状態を表示するスナックバーを追加します。
* [ ] 今は権限処理がとてもシンプルです。権限が拒否されたとき、ユーザーに手動で権限を有効にするよう知らせるダイアログを追加します。
* [ ] すでに存在するキーワードを追加しようとしているかどうかを検出する機能を追加します。

#### CI/CD

* [ ] `.yml` ファイルを追加し、コミット、PR、アーティファクト作成用の GitHub Actions フローを設定します。