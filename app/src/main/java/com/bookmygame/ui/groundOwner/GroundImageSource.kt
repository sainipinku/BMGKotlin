package com.bookmygame.ui.groundOwner

import android.net.Uri

sealed class GroundImageSource {
    data class Local(val uri: Uri) : GroundImageSource()
    data class URL(val url: String) : GroundImageSource()
}