//
//  ColorMapper.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.10.25.
//

import SwiftUI
import Shared

@inline(__always)
func ColorFromArgb(_ argb: Int64) -> Color {
    let a = Double((argb >> 24) & 0xFF) / 255.0
    let r = Double((argb >> 16) & 0xFF) / 255.0
    let g = Double((argb >>  8) & 0xFF) / 255.0
    let b = Double((argb      ) & 0xFF) / 255.0
    return Color(.sRGB, red: r, green: g, blue: b, opacity: a)
}

let goldPrimary = ColorFromArgb(SharedPalette.shared.GoldPrimary)
let turquoiseSeconday = ColorFromArgb(SharedPalette.shared.TurquoiseSecondary)
let orangeAccent = ColorFromArgb(SharedPalette.shared.OrangeAccent)
let textPrimary = ColorFromArgb(SharedPalette.shared.TextPrimary)
let textSecondary = ColorFromArgb(SharedPalette.shared.TextSecondary)
let backgroundLight = ColorFromArgb(SharedPalette.shared.BackgroundLight)
let backgroundDark = ColorFromArgb(SharedPalette.shared.BackgroundDark)
