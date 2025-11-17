//
//  ToastView.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI


// MARK: ToastView ("dumb" layout, doesn't know when to appear or disappear)
struct ToastView: View {
    
    var message: String
    var type: ToastType
    
    private let horizontalPadding: CGFloat = 20
    private let verticalPadding: CGFloat = 16
    private let cornerRadius: CGFloat = 14
    
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            // icon
            Image(systemName: type.iconFileName)
                .foregroundColor(type.tintColor)
                .font(.system(size: 20))
            // message
            Text(message)
                .font(.system(size: 16))
                .foregroundColor(.primary)
                .multilineTextAlignment(.leading)
        }
        .padding(.horizontal, horizontalPadding)
        .padding(.vertical, verticalPadding)
        // frame
        .background(
            RoundedRectangle(cornerRadius: cornerRadius)
                .fill(.white.opacity(0.9))
                .stroke(type.tintColor, lineWidth: 2)
        )
        .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 4)
        //.padding(.horizontal, 16)
    }
}
