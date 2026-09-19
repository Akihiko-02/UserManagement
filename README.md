# User Management System

A full-stack User Management System built with **Java Spring Boot** and **React**.

This project demonstrates user registration, login, JWT-based authentication, protected routes, and secure communication between a React frontend and Spring Boot REST API.

##  Features

* User Registration
* User Login
* JWT-based Authentication
* Protected API Endpoints
* Protected React Routes
* Password Encryption
* User Management
* RESTful API
* React-based User Interface
* Authentication state management
* Logout functionality

---

##  Tech Stack

### Backend

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Security**
* **JWT (JSON Web Token)**
* **Maven**
* **REST API**

### Frontend

* **React**
* **Vite**
* **JavaScript**
* **React Router**
* **HTML / CSS**

### Database

* **SQL Database**

### Development Tools

* **Git**
* **GitHub**
* **VS Code / IntelliJ IDEA**

---

#  Authentication & Security

One of the main purposes of this project is to demonstrate how authentication can be implemented using **JWT with Spring Security**.

Instead of maintaining a traditional server-side session, the application uses a JWT token to authenticate requests.

## JWT Authentication Flow

```text
                    ┌──────────────────┐
                    │   React Client   │
                    └────────┬─────────┘
                             │
                             │ Login
                             │ username + password
                             ▼
                    ┌──────────────────┐
                    │   Spring Boot    │
                    │    REST API      │
                    └────────┬─────────┘
                             │
                             │ Authenticate User
                             ▼
                    ┌──────────────────┐
                    │ Spring Security  │
                    └────────┬─────────┘
                             │
                             │ Valid User
                             ▼
                    ┌──────────────────┐
                    │ Generate JWT     │
                    │     Token        │
                    └────────┬─────────┘
                             │
                             │ JWT
                             ▼
                    ┌──────────────────┐
                    │   React Client   │
                    └────────┬─────────┘
                             │
                             │ Authorization:
                             │ Bearer <JWT>
                             ▼
                    ┌──────────────────┐
                    │    JWT Filter    │
                    └────────┬─────────┘
                             │
                     Validate Token
                             │
                             ▼
                    ┌──────────────────┐
                    │ Protected API    │
                    └──────────────────┘
```

## How JWT Works in This Project

### 1. User Login

The user enters their login credentials through the React frontend.

```text
Username / Email
Password
     │
     ▼
Spring Boot Login API
```

The backend authenticates the user using Spring Security.

### 2. JWT Token Generation

After successful authentication, the backend generates a JWT token.

```text
User Authentication
        ↓
Successful Login
        ↓
JWT Token Generated
        ↓
Token Returned to React
```

The token contains information that can be used to identify the authenticated user and has an expiration time.

### 3. Sending the JWT

When accessing a protected API, the frontend sends the token through the HTTP `Authorization` header.

```http
Authorization: Bearer <JWT_TOKEN>
```

### 4. JWT Filter

The Spring Security filter checks incoming requests.

The JWT filter:

1. Reads the `Authorization` header.
2. Extracts the JWT token.
3. Validates the token.
4. Extracts the user information from the token.
5. Creates the authentication information for Spring Security.
6. Allows the request to continue when authentication is valid.

```text
HTTP Request
     │
     ▼
Authorization Header
     │
     ▼
Extract JWT
     │
     ▼
Validate JWT
     │
     ├── Invalid → Reject Request
     │
     └── Valid
          │
          ▼
   Set Authentication
          │
          ▼
   Protected Controller
```

---

#  Password Security

User passwords should never be stored as plain text.

The application uses password hashing through Spring Security so that the original password is not directly stored in the database.

```text
User Password
      │
      ▼
Password Encoder
      │
      ▼
Hashed Password
      │
      ▼
Database
```

During login, the submitted password is checked against the stored password hash.

---

#  Protected Routes

The application contains protected resources that require authentication.

On the frontend, React Router is used to control access to protected pages.

```text
User
 │
 ▼
Protected Route
 │
 ├── Authenticated ──► Dashboard
 │
 └── Not Authenticated
              │
              ▼
            Login
```

The backend also protects API endpoints using Spring Security.

This means frontend route protection alone is not considered sufficient security. The backend validates authentication before allowing access to protected resources.

---

#  Project Architecture

The project follows a frontend/backend architecture.

```text
┌─────────────────────────────┐
│        React Frontend       │
│                             │
│ Login / Signup / Dashboard  │
│ React Router / UI           │
└──────────────┬──────────────┘
               │
               │ HTTP / REST API
               │
               ▼
┌─────────────────────────────┐
│       Spring Boot API       │
│                             │
│ Controllers                 │
│ Services                    │
│ Security                    │
│ JWT Filter                  │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          Database           │
│                             │
│        User Data            │
└─────────────────────────────┘
```

---

#  Project Structure

