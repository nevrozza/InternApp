package disk.models.resources

import disk.models.DiskPath
import kotlin.time.Instant

data class DirectoryResource(
    override val localId: String,
    override val resourceId: String?,
    override val path: DiskPath,
    override val parentPath: DiskPath,
    override val name: String,
    override val modified: Instant,
) : DiskResource