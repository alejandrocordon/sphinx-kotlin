package chat.sphinx.wrapper_common


fun calculateStoragePercentage(storageData: StorageData): StoragePercentage {
    val freeStorage = storageData.freeStorage?.value ?: 0L
    val totalStorage = freeStorage + storageData.images.totalSize.value +
            storageData.video.totalSize.value + storageData.audio.totalSize.value +
            storageData.files.totalSize.value

    // Helper function to format the storage value
    fun formatStorageValue(value: Long): Float {
        val formattedString = value.toString().replace(",", "")
        return String.format("%.3f", formattedString.toFloat() / totalStorage).toFloat()
    }

    return StoragePercentage(
        freeStorage = formatStorageValue(freeStorage),
        image = formatStorageValue(storageData.images.totalSize.value),
        video = formatStorageValue(storageData.video.totalSize.value),
        audio = formatStorageValue(storageData.audio.totalSize.value),
        files = formatStorageValue(storageData.files.totalSize.value)
    )
}

fun calculateUsedStoragePercentage(storageData: StorageData): Float {
    val usedStorage = storageData.usedStorage.value
    val freeStorage = storageData.freeStorage?.value ?: 0L
    val totalStorage = usedStorage + freeStorage
    return String.format("%.3f", usedStorage.toFloat() / totalStorage).toFloat()
}

data class StoragePercentage(
    val freeStorage: Float,
    val image: Float,
    val video: Float,
    val audio: Float,
    val files: Float,
)