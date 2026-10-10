# Pagelin

**Compose-first PDF generation for Android.**

Pagelin is an Android library for building PDF documents using Jetpack Compose.

Create reports, tables, or completely custom multi-page documents with Compose, then preview, share, or save the generated PDF.

## Features

- Jetpack Compose-first API
- Multi-page PDF generation
- Automatic pagination
- Ready-to-use Report and Table templates
- Fully custom Compose pages
- PDF preview and sharing
- Save generated PDFs to Downloads
- Distributed through Maven Central
- Android API 29+

## Installation

Add Pagelin to your app module:

```kotlin
dependencies {
    implementation("io.github.adamian-code:pagelin:1.0.3")
}
```

Make sure Maven Central is available in your project:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

## Quick Start

Generate a PDF directly from Compose content:

```kotlin
val result = Pagelin.generate(
    activity = activity,
    config = PdfConfig(
        fileName = "document.pdf"
    ),
    pages = listOf(
        {
            Text("Hello from Pagelin!")
        }
    )
)

val document = result.getOrThrow()

document.preview(activity)
```

## Templates

Pagelin includes document templates for common use cases:

- Report
- Table
- Custom Compose documents

Templates handle document structure and pagination while still allowing content to be rendered with Jetpack Compose.

## Generated PDFs

After generating a document, Pagelin returns a `GeneratedPdf`.

You can preview the generated PDF:

```kotlin
document.preview(activity)
```

Share it:

```kotlin
document.share(activity)
```

Or save it to Downloads:

```kotlin
document.saveToDownloads(activity)
```

## Custom Documents

You are not limited to the built-in templates.

Any Compose UI can be used as a PDF page:

```kotlin
Pagelin.generate(
    activity = activity,
    config = PdfConfig(
        fileName = "custom.pdf"
    ),
    pages = listOf(
        {
            Column {
                Text("My document")
                Text("Built with Jetpack Compose")
            }
        }
    )
)
```

Multiple Compose pages can be generated in the same document:

```kotlin
Pagelin.generate(
    activity = activity,
    config = PdfConfig(
        fileName = "multi-page.pdf"
    ),
    pages = listOf(
        {
            Text("Page 1")
        },
        {
            Text("Page 2")
        },
        {
            Text("Page 3")
        }
    )
)
```

## PDF Configuration

Basic PDF options are configured through `PdfConfig`:

```kotlin
val config = PdfConfig(
    fileName = "document.pdf",
    marginMm = 10f
)
```

## Theming

Pagelin can render PDF content using your Compose theme:

```kotlin
Pagelin.generate(
    activity = activity,
    config = PdfConfig("document.pdf"),
    pages = pages,
    theme = { content ->
        MaterialTheme {
            content()
        }
    }
)
```

## Requirements

- Android API 29+
- Jetpack Compose
- Kotlin 1.8.10+

## Compatibility

Pagelin 1.0.3 requires Android API 29 or higher.

The library is built with Kotlin 1.8.10 and targets JVM 11.

## Sample App

The repository includes a sample application demonstrating:

- Report generation
- Table generation
- Custom Compose PDFs

Run the `app` module to explore the examples.

## License

Pagelin is licensed under the Apache License 2.0.

See the `LICENSE` file for details.