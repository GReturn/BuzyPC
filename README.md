# BuzyPC

## Project Overview
Badly need a PC? Have a limited budget? BuzyPC will provide you a PC build on your own terms!

This project is a mock-up of what the true application should expectedly function.
XML is used for UI layouts and Kotlin for logic. Jetpack Compose is **not** used in this project. 

## Table of Contents
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Kotlin Code Guidelines](#kotlin-code-guidelines)
- [XML UI Guidelines](#xml-ui-guidelines)
- [Styles and Themes](#styles-and-themes)
- [Resource Organization](#resource-organization)
- [Image Asset Guidelines](#image-asset-guidelines)
- [Dependency Management](#dependency-management)
- [API Integration](#api-integration)
- [Version Control](#version-control)

---

## Tech Stack
- **Language**: Kotlin
- **UI Framework**: XML-based layouts
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Testing**: JUnit

## Project Structure
```
app/
│── src/
│   ├── main/
│   │   ├── java/io/buzypc/app/
│   │   │   ├── data/       # Data sources (API, repositories)
│   │   │   ├── ui/         # UI components (Activities, Fragments, ViewModels)
│   │   │   ├── utils/      # Utility classes & extensions
│   │   │   ├── di/         # Dependency Injection modules
│   │   ├── res/
│   │   │   ├── drawable/   # Images and vector assets
│   │   │   ├── layout/     # XML UI layouts
│   │   │   ├── values/     # Colors, strings, dimensions, styles
│   │   │   ├── mipmap/     # App icons
```

## Kotlin Code Guidelines
We will follow the coding style guide provided by Android: 
[Android - Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)

- Use **PascalCase** for classes: `class UserRepository {}`
- Use **camelCase** for function names and local variables: `fun foo() {}`
- Use **UPPERCASE_WITH_UNDERSCORES** for constants: `const val API_TIMEOUT = 30L`
- Use **lower_snake_case** for XML IDs and resource names: `btn_submit`
- Use **expression body functions** when possible for one-liner functions:
  ```kotlin
  fun sum(a: Int, b: Int) = a + b
  ```
- Use **scoped functions** (`apply`, `let`, `also`, `run`, `with`) properly.
- Prefer **immutable variables** (`val` over `var`).
- Use **coroutines** for asynchronous work.

## XML UI Guidelines
- Use **ConstraintLayout** for flexible layouts.
- Avoid hardcoded dimensions; use `values/dimens.xml` instead.
- Keep IDs meaningful: `btn_login`, `tv_username`, `iv_logo`.
- Use **ViewBinding** or **DataBinding** to avoid `findViewById()`.
- Example XML format:
  ```xml
  <Button
      android:id="@+id/btn_submit"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="Submit" />
  ```

## Styles and Themes
- Define a **base theme** in `styles.xml`.
- Use separate styles for buttons, text, and input fields.
- Example:
  ```xml
  <style name="Base.Button">
      <item name="android:background">@color/primary</item>
      <item name="android:textColor">@color/white</item>
  </style>
  ```

## Resource Organization
- Store **strings** in `strings.xml`.
- Store **colors** in `colors.xml`.
- Store **dimensions** in `dimens.xml`.
- Example `colors.xml`:
  ```xml
  <color name="primary">#6200EE</color>
  ```

## Image Asset Guidelines
- Use **vector drawables** whenever possible.
- Images should be placed in `res/drawable/`.
- Keep file names descriptive: `ic_launcher.png`, `bg_splash.jpg`.
- Use Android’s **Adaptive Icons** for app icons.

## Dependency Management
- Use **Gradle Kotlin DSL** (`build.gradle.kts`).
- Keep dependencies updated.
- Use `implementation` for standard dependencies, `api` for shared ones. For more details, 
[refer to this StackOverflow question](https://stackoverflow.com/questions/44413952/gradle-implementation-vs-api-configuration)
- Example `build.gradle.kts`:
  ```kotlin
  dependencies {
      implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
      implementation("com.squareup.retrofit2:retrofit:2.9.0")
  }
  ```

## API Integration
- Use **Retrofit** for HTTP requests.
- Use **Gson** for JSON parsing.
- Handle errors properly with **sealed classes**.
- Example:
  ```kotlin
  sealed class ApiResponse<out T> {
      data class Success<T>(val data: T) : ApiResponse<T>()
      data class Error(val message: String) : ApiResponse<Nothing>()
  }
  ```

## Version Control
- Follow **Git flow**.
- Use meaningful commit messages:
  ```
  feat: Add user login feature
  fix: Resolve crash in MainActivity
  refactor: Improve performance of data fetching
  ```
- Create feature branches: `feature/login-screen`.
- Keep `main` branch stable.

---

## Contributing
1. Clone the repository.
2. Create a feature branch (`dev/feature/your-feature`).
3. Fragment your code changes into specific purposes. 
Avoid making a large commit that span across multiple files with different purposes.
4. Commit changes with clear messages.
5. Push and create a pull request.

