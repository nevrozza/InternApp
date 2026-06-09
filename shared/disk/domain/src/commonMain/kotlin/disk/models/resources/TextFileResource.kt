package disk.models.resources

import utils.types.DiskPath
import kotlin.time.Instant

data class TextFileResource(
    override val localId: String,
    override val resourceId: String?,
    override val path: DiskPath,
    override val parentPath: DiskPath,
    override val name: String,
    override val modified: Instant,
    override val md5: String?,
    val textContent: String,
) : FileResource