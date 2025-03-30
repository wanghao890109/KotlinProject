package org.example.project.uitls

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout


fun Modifier.visible(visible: Boolean) =
    this.then(if (!visible) Modifier.layout { _, _ -> layout(0, 0) {} } else Modifier)