```text
UserManagement
│
├── src
│   └── main
│       ├── java
│       │   └── ...
│       │
│       └── resources
│           ├── application.yml
│           └── templates
│               └── React
│                   └── UserManagementUI
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

The backend contains the Spring Boot application, security configuration, JWT authentication components, controllers, DTOs, and related services.

The React frontend contains the user interface, routing, login/signup pages, protected routes, and dashboard.

---

#  REST API

The application communicates between the React frontend and Spring Boot backend through REST APIs.

Typical authentication flow:

```text
POST   /login
POST   /signup
```

Authenticated requests include:

```http
Authorization: Bearer <JWT_TOKEN>
```

The exact API endpoints may vary depending on the current implementation.

---

#  How to Run

## Backend

Clone the repository:

```bash
git clone https://github.com/Akihiko-02/UserManagement.git
```

Go to the project directory:

```bash
cd UserManagement
```

Run the Spring Boot application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Frontend

Go to the React project:

```bash
cd src/main/resources/templates/React/UserManagementUI
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

---

#  Authentication Example

### Login Request

```http
POST /login
Content-Type: application/json
```

Example:

```json
{
  "username": "example",
  "password": "password"
}
```

### Authenticated Request

```http
GET /api/protected
Authorization: Bearer <JWT_TOKEN>
```

The backend validates the JWT before processing the protected request.

---

# 💡 What I Learned

Through this project, I practiced:

* Building REST APIs with Spring Boot
* Connecting a React frontend with a Spring Boot backend
* Implementing authentication with Spring Security
* Understanding JWT authentication
* Creating and validating JWT tokens
* Using authentication filters
* Protecting backend API endpoints
* Protecting frontend routes with React Router
* Password hashing
* DTO-based data transfer
* Working with databases
* Using Git and GitHub
* Debugging frontend and backend integration issues

---

#  Future Improvements

Possible future improvements include:

* Role-based authorization
* Refresh token implementation
* Improved token storage strategy
* Email verification
* Password reset functionality
* User profile management
* Pagination and search
* More comprehensive API validation
* Unit and integration testing
* Docker deployment

---

#  Author

**Akihiko-02**

GitHub:
https://github.com/Akihiko-02

---

##  Project Purpose

This project was created as a practical full-stack application to understand how **Spring Boot, Spring Security, JWT authentication, REST APIs, React, and database technologies** can work together in a real-world application.


---

# User Management System

**Java Spring Boot** と **React** を使用して開発した、フルスタックのユーザー管理システムです。

ユーザー登録、ログイン、JWTを使用した認証、認証が必要なAPIや画面へのアクセス制御などを実装しています。

---

##  主な機能

* ユーザー登録
* ユーザーログイン
* JWTによる認証
* 認証が必要なAPIのアクセス制御
* Reactのルート保護
* パスワードのハッシュ化
* ユーザー管理
* REST API
* Reactによるユーザーインターフェース
* 認証状態の管理
* ログアウト

---

##  使用技術

### Backend

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Security**
* **JWT (JSON Web Token)**
* **Maven**
* **REST API**

### Frontend

* **React**
* **Vite**
* **JavaScript**
* **React Router**
* **HTML / CSS**

### Database

* **SQL Database**

### Development Tools

* **Git**
* **GitHub**
* **VS Code / IntelliJ IDEA**

---

#  認証・セキュリティ

このプロジェクトでは、**Spring SecurityとJWTを使用した認証機能**を実装しています。

サーバー側でセッションを管理する方式ではなく、JWTを使用してリクエストの認証を行っています。

## JWT認証の流れ

```text
                    ┌──────────────────┐
                    │   React Client   │
                    └────────┬─────────┘
                             │
                             │ ログイン
                             │ Username + Password
                             ▼
                    ┌──────────────────┐
                    │   Spring Boot    │
                    │    REST API      │
                    └────────┬─────────┘
                             │
                             │ ユーザー認証
                             ▼
                    ┌──────────────────┐
                    │ Spring Security  │
                    └────────┬─────────┘
                             │
                             │ 認証成功
                             ▼
                    ┌──────────────────┐
                    │   JWTを生成      │
                    └────────┬─────────┘
                             │
                             │ JWT
                             ▼
                    ┌──────────────────┐
                    │   React Client   │
                    └────────┬─────────┘
                             │
                             │ Authorization:
                             │ Bearer <JWT>
                             ▼
                    ┌──────────────────┐
                    │    JWT Filter    │
                    └────────┬─────────┘
                             │
                         JWTを検証
                             │
                             ▼
                    ┌──────────────────┐
                    │ Protected API    │
                    └──────────────────┘
```

## JWTの処理

### 1. ログイン

Reactのログイン画面からユーザーがログイン情報を入力します。

```text
Username / Email
Password
     │
     ▼
Spring Boot Login API
```

Spring Securityを使用してユーザーを認証します。

### 2. JWTトークンの生成

認証に成功すると、Spring Boot側でJWTを生成します。

```text
ユーザー認証
    ↓
認証成功
    ↓
JWT生成
    ↓
ReactへJWTを返す
```

JWTにはユーザーを識別するための情報などが含まれ、有効期限も設定されています。

### 3. JWTの送信

認証が必要なAPIにアクセスするとき、ReactからHTTPリクエストの `Authorization` ヘッダーにJWTを付けて送信します。

```http
Authorization: Bearer <JWT_TOKEN>
```

### 4. JWT Filter

