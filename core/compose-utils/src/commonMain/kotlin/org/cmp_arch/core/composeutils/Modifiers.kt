package org.cmp_arch.core.composeutils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.screenHorizontalPadding(): Modifier = this.padding(horizontal = LocalSpacing.md)

fun Modifier.screenVerticalPadding(): Modifier = this.padding(vertical = LocalSpacing.md)

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}
