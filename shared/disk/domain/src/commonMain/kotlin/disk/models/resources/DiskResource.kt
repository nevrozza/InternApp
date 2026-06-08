package disk.models.resources

import utils.types.DiskPath
import kotlin.time.Instant


sealed interface DiskResource {
    val localId: String
    val resourceId: String?
    val path: DiskPath
    val parentPath: DiskPath
    val name: String
    val modified: Instant
}