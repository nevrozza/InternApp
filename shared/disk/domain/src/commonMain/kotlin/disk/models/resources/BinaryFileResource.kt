package disk.models.resources

import disk.models.DiskPath
import disk.models.LocalPath
import kotlin.time.Instant

data class BinaryFileResource(
    override val localId: String,
    override val resourceId: String?,
    override val path: DiskPath,
    override val parentPath: DiskPath,
    override val name: String,
    override val modified: Instant,
    override val md5: String?,
    val localFilePath: LocalPath?,
) : FileResource