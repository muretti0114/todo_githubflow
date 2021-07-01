# ToDoアプリ - 簡単版

## 準備
- 手元のMySQLにToDoアプリ用のDBを作成．
    - データベース名： todoapp
    - ユーザ名： todouser
    - パスワード： todotodo
```
mysql> create database todoapp;
mysql> create user todouser identified by 'todotodo';
mysql> grant all on todoapp.* to todouser;
```

## デプロイ & 実行
- ToDoV1.war を手元のPCの tomcatの webappsにコピー
- ブラウザから，http://localhost:8080/ToDoV1/ にアクセス

## 設計
- メンバー，および，ToDo項目をCRUDする典型的なアプリケーション
- フロントエンド： Thymeleafでサーバ側で生成
- バックエンド： Spring MVC で普通に
    - Controller 層
        - メンバーの登録
            - 入力→チェック→登録
        - ToDo管理
            - 自分のToDo/Doneリスト表示
            - 全員のToDo/Doneリスト表示
            - ToDo登録 
            - ToDo完了 (Done)
    - Service 層
        - メンバーのCRUD
            - 作成
            - 取得
            - 全取得
            - 更新
            - 削除
        - ToDoのCRUD
    - Repository 層 (Spring JPA)
        - メンバーリポジトリ
        - ToDoリポジトリ
            - メンバのToDo取得
            - メンバのDone取得
            - 全員のToDo取得
            - 全員のDone取得
    - DB
        - MySQL

## 実装

### Entity

#### Member
ToDoアプリを利用するメンバー
- String mid;   // メンバーID．ログインに使用．英数字，ハイフン，アンダーバーで構成される文字列．省略不可
- String name;  // メンバー氏名．任意の文字列．漢字OK．省略不可

#### ToDo
ToDoの項目
- Long seq;       // ToDoの一意の通し番号．
- String title;   // ToDoの題目．省略不可．
- String mid;     // ToDoの作成者．省略不可．
- boolean done;     // 完了フラグ．
- Date createdAt; // ToDoの作成日時．システムが自動で付与．
- Date doneAt; // ToDoの完了日時．システムが自動で付与．

### Repository
#### MemberRepository
#### ToDoRepository

### Service
#### MemberService
#### ToDoService

### Controller
#### MemberController
#### ToDoController
#### ToDoAppExceptionHander






