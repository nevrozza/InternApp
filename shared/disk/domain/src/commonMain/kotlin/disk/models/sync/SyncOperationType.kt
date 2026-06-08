package disk.models.sync

enum class SyncOperationType {
    CREATE_FOLDER,
    DELETE,
    RENAME,
    UPSERT_TEXT_FILE,
    UPLOAD_FILE,
}