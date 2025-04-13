package com.example.blogmultiplatform.styles


import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.selectors.anyLink
import com.varabyte.kobweb.silk.style.CssStyle
import org.jetbrains.compose.web.css.ms

val  CategoryItemStyle = CssStyle {
    base {
        Modifier
            .color(Colors.White)
            .transition(Transition.of(property = "color", duration = 200.ms))
    }
    anyLink {
        Modifier.color(Colors.White)
    }
    hover {
        Modifier.color(Theme.Primary.rgb)
    }
}