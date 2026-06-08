package disk.models.resources

sealed interface FileResource : DiskResource {
    val md5: String?
}