//
//  PressScaleStyle.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.10.25.
//


import SwiftUI

// scales the item down a little when tapped
struct PressScaleStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.96 : 1.0)
            .animation(.spring(response: 0.2, dampingFraction: 0.7), value: configuration.isPressed)
    }
}