Spring SecurityのJWT Filterで、リクエストに含まれているJWTを確認します。

JWT Filterでは、主に以下の処理を行います。

1. `Authorization` ヘッダーを取得する
2. JWTを取り出す
3. JWTの有効性を確認する
4. JWTからユーザー情報を取得する
5. Spring Securityに認証情報を設定する
6. 認証が有効な場合、次の処理へ進む

```text
HTTP Request
     │
     ▼
Authorization Header
     │
     ▼
JWTを取得
     │
     ▼
JWTを検証
     │
     ├── 無効 → リクエストを拒否
     │
     └── 有効
          │
          ▼
     認証情報を設定
          │
          ▼
     Protected Controller
```

---

#  パスワードのセキュリティ

ユーザーのパスワードをそのままデータベースに保存しないようにしています。

Spring SecurityのPassword Encoderを使用して、パスワードをハッシュ化して保存します。

```text
ユーザーパスワード
      │
      ▼
Password Encoder
      │
      ▼
ハッシュ化されたパスワード
      │
      ▼
Database
```

ログイン時には、入力されたパスワードとデータベースに保存されているハッシュ値を比較して認証します。

---

#  Protected Routes

認証が必要な画面には、ログインしていないユーザーがアクセスできないようにしています。

React Routerを使用して、フロントエンド側のルートを保護しています。

```text
User
 │
 ▼
Protected Route
 │
 ├── 認証済み ──► Dashboard
 │
 └── 未認証
        │
        ▼
      Login
```

また、バックエンド側でもSpring Securityを使用してAPIを保護しています。

そのため、フロントエンド側のルート保護だけではなく、バックエンド側でも認証情報を確認します。

---

#  システム構成

```text
┌─────────────────────────────┐
│        React Frontend       │
│                             │
│ Login / Signup / Dashboard  │
│ React Router / UI           │
└──────────────┬──────────────┘
               │
               │ HTTP / REST API
               │
               ▼
┌─────────────────────────────┐
│       Spring Boot API       │
│                             │
│ Controllers                 │
│ Services                    │
│ Security                    │
│ JWT Filter                  │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          Database           │
│                             │
│        User Data            │
└─────────────────────────────┘
```

---

#  プロジェクト構成

```text
UserManagement
│
├── src
│   └── main
│       ├── java
│       │   └── ...
│       │
│       └── resources
│           └── templates
│               └── React
│                   └── UserManagementUI
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

バックエンドには、Spring Boot、Security設定、JWT認証、Controller、DTO、Serviceなどを実装しています。

フロントエンドには、ログイン、ユーザー登録、Dashboard、React Router、Protected Routeなどを実装しています。

---

#  REST API

ReactとSpring Bootの間では、REST APIを使用してデータを通信します。

認証に関係する処理では、ログインやユーザー登録などのAPIを使用します。

認証済みのリクエストでは、以下のようにJWTを送信します。

```http
Authorization: Bearer <JWT_TOKEN>
```

---

#  起動方法

## Backend

リポジトリをCloneします。

```bash
git clone https://github.com/Akihiko-02/UserManagement.git
```

プロジェクトディレクトリへ移動します。

```bash
cd UserManagement
```

Spring Bootを起動します。

```bash
./mvnw spring-boot:run
```

Windowsの場合：

```bash
mvnw.cmd spring-boot:run
```

## Frontend

Reactプロジェクトへ移動します。

```bash
cd src/main/resources/templates/React/UserManagementUI
```

依存関係をインストールします。

```bash
npm install
```

開発サーバーを起動します。

```bash
npm run dev
```

---

#  JWT認証の例

### Login Request

```http
POST /login
Content-Type: application/json
```

```json
{
  "username": "example",
  "password": "password"
}
```

### Authenticated Request

```http
GET /api/protected
Authorization: Bearer <JWT_TOKEN>
```

バックエンドでは、リクエストを処理する前にJWTの有効性を確認します。

---

#  このプロジェクトで学んだこと

このプロジェクトを通して、以下の技術を学び、実装しました。

* Spring BootでREST APIを開発する方法
* ReactとSpring Bootを連携する方法
* Spring Securityを使用した認証
* JWT認証の仕組み
* JWTの生成と検証
* JWT Filterの実装
* バックエンドAPIの保護
* React Routerによる画面の保護
* パスワードのハッシュ化
* DTOを使用したデータの受け渡し
* データベースとの連携
* Git / GitHubの使用
* FrontendとBackendの連携・デバッグ

---

#  今後追加したい機能

* Role-based Authorization
* Refresh Token
* メール認証
* パスワードリセット
* ユーザープロフィール管理
* Pagination / Search
* API Validationの改善
* Unit Test / Integration Test
* Docker対応

---

#  Author

**Akihiko-02**

GitHub:
https://github.com/Akihiko-02

---

##  プロジェクトの目的

このプロジェクトは、**Spring Boot、Spring Security、JWT認証、REST API、React、Database**を使用したフルスタックアプリケーションを実際に開発することを目的として作成しました。

特に、**JWTを使用した認証処理と、Spring SecurityによるAPIのアクセス制御**について理解を深めることを目的としています。
