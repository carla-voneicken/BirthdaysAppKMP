//
//  CustomFAB.swift
//  iosApp
//
//  Created by Carla von Eicken on 14.11.25.
//

import SwiftUI

struct CustomFAB: View {
    var body: some View {
        ZStack {
            Circle()
                .fill(goldPrimary)
                .frame(width: 80, height: 80)
                .shadow(color: backgroundDark.opacity(0.4), radius: 8, x: 5, y: 5)
            Image("BirthdayCakeShadow")
                .renderingMode(.template)
                .resizable()
                .scaledToFit()
                .frame(width: 50, height: 50)
                .foregroundStyle(.white)
            Image(systemName: "plus")
                .font(.system(size: 20, weight: .bold))
                .foregroundStyle(goldPrimary)
                .offset(y: 10)
        }
    }
}
