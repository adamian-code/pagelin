package ui

enum class SampleTemplate(
    val title: String,
    val description: String
) {
    Report(
        title = "Report",
        description = "Dynamic reports with measured pagination."
    ),
    Table(
        title = "Table",
        description = "Large tables with automatic pagination."
    ),
    Custom(
        title = "Custom",
        description = "Build PDFs using your own Compose pages."
    )
}